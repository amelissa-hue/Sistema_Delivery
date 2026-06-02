import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.time.LocalDateTime;

public class BSTRestaurante {
  private NodoBST<Restaurante> raiz;
    private int size; 
    public BSTRestaurante() { this.raiz = null; this.size = 0; }
    public void insert(Restaurante r) { raiz = insertRec(raiz, r); size++; }
    private NodoBST<Restaurante> insertRec(NodoBST<Restaurante> nodo, Restaurante r) {
        if (nodo == null) return new NodoBST<>(r);
        if (r.getRating() < nodo.getDato().getRating()) nodo.setIzquierdo(insertRec(nodo.getIzquierdo(), r));
        else nodo.setDerecho(insertRec(nodo.getDerecho(), r));
        return nodo;
    }
    public Restaurante[] inOrderDesc() {
        Restaurante[] r = new Restaurante[size];
        int[] idx = {0};
        inOrderDescRec(raiz, r, idx);
        return r;
    }
    private void inOrderDescRec(NodoBST<Restaurante> nodo, Restaurante[] res, int[] idx) {
        if (nodo != null) {
            inOrderDescRec(nodo.getDerecho(), res, idx);
            res[idx[0]++] = nodo.getDato();
            inOrderDescRec(nodo.getIzquierdo(), res, idx);
        }
    }
    public Restaurante[] topK(int k) {
        Restaurante[] completo = inOrderDesc();
        int limit = Math.min(k, size);
        Restaurante[] out = new Restaurante[limit];
        System.arraycopy(completo, 0, out, 0, limit);
        return out;
    }
    public int size() { return size; }
}