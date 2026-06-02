public class QueuePedido {
    private NodoT<Pedido> frente, fin;
    private int size;

    public QueuePedido() { this.frente = null; this.fin = null; this.size = 0; }

    public void enqueue(Pedido pedido) {
        NodoT<Pedido> nuevo = new NodoT<>(pedido);
        if (isEmpty()) { frente = nuevo; fin = nuevo; }
        else { fin.setSiguiente(nuevo); fin = nuevo; }
        size++;
    }

    public Pedido dequeue() {
        if (isEmpty()) return null;
        Pedido pedido = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) fin = null;
        size--;
        return pedido;
    }

    public Pedido peek() { return isEmpty() ? null : frente.getDato(); }
    public boolean isEmpty() { return frente == null; }
    public int size() { return size; }
}
