public class NodoBST<T> {
    private T dato;
    private NodoBST<T> izquierdo, derecho;

    public NodoBST(T dato) { this.dato = dato; this.izquierdo = null; this.derecho = null; }
    public T getDato() { return dato; }
    public void setDato(T dato) { this.dato = dato; }
    public NodoBST<T> getIzquierdo() { return izquierdo; }
    public void setIzquierdo(NodoBST<T> izquierdo) { this.izquierdo = izquierdo; }
    public NodoBST<T> getDerecho() { return derecho; }
    public void setDerecho(NodoBST<T> derecho) { this.derecho = derecho; }
}
