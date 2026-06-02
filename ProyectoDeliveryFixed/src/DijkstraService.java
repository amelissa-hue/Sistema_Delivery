import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.time.LocalDateTime;

public class DijkstraService {
 private final WeightedGraph graph;
    public DijkstraService(WeightedGraph graph) { this.graph = graph; }
    
    public Zona[] shortestPath(Zona origen, Zona destino) {
        Zona[] zonas = graph.getZonasAsArray();
        int total = zonas.length, s = idx(zonas, origen), t = idx(zonas, destino);
        if (s == -1 || t == -1) return new Zona[0];
        int[] dist = new int[total], prev = new int[total];
        boolean[] vis = new boolean[total];
        for (int i = 0; i < total; i++) { dist[i] = WeightedGraph.INFINITO; prev[i] = -1; vis[i] = false; }
        dist[s] = 0;
        for (int i = 0; i < total - 1; i++) {
            int u = minDist(dist, vis);
            if (u == -1) break;
            vis[u] = true;
            for (AristaGrafo e = graph.getNeighbors(zonas[u]); e != null; e = e.getSiguiente()) {
                int v = idx(zonas, e.getDestino());
                if (v != -1 && !vis[v] && dist[u] + e.getMinutos() < dist[v]) {
                    dist[v] = dist[u] + e.getMinutos();
                    prev[v] = u;
                }
            }
        }
        return buildPath(prev, zonas, t);
    }
    public int getTotalMinutes(Zona o, Zona d) {
        Zona[] z = graph.getZonasAsArray();
        int s = idx(z, o), t = idx(z, d);
        if (s == -1 || t == -1) return WeightedGraph.INFINITO;
        int[] dist = new int[z.length]; boolean[] vis = new boolean[z.length];
        for (int i = 0; i < z.length; i++) { dist[i] = WeightedGraph.INFINITO; vis[i] = false; }
        dist[s] = 0;
        for (int i = 0; i < z.length - 1; i++) {
            int u = minDist(dist, vis);
            if (u == -1) break;
            vis[u] = true;
            for (AristaGrafo e = graph.getNeighbors(z[u]); e != null; e = e.getSiguiente()) {
                int v = idx(z, e.getDestino());
                if (v != -1 && !vis[v] && dist[u] + e.getMinutos() < dist[v]) dist[v] = dist[u] + e.getMinutos();
            }
        }
        return dist[t];
    }
    private int minDist(int[] d, boolean[] v) {
        int min = WeightedGraph.INFINITO, mi = -1;
        for (int i = 0; i < d.length; i++) if (!v[i] && d[i] <= min) { min = d[i]; mi = i; }
        return mi;
    }
    private Zona[] buildPath(int[] prev, Zona[] z, int t) {
        int c = 0, tmp = t;
        while (tmp != -1) { c++; tmp = prev[tmp]; }
        Zona[] p = new Zona[c]; tmp = t;
        for (int i = c - 1; i >= 0; i--) { p[i] = z[tmp]; tmp = prev[tmp]; }
        return p;
    }
    private int idx(Zona[] z, Zona x) {
        for (int i = 0; i < z.length; i++) if (z[i].getId() == x.getId()) return i;
        return -1;
    } 
}