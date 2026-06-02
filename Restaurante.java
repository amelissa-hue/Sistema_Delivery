import java.util.ArrayList;
import java.util.List;

public class Restaurante {
    private final int id;
    private final String nombre;
    private double rating;
    private final Zona zona;
    private final List<Producto> menu;

    public Restaurante(int id, String nombre, double rating, Zona zona) {
        this.id = id;
        this.nombre = nombre;
        this.rating = rating;
        this.zona = zona;
        this.menu = new ArrayList<>();
    }

    public void agregarProducto(Producto p) { menu.add(p); }
    public List<Producto> getMenu() { return menu; }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getRating() { return rating; }
    public Zona getZona() { return zona; }
    public void setRating(double rating) { this.rating = rating; }

    @Override
    public String toString() {
        return nombre + " ★" + rating;
    }
}
