import java.util.*;
import java.awt.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.time.LocalDateTime;

public class DeliveryController {
    private final DeliveryService service;
    private final ReporteGenerator reportGenerator;
    private AppDelivery frame;

    public DeliveryController(DeliveryService service) {
        this.service = service;
        this.reportGenerator = new ReporteGenerator(service);
    }

    public void bindFrame(AppDelivery frame) { this.frame = frame; refreshViews(); }

    public void onNewOrder(String cliente, int restId, Zona destino, boolean vip, int prioridad, java.util.List<Producto> productos) {
        int id = service.generarId();
        Restaurante rest = null;
        for (Restaurante r : service.getTopRestaurantes(99))
            if (r.getId() == restId) { rest = r; break; }
        if (rest == null) {
            JOptionPane.showMessageDialog(frame, "Error: restaurante no encontrado.");
            return;
        }
        Pedido p = vip ? new PedidoVIP(id, cliente, rest, destino, prioridad, 2)
                       : new Pedido(id, cliente, rest, destino, prioridad);
        for (Producto prod : productos) p.agregarProducto(prod);
        service.newOrder(p);
        refreshViews();
    }

    public void onSimulate() {
        StringBuilder gps = new StringBuilder();
        Pedido p;
        try {
            p = service.simulate(gps);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Sin repartidores disponibles", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (p != null) {
            frame.getRoutePanel().showRoute(gps.toString());
            refreshViews();
            String detalle = gps.toString()
                + "\n\nProductos: " + p.getResumenProductos()
                + "\nTotal: $" + String.format("%.2f", p.getTotalProductos());
            int ok = JOptionPane.showConfirmDialog(frame,
                "Pedido #" + p.getId() + " — " + p.getCliente() + "\n" + detalle + "\n\n¿Marcar como entregado?",
                "Despacho en curso", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                service.marcarPedidoEntregado(p);
            } else {
                // Al presionar NO: liberar el repartidor para que vuelva a estar disponible
                if (p.getRepartidorAsignado() != null) {
                    p.getRepartidorAsignado().liberarPedido(false);
                }
                // Regresar el pedido a la cola (re-insertar)
                service.devolverPedidoACola(p);
            }
            refreshViews();
        } else {
            JOptionPane.showMessageDialog(frame, "No hay pedidos pendientes en cola.");
        }
    }

    public void onGenerateReport() {
        String path = "Reporte_SistemaDelivery.txt";
        reportGenerator.generateDailyReport(path);

        // Mostrar aviso y abrir diálogo de impresión por cada taquilla (pedido)
        Pedido[] todos = service.getTodosLosPedidos();
        int count = 0;
        for (Pedido p : todos) if (p != null) count++;

        if (count == 0) {
            JOptionPane.showMessageDialog(frame, "No hay pedidos para imprimir.\nReporte guardado en: " + path);
            return;
        }

        int resp = JOptionPane.showConfirmDialog(frame,
            "Reporte guardado en: " + path + "\n\n¿Desea imprimir el ticket de cada pedido (" + count + " en total)?",
            "Exportar e Imprimir", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (resp == JOptionPane.YES_OPTION) {
            for (Pedido p : todos) {
                if (p == null) continue;
                imprimirTaquilla(p);
            }
        }
    }

    /** Abre el diálogo de impresión del sistema para un pedido individual (taquilla) */
    private void imprimirTaquilla(Pedido p) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Taquilla Pedido #" + p.getId());

        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

            Graphics2D g2 = (Graphics2D) graphics;
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            Font fontTitulo = new Font("Monospaced", Font.BOLD, 14);
            Font fontNormal = new Font("Monospaced", Font.PLAIN, 11);

            int y = 20;
            g2.setFont(fontTitulo);
            g2.drawString("===== SISTEMA DELIVERY =====", 10, y); y += 20;
            g2.drawString("     TAQUILLA / TICKET", 10, y);       y += 20;
            g2.drawString("============================", 10, y);  y += 20;

            g2.setFont(fontNormal);
            g2.drawString("Pedido #: " + p.getId(),                           10, y); y += 18;
            g2.drawString("Cliente : " + p.getCliente(),                      10, y); y += 18;
            g2.drawString("Restaurante: " + p.getRestaurante().getNombre(),   10, y); y += 18;
            g2.drawString("Destino : " + p.getZonaDestino().getNombre(),      10, y); y += 18;
            g2.drawString("VIP     : " + (p.isVip() ? "Sí" : "No"),          10, y); y += 18;
            g2.drawString("Estado  : " + p.getEstado(),                       10, y); y += 18;
            g2.drawString("----------------------------",                      10, y); y += 18;
            g2.drawString("Productos:",                                        10, y); y += 18;
            for (String prod : p.getResumenProductos().split(",")) {
                g2.drawString("  · " + prod.trim(),                           10, y); y += 16;
            }
            g2.drawString("----------------------------",                      10, y); y += 18;
            g2.setFont(fontTitulo);
            g2.drawString("TOTAL: $" + String.format("%.2f", p.getTotalProductos()), 10, y); y += 20;
            g2.setFont(fontNormal);
            g2.drawString("============================",                      10, y);

            return Printable.PAGE_EXISTS;
        });

        // Mostrar el diálogo de impresión del sistema operativo
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(frame, "Error al imprimir pedido #" + p.getId() + ":\n" + ex.getMessage());
            }
        }
    }

    public void refreshViews() {
        frame.refresh(service.getActiveOrders(), service.getAllCouriers(), service.getTopRestaurantes(5));
        frame.getAuditoriaPanel().recargarPila(service.getAuditoria());
    }
}
