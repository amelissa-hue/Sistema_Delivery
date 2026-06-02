import java.awt.*;
import javax.swing.*;

public class ControlPanel extends JPanel {
    private JButton btnNuevo, btnSim, btnRep;
    private DeliveryController controller;

    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        setBorder(BorderFactory.createTitledBorder("Herramientas de Simulacion"));

        btnNuevo = new JButton("Registrar Pedido");
        styleButton(btnNuevo);

        btnSim = new JButton("<html><b>Simular Despacho -></b></html>");
        btnSim.setBackground(new Color(40, 167, 69));
        styleButton(btnSim);

        btnRep = new JButton("<html><b>Exportar Reporte (.txt)</b></html>");
        btnRep.setBackground(new Color(0, 123, 255));
        styleButton(btnRep);

        add(btnNuevo);
        add(btnSim);
        add(btnRep);

        btnNuevo.addActionListener(e -> dispatchForm());
        btnSim.addActionListener(e -> { if (controller != null) controller.onSimulate(); });
        btnRep.addActionListener(e -> { if (controller != null) controller.onGenerateReport(); });
    }

    public void bindController(DeliveryController c) { this.controller = c; }

    private void styleButton(JButton button) {
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setOpaque(true);
    }

    private Component dialogParent() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        return owner != null ? owner : this;
    }

    private void dispatchForm() {
        JTextField txtCliente = new JTextField(20);
        JPanel pCliente = new JPanel(new BorderLayout(5, 5));
        pCliente.add(new JLabel("Nombre del Cliente:"), BorderLayout.NORTH);
        pCliente.add(txtCliente, BorderLayout.CENTER);

        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dlgCliente = new JDialog(owner, "Registrar Pedido", Dialog.ModalityType.APPLICATION_MODAL);
        JButton okBtn = new JButton("Aceptar");
        JButton cancelBtn = new JButton("Cancelar");
        styleButton(okBtn);
        styleButton(cancelBtn);
        final boolean[] aceptado = {false};

        okBtn.addActionListener(ev -> { aceptado[0] = true; dlgCliente.dispose(); });
        cancelBtn.addActionListener(ev -> dlgCliente.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        contenido.add(pCliente, BorderLayout.CENTER);
        contenido.add(btnPanel, BorderLayout.SOUTH);

        dlgCliente.setContentPane(contenido);
        dlgCliente.pack();
        dlgCliente.setLocationRelativeTo(dialogParent());
        dlgCliente.setVisible(true);

        if (!aceptado[0] || txtCliente.getText().trim().isEmpty()) return;
        String cli = txtCliente.getText().trim();

        Restaurante[] restaurantes = AppDelivery.getCoreService().getTopRestaurantes(99);
        if (restaurantes.length == 0) {
            JOptionPane.showMessageDialog(dialogParent(), "No hay restaurantes disponibles.");
            return;
        }
        Restaurante restElegido = (Restaurante) JOptionPane.showInputDialog(
            dialogParent(), "Selecciona el restaurante:", "Restaurante",
            JOptionPane.QUESTION_MESSAGE, null, restaurantes, restaurantes[0]);
        if (restElegido == null) return;

        Zona[] zonas = AppDelivery.getCoreService().getGrafo().getZonasAsArray();
        Zona zonaElegida = (Zona) JOptionPane.showInputDialog(
            dialogParent(), "Selecciona la zona de entrega:", "Zona Destino",
            JOptionPane.QUESTION_MESSAGE, null, zonas, zonas[0]);
        if (zonaElegida == null) return;

        java.util.List<Producto> menu = restElegido.getMenu();
        if (menu.size() < 2) {
            JOptionPane.showMessageDialog(dialogParent(), "El restaurante no tiene suficientes productos.");
            return;
        }
        Producto[] menuArr = menu.toArray(new Producto[0]);
        JPanel panelProductos = new JPanel(new GridLayout(menuArr.length, 1, 4, 4));
        panelProductos.setBorder(BorderFactory.createTitledBorder("Selecciona al menos 2 productos:"));
        JCheckBox[] checks = new JCheckBox[menuArr.length];
        for (int i = 0; i < menuArr.length; i++) {
            checks[i] = new JCheckBox(menuArr[i].toString());
            panelProductos.add(checks[i]);
        }
        while (true) {
            int res = JOptionPane.showConfirmDialog(dialogParent(), panelProductos, "Menu de " + restElegido.getNombre(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) return;
            int seleccionados = 0;
            for (JCheckBox cb : checks) if (cb.isSelected()) seleccionados++;
            if (seleccionados >= 2) break;
            JOptionPane.showMessageDialog(dialogParent(), "Debes seleccionar al menos 2 productos.", "Atencion", JOptionPane.WARNING_MESSAGE);
        }

        int vip = JOptionPane.showConfirmDialog(dialogParent(), "Es un pedido prioritario / VIP?", "Categoria", JOptionPane.YES_NO_OPTION);
        boolean esVip = (vip == JOptionPane.YES_OPTION);
        int prio = esVip ? 5 : 1;

        java.util.List<Producto> elegidos = new java.util.ArrayList<>();
        for (int i = 0; i < menuArr.length; i++)
            if (checks[i].isSelected()) elegidos.add(menuArr[i]);

        if (controller != null)
            controller.onNewOrder(cli, restElegido.getId(), zonaElegida, esVip, prio, elegidos);
    }
}
