import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.time.LocalDateTime;

public class DeliveryService {
    private static final int ID_INICIAL = 1000;
    private final HashTablePedido pedidos;
    private final QueuePedido colaNormal;
    private final HeapPedido colaVip;
    private final LinkedListRepartidor repartidores;
    private final StackEstadoPedido auditoria;
    private final BSTRestaurante restaurantes;
    private final WeightedGraph grafo;
    private final DijkstraService dijkstra;
    private int contadorId;

    public DeliveryService() {
        this.pedidos      = new HashTablePedido();
        this.colaNormal   = new QueuePedido();
        this.colaVip      = new HeapPedido();
        this.repartidores = new LinkedListRepartidor();
        this.auditoria    = new StackEstadoPedido();
        this.restaurantes = new BSTRestaurante();
        this.grafo        = new WeightedGraph();
        this.dijkstra     = new DijkstraService(grafo);
        this.contadorId   = ID_INICIAL;
    }

    public int generarId() { return ++contadorId; }

    public void newOrder(Pedido p) {
        pedidos.put(p.getId(), p);
        if (p.isVip()) colaVip.insert(p); else colaNormal.enqueue(p);
    }

    public Pedido simulate(StringBuilder logsGps) {
        if (!hasPendingOrders()) return null;
        if (repartidores.findAvailableRandom() == null) {
            throw new IllegalStateException("No hay repartidores libres en este momento.");
        }

        Pedido p = getNextOrder();

        p.marcarPreparacion();
        auditoria.push(new EventoAuditoria(p.getId(), EstadoPedido.EN_PREPARACION, LocalDateTime.now()));

        Repartidor rep = assignCourier(p);

        // Ruta completa: posición del repartidor → restaurante → destino
        Zona posRep    = rep.getZonaActual();
        Zona restauranteZona = p.getRestaurante().getZona();
        Zona destino   = p.getZonaDestino();

        // Tramo 1: repartidor → restaurante
        Zona[] tramo1  = dijkstra.shortestPath(posRep, restauranteZona);
        int    mins1   = dijkstra.getTotalMinutes(posRep, restauranteZona);

        // Tramo 2: restaurante → destino (ruta más corta)
        Zona[] tramo2  = dijkstra.shortestPath(restauranteZona, destino);
        int    mins2   = dijkstra.getTotalMinutes(restauranteZona, destino);

        int    total   = mins1 + mins2;

        // Construir log GPS detallado
        logsGps.append("╔══════════════════════════════════════╗\n");
        logsGps.append("║       GPS DIJKSTRA — RUTA ÓPTIMA     ║\n");
        logsGps.append("╚══════════════════════════════════════╝\n\n");
        logsGps.append("🚴 Repartidor : ").append(rep.getNombre()).append("\n");
        logsGps.append("📍 Posición   : ").append(posRep.getNombre()).append("\n");
        logsGps.append("🍽  Restaurante: ").append(restauranteZona.getNombre()).append("\n");
        logsGps.append("🏠 Destino    : ").append(destino.getNombre()).append("\n\n");

        logsGps.append("── TRAMO 1: Ir a recoger pedido ──\n");
        logsGps.append("   ");
        for (int i = 0; i < tramo1.length; i++) {
            if (i > 0) logsGps.append(" ➔ ");
            logsGps.append(tramo1[i].getNombre());
        }
        logsGps.append("\n   ⏱ ").append(mins1).append(" min\n\n");

        logsGps.append("── TRAMO 2: Entregar al cliente ──\n");
        logsGps.append("   ");
        for (int i = 0; i < tramo2.length; i++) {
            if (i > 0) logsGps.append(" ➔ ");
            logsGps.append(tramo2[i].getNombre());
        }
        logsGps.append("\n   ⏱ ").append(mins2).append(" min\n\n");

        logsGps.append("══════════════════════════════════════\n");
        logsGps.append("⏰ TIEMPO TOTAL ESTIMADO: ").append(total).append(" minutos\n");
        logsGps.append("📦 Pedido #").append(p.getId()).append(" — ").append(p.getCliente()).append("\n");
        logsGps.append("🛒 Productos: ").append(p.getResumenProductos()).append("\n");
        logsGps.append("💰 Total    : $").append(String.format("%.2f", p.getTotalProductos())).append("\n");
        logsGps.append("══════════════════════════════════════\n");

        p.marcarEnCamino();
        auditoria.push(new EventoAuditoria(p.getId(), EstadoPedido.EN_CAMINO, LocalDateTime.now()));
        return p;
    }

    public void marcarPedidoEntregado(Pedido p) {
        p.marcarEntregado();
        auditoria.push(new EventoAuditoria(p.getId(), EstadoPedido.ENTREGADO, LocalDateTime.now()));
        if (p.getRepartidorAsignado() != null) p.getRepartidorAsignado().liberarPedido();
    }

    /** Al presionar NO en despacho: regresa el pedido a la cola correspondiente sin marcarlo entregado */
    public void devolverPedidoACola(Pedido p) {
        p.resetearEstado(); // vuelve a PENDIENTE
        p.setRepartidorAsignado(null);
        if (p.isVip()) colaVip.insert(p); else colaNormal.enqueue(p);
        auditoria.push(new EventoAuditoria(p.getId(), EstadoPedido.PENDIENTE, LocalDateTime.now()));
    }

    private Pedido getNextOrder() {
        if (!colaVip.isEmpty())    return colaVip.extractMax();
        if (!colaNormal.isEmpty()) return colaNormal.dequeue();
        return null;
    }

    private boolean hasPendingOrders() {
        return !colaVip.isEmpty() || !colaNormal.isEmpty();
    }

    private Repartidor assignCourier(Pedido p) {
        Repartidor r = repartidores.findAvailableRandom();
        if (r == null) throw new IllegalStateException("Flota ocupada: Sin repartidores libres.");
        r.asignarPedido(p);
        p.setRepartidorAsignado(r);
        return r;
    }

    public void addRepartidor(Repartidor r)  { repartidores.add(r); }
    public void addRestaurante(Restaurante r) { restaurantes.insert(r); grafo.addZone(r.getZona()); }
    public void addZone(Zona z)              { grafo.addZone(z); }
    public void connectZones(Zona a, Zona b, int d) { grafo.addEdge(a, b, d); }

    public Pedido[] getActiveOrders() {
        Pedido[] t = pedidos.getValores();
        Pedido[] a = new Pedido[t.length];
        int c = 0;
        for (Pedido p : t) if (p != null && p.getEstado() != EstadoPedido.ENTREGADO) a[c++] = p;
        Pedido[] out = new Pedido[c];
        System.arraycopy(a, 0, out, 0, c);
        return out;
    }

    public Pedido[]      getTodosLosPedidos()    { return pedidos.getValores(); }
    public Repartidor[]  getAllCouriers()        { return repartidores.toArray(); }
    public Repartidor[]  getFreeCouriers() {
        Repartidor[] t = repartidores.toArray();
        Repartidor[] l = new Repartidor[t.length];
        int c = 0;
        for (Repartidor r : t) if (r.isDisponible()) l[c++] = r;
        Repartidor[] out = new Repartidor[c];
        System.arraycopy(l, 0, out, 0, c);
        return out;
    }
    public Restaurante[]     getTopRestaurantes(int k) { return restaurantes.topK(k); }
    public WeightedGraph     getGrafo()                { return grafo; }
    public DijkstraService   getDijkstra()             { return dijkstra; }
    public StackEstadoPedido getAuditoria()            { return auditoria; }
}
