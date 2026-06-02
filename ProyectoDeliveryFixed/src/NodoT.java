public class NodoT<T> {
    private T dato;
    private NodoT<T> siguiente;

    public NodoT(T dato) { this.dato = dato; this.siguiente = null; }
    public T getDato() { return dato; }
    public void setDato(T dato) { this.dato = dato; }
    public NodoT<T> getSiguiente() { return siguiente; }
    public void setSiguiente(NodoT<T> siguiente) { this.siguiente = siguiente; }
}
