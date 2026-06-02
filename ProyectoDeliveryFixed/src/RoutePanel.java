import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class RoutePanel extends JPanel {
    private JTextArea area;

    public RoutePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Geolocalización por Dijkstra GPS — Ruta Más Corta",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12)));

        area = new JTextArea();
        area.setBackground(new Color(15, 20, 25));
        area.setForeground(new Color(50, 220, 50));
        area.setCaretColor(new Color(50, 220, 50));
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setMargin(new Insets(8, 10, 8, 10));

        // Mensaje inicial
        area.setText(
            "╔══════════════════════════════════════╗\n" +
            "║       GPS DIJKSTRA — EN ESPERA       ║\n" +
            "╚══════════════════════════════════════╝\n\n" +
            "  Registra un pedido y presiona\n" +
            "  'Simular Despacho' para calcular\n" +
            "  la ruta más corta automáticamente.\n\n" +
            "  El algoritmo de Dijkstra calculará:\n" +
            "  • Tramo 1: Repartidor → Restaurante\n" +
            "  • Tramo 2: Restaurante → Destino\n" +
            "  • Tiempo total estimado en minutos\n"
        );

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    public void showRoute(String msg) {
        area.setText(msg);
        area.setCaretPosition(0);
    }

    public void clear() { area.setText(""); }
}
