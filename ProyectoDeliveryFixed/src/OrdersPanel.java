import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class OrdersPanel extends JPanel {
    private JTable tabla;
    private String[] cols = {"ID", "Cliente", "Restaurante", "Productos", "Destino", "Total", "VIP", "Estado"};
    private Object[][] datos = new Object[0][8];

    public OrdersPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Cola de Despachos Activos",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 12)));
        tabla = new JTable();
        tabla.setRowHeight(22);
        tabla.setEnabled(false);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        setModel();
    }

    private void setModel() {
        tabla.setModel(new javax.swing.table.AbstractTableModel() {
            public int getRowCount() { return datos.length; }
            public int getColumnCount() { return cols.length; }
            public Object getValueAt(int r, int c) { return datos[r][c]; }
            public String getColumnName(int c) { return cols[c]; }
        });
        // Ancho de columna Productos
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
    }

    public void refresh(Pedido[] pedidos) {
        datos = new Object[pedidos.length][8];
        for (int i = 0; i < pedidos.length; i++) {
            if (pedidos[i] != null) {
                datos[i] = new Object[]{
                    pedidos[i].getId(),
                    pedidos[i].getCliente(),
                    pedidos[i].getRestaurante().getNombre(),
                    pedidos[i].getResumenProductos(),
                    pedidos[i].getZonaDestino().getNombre(),
                    "$" + String.format("%.2f", pedidos[i].getTotalProductos()),
                    pedidos[i].isVip() ? "★ VIP" : "Normal",
                    pedidos[i].getEstado()
                };
            }
        }
        setModel();
    }
}
