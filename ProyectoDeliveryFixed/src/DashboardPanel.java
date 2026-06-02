import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class DashboardPanel extends JPanel {
    private final JLabel lblTotal, lblEntregados, lblLibres, lblTitulo;

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 35, 45));
        setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        lblTitulo = new JLabel("🚀  SISTEMA DELIVERY", JLabel.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitulo.setForeground(new Color(255, 220, 50));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel stats = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 6));
        stats.setBackground(new Color(30, 35, 45));

        lblTotal = makeLabel("📋 Pedidos Activos: 0", new Color(200, 200, 255));
        lblEntregados = makeLabel("✅ Entregados: 0", new Color(80, 220, 100));
        lblLibres = makeLabel("🚴 Repartidores Libres: 0", new Color(80, 180, 255));

        stats.add(lblTotal);
        stats.add(lblEntregados);
        stats.add(lblLibres);
        add(stats, BorderLayout.CENTER);
    }

    private JLabel makeLabel(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(color);
        return l;
    }

    public void refresh(int total, int entregados, int libres) {
        lblTotal.setText("📋 Pedidos Activos: " + total);
        lblEntregados.setText("✅ Entregados: " + entregados);
        lblLibres.setText("🚴 Repartidores Libres: " + libres);
    }
}
