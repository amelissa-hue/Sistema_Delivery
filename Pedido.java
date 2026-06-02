import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Pedido implements Comparable<Pedido> {
    private final int id;
    private final String cliente;
    private final Restaurante restaurante;
    private final Zona zonaDestino;
    private final int prioridadBase;
    private final LocalDateTime timestamp;
    private EstadoPedido estado;
    private Repartidor repartidorAsignado;
    private final boolean vip;
    private final List<Producto> productos;

    public Pedido(int id, String cliente, Restaurante restaurante, Zona zonaDestino, int prioridad) {
        this(id, cliente, restaurante, zonaDestino, prioridad, false);
    }

    protected Pedido(int id, String cliente, Restaurante restaurante, Zona zonaDestino, int prioridad, boolean esVip) {
        this.id = id;
        this.cliente = cliente;
        this.restaurante = restaurante;
        this.zonaDestino = zonaDestino;
        this.prioridadBase = prioridad;
        this.timestamp = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
        this.repartidorAsignado = null;
        this.vip = esVip;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto p) { productos.add(p); }
    public List<Producto> getProductos() { return productos; }

    public double getTotalProductos() {
        double total = 0;
        for (Producto p : productos) total += p.getPrecio();
        return total;
    }

    public String getResumenProductos() {
        if (productos.isEmpty()) return "Sin productos";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < productos.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(productos.get(i).getNombre());
        }
        return sb.toString();
    }

    public int getId() { return id; }
    public String getCliente() { return cliente; }
    public Restaurante getRestaurante() { return restaurante; }
    public Zona getZonaDestino() { return zonaDestino; }
    public int getPrioridadBase() { return prioridadBase; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public EstadoPedido getEstado() { return estado; }
    public Repartidor getRepartidorAsignado() { return repartidorAsignado; }
    public boolean isVip() { return vip; }

    public void setEstado(EstadoPedido estado) { this.estado = estado; }
    public void setRepartidorAsignado(Repartidor repartidor) { this.repartidorAsignado = repartidor; }
    public void marcarPreparacion() { this.estado = EstadoPedido.EN_PREPARACION; }
    public void marcarEnCamino() { this.estado = EstadoPedido.EN_CAMINO; }
    public void marcarEntregado() { this.estado = EstadoPedido.ENTREGADO; }
    public void resetearEstado()  { this.estado = EstadoPedido.PENDIENTE; }

    public int getPrioridad() { return prioridadBase; }

    @Override
    public int compareTo(Pedido o) { return Integer.compare(this.getPrioridad(), o.getPrioridad()); }

    @Override
    public String toString() {
        return "Pedido[" + id + "] " + cliente + " ➔ " + zonaDestino.getNombre() + " [" + estado + "]" +
               (repartidorAsignado != null ? " - Repartidor: " + repartidorAsignado.getNombre() : "");
    }
}
