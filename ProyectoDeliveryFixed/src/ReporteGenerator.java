import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ReporteGenerator {
    private final DeliveryService service;

    public ReporteGenerator(DeliveryService service) {
        this.service = service;
    }

    public void generateDailyReport(String fileName) {
        Pedido[] todos = service.getTodosLosPedidos();
        Pedido[] ordenados = selectionSortByTimestamp(todos);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("========================================================================\n");
            bw.write("         INFORME DE ENTREGAS — SISTEMA DELIVERY\n");
            bw.write("========================================================================\n\n");
            int total = 0, entregados = 0, vips = 0;
            for (Pedido x : ordenados) {
                if (x == null) continue;
                total++;
                if (x.getEstado() == EstadoPedido.ENTREGADO) entregados++;
                if (x.isVip()) vips++;
                bw.write(String.format(
                    "ID: %-5d | Cliente: %-15s | Restaurante: %-14s | VIP: %-5b | Estado: %-14s | Total: $%-8.2f | Productos: %s\n",
                    x.getId(), x.getCliente(), x.getRestaurante().getNombre(),
                    x.isVip(), x.getEstado(),
                    x.getTotalProductos(), x.getResumenProductos()
                ));
            }
            bw.write("\n========================================================================\n");
            bw.write(String.format("  Total pedidos: %d  |  Entregados: %d  |  VIP: %d\n", total, entregados, vips));
            bw.write("========================================================================\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pedido[] selectionSortByTimestamp(Pedido[] arr) {
        int n = arr.length;
        Pedido[] c = new Pedido[n];
        System.arraycopy(arr, 0, c, 0, n);
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (c[j] != null && c[min] != null &&
                    c[j].getTimestamp().isBefore(c[min].getTimestamp())) min = j;
            }
            Pedido tmp = c[min]; c[min] = c[i]; c[i] = tmp;
        }
        return c;
    }
}
