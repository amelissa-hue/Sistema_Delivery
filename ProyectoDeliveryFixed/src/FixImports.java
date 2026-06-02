import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FixImports {
    public static void main(String[] args) throws Exception {
        String imports = "import java.util.*;\nimport java.awt.*;\nimport javax.swing.*;\nimport javax.swing.border.*;\nimport java.io.*;\n\n";
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".java") && !name.equals("FixImports.java"));
        if (files != null) {
            for (File f : files) {
                String content = new String(Files.readAllBytes(f.toPath()));
                content = content.replace("ReportGenerator", "ReporteGenerator");
                if (!content.startsWith("import")) {
                    Files.write(f.toPath(), (imports + content).getBytes());
                }
            }
        }
        System.out.println("Arreglo completado");
    }
}
