public class NodoHash<K, V> {
    private final K key;
    private V value;
    private NodoHash<K, V> siguiente;

    public NodoHash(K key, V value) { this.key = key; this.value = value; this.siguiente = null; }
    public K getKey() { return key; }
    public V getValue() { return value; }
    public void setValue(V value) { this.value = value; }
    public NodoHash<K, V> getSiguiente() { return siguiente; }
    public void setSiguiente(NodoHash<K, V> siguiente) { this.siguiente = siguiente; }
}
