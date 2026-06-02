public class AristaGrafo {
    private final Zona destino;
    private final int minutos;
    private AristaGrafo siguiente;
    public AristaGrafo(Zona destino, int minutos) { this.destino = destino; this.minutos = minutos; this.siguiente = null; }
    public Zona getDestino() { return destino; }
    public int getMinutos() { return minutos; }
    public AristaGrafo getSiguiente() { return siguiente; }
    public void setSiguiente(AristaGrafo siguiente) { this.siguiente = siguiente; }
}
