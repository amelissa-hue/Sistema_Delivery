import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.time.LocalDateTime;

public class LinkedListRepartidor {
   
    private NodoT<Repartidor> cabeza;
    private int size;

    public LinkedListRepartidor() {
        this.cabeza = null;
        this.size = 0;
    }

    public void add(Repartidor repartidor) {
        NodoT<Repartidor> nuevo = new NodoT<>(repartidor);

        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoT<Repartidor> aux = cabeza;
            while (aux.getSiguiente() != null) {
                aux = aux.getSiguiente();
            }
            aux.setSiguiente(nuevo);
        }

        size++;
    }

    public Repartidor findAvailable() {
        NodoT<Repartidor> aux = cabeza;

        while (aux != null) {
            if (aux.getDato().isDisponible()) {
                return aux.getDato();
            }
            aux = aux.getSiguiente();
        }

        return null;
    }

    public Repartidor findAvailableRandom() {
        Repartidor[] disponibles = new Repartidor[size];
        NodoT<Repartidor> aux = cabeza;
        int count = 0;

        while (aux != null) {
            if (aux.getDato().isDisponible()) {
                disponibles[count++] = aux.getDato();
            }
            aux = aux.getSiguiente();
        }

        if (count == 0) return null;
        return disponibles[new Random().nextInt(count)];
    }

    public Repartidor getById(int id) {
        NodoT<Repartidor> aux = cabeza;

        while (aux != null) {
            if (aux.getDato().getId() == id) {
                return aux.getDato();
            }
            aux = aux.getSiguiente();
        }

        return null;
    }

    public Repartidor[] toArray() {
        Repartidor[] arreglo = new Repartidor[size];

        NodoT<Repartidor> aux = cabeza;
        int i = 0;

        while (aux != null) {
            arreglo[i] = aux.getDato();
            aux = aux.getSiguiente();
            i++;
        }

        return arreglo;
    }

    public boolean isEmpty() {
        return cabeza == null;
    }

    public int size() {
        return size;
    }
}
