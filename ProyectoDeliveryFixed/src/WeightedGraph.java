import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.time.LocalDateTime;

public class WeightedGraph {

    public static final int INFINITO = 999999;
    private NodoGrafo cabeza;
    private int totalZonas;

    public WeightedGraph() {
        this.cabeza = null;
        this.totalZonas = 0;
    }

    public void addZone(Zona z) {
        if (containsZone(z)) return;
        NodoGrafo nuevo = new NodoGrafo(z);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoGrafo aux = cabeza;
            while (aux.getSiguiente() != null) {
                aux = aux.getSiguiente();
            }
            aux.setSiguiente(nuevo);
        }
        totalZonas++;
    }

    public void addEdge(Zona a, Zona b, int min) {
        NodoGrafo nA = findNodo(a);
        NodoGrafo nB = findNodo(b);
        if (nA != null && nB != null) {
            nA.agregarArista(b, min);
            nB.agregarArista(a, min);
        }
    }

    public AristaGrafo getNeighbors(Zona z) {
        NodoGrafo n = findNodo(z);
        return n != null ? n.getPrimeraArista() : null;
    }

    public int getWeight(Zona a, Zona b) {
        NodoGrafo n = findNodo(a);
        if (n == null) return INFINITO;
        for (AristaGrafo e = n.getPrimeraArista(); e != null; e = e.getSiguiente()) {
            if (e.getDestino().getId() == b.getId()) return e.getMinutos();
        }
        return INFINITO;
    }

    public boolean containsZone(Zona z) {
        return findNodo(z) != null;
    }

    private NodoGrafo findNodo(Zona z) {
        for (NodoGrafo aux = cabeza; aux != null; aux = aux.getSiguiente()) {
            if (aux.getZona().getId() == z.getId()) return aux;
        }
        return null;
    }

    public Zona[] getZonasAsArray() {
        Zona[] arr = new Zona[totalZonas];
        int i = 0;
        for (NodoGrafo aux = cabeza; aux != null; aux = aux.getSiguiente()) {
            arr[i++] = aux.getZona();
        }
        return arr;
    }

    public int size() {
        return totalZonas;
    }
}