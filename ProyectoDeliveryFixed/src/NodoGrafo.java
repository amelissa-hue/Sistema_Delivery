public class NodoGrafo {
    private final Zona zona;
    private AristaGrafo primeraArista;
    private NodoGrafo siguiente;

    public NodoGrafo(Zona zona) { this.zona = zona; this.primeraArista = null; this.siguiente = null; }
    public Zona getZona() { return zona; }
    public AristaGrafo getPrimeraArista() { return primeraArista; }
    public NodoGrafo getSiguiente() { return siguiente; }
    public void setSiguiente(NodoGrafo siguiente) { this.siguiente = siguiente; }
    public void agregarArista(Zona destino, int minutos) {
        AristaGrafo nueva = new AristaGrafo(destino, minutos);
        nueva.setSiguiente(primeraArista);
        primeraArista = nueva;
    }
}
