import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class PanelPilaAuditoria extends JPanel {
    private JList<String> list;
    private DefaultListModel<String> model;

    public PanelPilaAuditoria() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 160));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Pila de Auditoría LIFO (10)",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("SansSerif", Font.BOLD, 11)));
        model = new DefaultListModel<>();
        list = new JList<>(model);
        list.setFont(new Font("Monospaced", Font.PLAIN, 11));
        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    public void recargarPila(StackEstadoPedido pila) {
        model.clear();
        for (String log : pila.exportarComoArrayString()) model.addElement(log);
    }
}
