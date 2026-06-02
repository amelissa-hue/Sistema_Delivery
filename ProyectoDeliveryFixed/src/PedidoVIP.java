public class PedidoVIP extends Pedido {
    private static final int BONUS_VIP = 10;
    private int nivelUrgencia;

    public PedidoVIP(int id, String cliente, Restaurante restaurante, Zona zonaDestino, int prioridad, int nivelUrgencia) {
        super(id, cliente, restaurante, zonaDestino, prioridad, true);
        this.nivelUrgencia = nivelUrgencia;
    }

    public int getNivelUrgencia() { return nivelUrgencia; }
    public void setNivelUrgencia(int nivelUrgencia) { this.nivelUrgencia = nivelUrgencia; }

    @Override
    public int getPrioridad() {
        return super.getPrioridadBase() + (this.nivelUrgencia * BONUS_VIP);
    }

    @Override
    public boolean isVip() { return true; }
}
