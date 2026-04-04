import java.util.Stack;
import java.time.LocalDate;
import java.util.ArrayList;

public class Ventas {
    // Pila para recordar qué producto se vendió
    private Stack<Producto> historialProductos;
    // Pila paralela para recordar cuántas unidades se vendieron en esa transacción
    private Stack<Integer> historialCantidades;
    //Historial de ventas con fecha
    private ArrayList<RegistroVenta> historialVentas;

    public Ventas() {
        this.historialProductos = new Stack<>();
        this.historialCantidades = new Stack<>();
        this.historialVentas = new ArrayList<>();
    }
    //Clase interna 
    public static class RegistroVenta {
        private LocalDate fecha;
        private int cantidad;
        private Producto producto;

        public RegistroVenta(LocalDate fecha, int cantidad, Producto producto) {
            this.fecha = fecha;
            this.cantidad = cantidad;
            this.producto = producto;
        }
        //getters
        public LocalDate getFecha() { return fecha; }
        public int getCantidad() { return cantidad; }
        public Producto getProducto() { return producto; }
    }

    public void realizarVenta(Producto productoParaVender, int cantidadSolicitada, LocalDate fecha) {
        if (productoParaVender.getStockActual() >= cantidadSolicitada) {
            // 1. Reducir el stock físicamente en el objeto producto
            productoParaVender.vender(cantidadSolicitada);
            
            // 2. Guardar en las pilas para permitir el "Deshacer"
            historialProductos.push(productoParaVender);
            historialCantidades.push(cantidadSolicitada);

            // Agregar datos de venta al arreglo de historialVentas
            historialVentas.add(new RegistroVenta(fecha, cantidadSolicitada, productoParaVender));
            
            System.out.println("✅ Venta registrada: " + cantidadSolicitada + " unidad(es) de " + productoParaVender.getNombre());
        } else {
            System.out.println("❌ Error: Stock insuficiente para " + productoParaVender.getNombre());
        }
    }

    public void deshacerVenta() {
        if (!historialProductos.isEmpty() && !historialCantidades.isEmpty()) {
            // Sacamos el último producto y la última cantidad vendida de las pilas
            Producto productoARestaurar = historialProductos.pop();
            int cantidadADevolver = historialCantidades.pop();
            
            // Devolvemos exactamente la cantidad que se había vendido
            int stockAnterior = productoARestaurar.getStockActual();
            productoARestaurar.setStockActual(stockAnterior + cantidadADevolver);
            
            System.out.println("🔄 Deshacer exitoso: Se devolvieron " + cantidadADevolver + 
                               " unidades a " + productoARestaurar.getNombre());
            System.out.println("   Nuevo stock: " + productoARestaurar.getStockActual());
        } else {
            System.out.println("ℹ️ No hay ventas recientes para deshacer.");
        }
    }
    //Getter de la clase para Prediccion Demanda 
    public ArrayList<RegistroVenta> getHistorialVentas() {
        return historialVentas;
    }
}
