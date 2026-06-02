import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class CouriersPanel extends JPanel {
    private JTable tabla;
    private String[] cols = {"Repartidor ID", "Nombre", "Zona Actual", "Estado"};
    private Object[][] datos = new Object[0][4];

    public CouriersPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 150));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
            "Flota de Distribución", TitledBorder.LEFT, TitledBorder.TOP,
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
    }

    public void refresh(Repartidor[] rep) {
        datos = new Object[rep.length][4];
        for (int i = 0; i < rep.length; i++) if (rep[i] != null) {
            datos[i] = new Object[]{
                rep[i].getId(), rep[i].getNombre(),
                rep[i].getZonaActual().getNombre(),
                rep[i].isDisponible() ? "LIBRE" : "OCUPADO"
            };
        }
        setModel();
    }
}
