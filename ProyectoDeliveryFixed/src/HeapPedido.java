import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.time.LocalDateTime;

public class HeapPedido {
  private static final int CAPACIDAD_INICIAL = 16;
    private Pedido[] heap;
    private int size, capacity;

    public HeapPedido() {
        this.capacity = CAPACIDAD_INICIAL;
        this.heap = new Pedido[capacity];
        this.size = 0;
    }
    public void insert(Pedido pedido) {
        if (size == capacity) resize();
        heap[size] = pedido;
        size++;
        heapifyUp(size - 1);
    }
    public Pedido extractMax() {
        if (isEmpty()) return null;
        Pedido max = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        heapifyDown(0);
        return max;
    }
    public Pedido peek() { return isEmpty() ? null : heap[0]; }
    private void heapifyUp(int index) {
        while (index > 0) {
            int padre = (index - 1) / 2;
            if (heap[index].getPrioridad() > heap[padre].getPrioridad()) {
                swap(index, padre);
                index = padre;
            } else break;
        }
    }
    private void heapifyDown(int index) {
        while (true) {
            int mayor = index, izq = 2 * index + 1, der = 2 * index + 2;
            if (izq < size && heap[izq].getPrioridad() > heap[mayor].getPrioridad()) mayor = izq;
            if (der < size && heap[der].getPrioridad() > heap[mayor].getPrioridad()) mayor = der;
            if (mayor == index) break;
            swap(index, mayor);
            index = mayor;
        }
    }
    private void swap(int i, int j) {
        Pedido temp = heap[i]; heap[i] = heap[j]; heap[j] = temp;
    }
    private void resize() {
        capacity *= 2;
        
        Pedido[] nuevo = new Pedido[capacity];
        System.arraycopy(heap, 0, nuevo, 0, size);
        heap = nuevo;
    }
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }   
}