import java.util.Stack;

public class Ventas {
    // Para guardar productos vendidos y deshacer en caso de errores
    private Stack<Producto> historial;

    public Ventas() {
        this.historial = new Stack<>();
    }

    public void realizarVenta(Producto producto, int cantidad) {
        if (producto.getStockActual() >= cantidad) {
            producto.vender(cantidad);
            historial.push(producto); // Guardamos el producto en la pila
            System.out.println("Venta realizada: " + producto.getNombre());
        } else {
            System.out.println("No hay suficiente stock.");
        }
    }

    public void deshacerVenta() {
        if (!historial.isEmpty()) {
            Producto ultimo = historial.pop(); // Deshacemos el último movimiento de la pila
            ultimo.setStockActual(ultimo.getStockActual() + 1); // Devolvemos producto al stock
            System.out.println("Se deshizo la venta de: " + ultimo.getNombre());
        } else {
            System.out.println("No hay ventas que deshacer.");
        }
    }
}