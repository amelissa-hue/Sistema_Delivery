import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.time.LocalDateTime;

public class AppDelivery extends JFrame {
    private DashboardPanel dashboardPanel;
    private OrdersPanel ordersPanel;
    private CouriersPanel couriersPanel;
    private RoutePanel routePanel;
    private ControlPanel controlPanel;
    private PanelPilaAuditoria auditoriaPanel;
    private final DeliveryController controller;
    private static DeliveryService coreService;
    private static Zona zonaNorte;

    public static DeliveryService getCoreService() { return coreService; }
    public static Zona getZonaNorte() { return zonaNorte; }

    public AppDelivery(DeliveryController controller) {
        this.controller = controller;
        buildUI();
    }

    private void buildUI() {
        setTitle("Sistema Delivery");
        setSize(1400, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(new Color(45, 50, 60));

        dashboardPanel = new DashboardPanel();
        add(dashboardPanel, BorderLayout.NORTH);

        JPanel panelGrid = new JPanel(new GridLayout(1, 2, 8, 8));
        panelGrid.setBackground(new Color(45, 50, 60));

        ordersPanel  = new OrdersPanel();
        couriersPanel = new CouriersPanel();
        JPanel izq = new JPanel(new BorderLayout(5, 5));
        izq.setBackground(new Color(45, 50, 60));
        izq.add(ordersPanel,   BorderLayout.CENTER);
        izq.add(couriersPanel, BorderLayout.SOUTH);

        routePanel    = new RoutePanel();
        auditoriaPanel = new PanelPilaAuditoria();
        JPanel der = new JPanel(new BorderLayout(5, 5));
        der.setBackground(new Color(45, 50, 60));
        der.add(routePanel,    BorderLayout.CENTER);
        der.add(auditoriaPanel, BorderLayout.SOUTH);

        panelGrid.add(izq);
        panelGrid.add(der);
        add(panelGrid, BorderLayout.CENTER);

        controlPanel = new ControlPanel();
        controlPanel.bindController(controller);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public void refresh(Pedido[] pedidos, Repartidor[] rep, Restaurante[] top) {
        int ent = 0;
        for (Pedido p : coreService.getTodosLosPedidos())
            if (p != null && p.getEstado() == EstadoPedido.ENTREGADO) ent++;
        int libres = 0;
        for (Repartidor r : rep)
            if (r != null && r.isDisponible()) libres++;
        dashboardPanel.refresh(pedidos.length, ent, libres);
        ordersPanel.refresh(pedidos);
        couriersPanel.refresh(rep);
    }

    public RoutePanel        getRoutePanel()    { return routePanel; }
    public PanelPilaAuditoria getAuditoriaPanel() { return auditoriaPanel; }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.disabledText", Color.BLACK);
        UIManager.put("OptionPane.buttonFont", new Font("SansSerif", Font.PLAIN, 12));

        coreService = new DeliveryService();

        // ── Zonas ──────────────────────────────────────────────────────────────
        Zona centro      = new Zona(101, "Centro");
        zonaNorte        = new Zona(102, "Norte");
        Zona sur         = new Zona(103, "Sur");
        Zona residencial = new Zona(104, "Residencial");
        Zona oriental    = new Zona(105, "Oriental");

        coreService.addZone(centro);
        coreService.addZone(zonaNorte);
        coreService.addZone(sur);
        coreService.addZone(residencial);
        coreService.addZone(oriental);

        // Conexiones con tiempos en minutos (grafo bidireccional)
        coreService.connectZones(centro,      zonaNorte,   5);
        coreService.connectZones(centro,      sur,         10);
        coreService.connectZones(centro,      oriental,    7);
        coreService.connectZones(zonaNorte,   residencial, 4);
        coreService.connectZones(sur,         residencial, 3);
        coreService.connectZones(oriental,    residencial, 6);
        coreService.connectZones(zonaNorte,   oriental,    8);

        // ── Restaurantes con menú ──────────────────────────────────────────────
        Restaurante pizza = new Restaurante(1, "Pizza Roma", 4.8, centro);
        pizza.agregarProducto(new Producto("Pizza Margherita",  18000));
        pizza.agregarProducto(new Producto("Pizza Pepperoni",   21000));
        pizza.agregarProducto(new Producto("Pizza 4 Quesos",    23000));
        pizza.agregarProducto(new Producto("Calzone Jamón",     19000));
        pizza.agregarProducto(new Producto("Tiramisú",           8000));
        coreService.addRestaurante(pizza);

        Restaurante sushi = new Restaurante(2, "Sushi Master", 4.2, centro);
        sushi.agregarProducto(new Producto("Sushi Salmón x8",   25000));
        sushi.agregarProducto(new Producto("Rolls California x6",20000));
        sushi.agregarProducto(new Producto("Ramen Tonkotsu",    22000));
        sushi.agregarProducto(new Producto("Gyozas x6",         15000));
        sushi.agregarProducto(new Producto("Mochi Postre",       9000));
        coreService.addRestaurante(sushi);

        Restaurante burger = new Restaurante(3, "Burger House", 4.5, centro);
        burger.agregarProducto(new Producto("Burger Clásica",   16000));
        burger.agregarProducto(new Producto("Burger BBQ Doble", 22000));
        burger.agregarProducto(new Producto("Papas Medianas",    7000));
        burger.agregarProducto(new Producto("Papas Grandes",     9000));
        burger.agregarProducto(new Producto("Malteada Vainilla", 8000));
        coreService.addRestaurante(burger);

        // ── Repartidores ───────────────────────────────────────────────────────
        coreService.addRepartidor(new Repartidor(1, "Carlos Gomez", centro));
        coreService.addRepartidor(new Repartidor(2, "Maria Lopez",  zonaNorte));
        coreService.addRepartidor(new Repartidor(3, "Luis Torres",  sur));

        DeliveryController ctrl = new DeliveryController(coreService);
        SwingUtilities.invokeLater(() -> {
            AppDelivery f = new AppDelivery(ctrl);
            ctrl.bindFrame(f);
            f.setVisible(true);
        });
    }
}
