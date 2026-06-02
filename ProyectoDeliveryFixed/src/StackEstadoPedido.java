import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.time.LocalDateTime;

public class StackEstadoPedido {
    public static final int LIMITE_AUDITORIA = 10;
    private NodoT<EventoAuditoria> top;
    private int size;

    public StackEstadoPedido() { this.top = null; this.size = 0; }
    public void push(EventoAuditoria evento) {
        if (size == LIMITE_AUDITORIA) eliminarFondo();
        NodoT<EventoAuditoria> nuevo = new NodoT<>(evento);
        nuevo.setSiguiente(top);
        top = nuevo;
        size++;
    }
    public EventoAuditoria pop() {
        if (isEmpty()) return null;
        EventoAuditoria ev = top.getDato();
        top = top.getSiguiente();
        size--;
        return ev;
    }
    public EventoAuditoria peek() { return isEmpty() ? null : top.getDato(); }
    private void eliminarFondo() {
        if (top == null) return;
        if (top.getSiguiente() == null) { top = null; size = 0; return; }
        NodoT<EventoAuditoria> prev = null;
        NodoT<EventoAuditoria> curr = top;
        while (curr.getSiguiente() != null) { prev = curr; curr = curr.getSiguiente(); }
        if (prev != null) { prev.setSiguiente(null); size--; }
    }
    public boolean isEmpty() { return top == null; }
    public boolean isFull() { return size == LIMITE_AUDITORIA; }
    public int size() { return size; }
    public String[] exportarComoArrayString() {
        String[] arr = new String[size];
        NodoT<EventoAuditoria> aux = top;
        for (int i = 0; i < size; i++, aux = aux.getSiguiente()) arr[i] = aux.getDato().toString();
        return arr;
    } 
     
}