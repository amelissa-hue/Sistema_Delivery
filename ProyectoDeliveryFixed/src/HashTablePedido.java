import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.time.LocalDateTime;

public class HashTablePedido {
  private static final int CAPACIDAD_PRIMA = 23;
    @SuppressWarnings("unchecked")
    private final  NodoHash<Integer, Pedido>[] buckets = new NodoHash[CAPACIDAD_PRIMA];
    private int size;

    private int hash(int key) { return Math.abs(key % CAPACIDAD_PRIMA); }
    public void put(int key, Pedido value) {
        int idx = hash(key);
        NodoHash<Integer, Pedido> actual = buckets[idx];
        while (actual != null) {
            if (actual.getKey().equals(key)) { actual.setValue(value); return; }
            actual = actual.getSiguiente();
        }
        NodoHash<Integer, Pedido> nuevo = new NodoHash<>(key, value);
        nuevo.setSiguiente(buckets[idx]);
        buckets[idx] = nuevo;
        size++;
    }
    public Pedido get(int key) {
        int idx = hash(key);
        for (NodoHash<Integer, Pedido> actual = buckets[idx]; actual != null; actual = actual.getSiguiente()) {
            if (actual.getKey().equals(key)) return actual.getValue();
        }
        return null;
    }
    public boolean remove(int key) {
        int idx = hash(key);
        NodoHash<Integer, Pedido> actual = buckets[idx], anterior = null;
        while (actual != null) {
            if (actual.getKey().equals(key)) {
                if (anterior == null) buckets[idx] = actual.getSiguiente();
                else anterior.setSiguiente(actual.getSiguiente());
                size--;
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        return false;
    }
    public Pedido[] getValores() {
        Pedido[] arr = new Pedido[size];
        int i = 0;
        for (int b = 0; b < CAPACIDAD_PRIMA; b++) {
            for (NodoHash<Integer, Pedido> actual = buckets[b]; actual != null; actual = actual.getSiguiente()) {
                arr[i++] = actual.getValue();
            }
        }
        return arr;
    }
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }   
}