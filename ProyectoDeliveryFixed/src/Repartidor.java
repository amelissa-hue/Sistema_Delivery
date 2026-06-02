
public class Repartidor {
    private final int id;
    private final String nombre;
    private boolean disponible;
    private Zona zonaActual;
    private Pedido pedidoActual;

    public Repartidor(int id, String nombre, Zona zonaActual) {
        this.id = id;
        this.nombre = nombre;
        this.disponible = true;
        this.zonaActual = zonaActual;
        this.pedidoActual = null;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public boolean isDisponible() { return disponible; }
    public Zona getZonaActual() { return zonaActual; }
    public Pedido getPedidoActual() { return pedidoActual; }
    public void setZonaActual(Zona zona) { this.zonaActual = zona; }

    public void asignarPedido(Pedido pedido) {
        this.pedidoActual = pedido;
        this.disponible = false;
    }

    public void liberarPedido() {
        liberarPedido(true);
    }

    public void liberarPedido(boolean moverADestino) {
        if (pedidoActual != null) {
            if (moverADestino) {
                this.zonaActual = pedidoActual.getZonaDestino();
            }
            this.pedidoActual = null;
            this.disponible = true;
        }
    }

    @Override
    public String toString() {
        return "Repartidor[" + id + "] " + nombre + 
               " [" + (disponible ? "LIBRE" : "OCUPADO") + "]" + 
               (pedidoActual != null ? " - Pedido#" + pedidoActual.getId() : "");
    }
}
