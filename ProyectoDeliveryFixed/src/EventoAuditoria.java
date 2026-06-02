import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class EventoAuditoria {
    private final int idPedido;
    private final EstadoPedido estado;
    private final LocalDateTime momento;
    public EventoAuditoria(int idPedido, EstadoPedido estado, LocalDateTime momento) {
        this.idPedido = idPedido; this.estado = estado; this.momento = momento;
    }
    public int getIdPedido() { return idPedido; }
    public EstadoPedido getEstado() { return estado; }
    public LocalDateTime getMomento() { return momento; }
    @Override
    public String toString() {
        return "Pedido#" + idPedido + " ➔ " + estado + " @ " + momento.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}