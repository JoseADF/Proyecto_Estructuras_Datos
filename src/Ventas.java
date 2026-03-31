import java.util.Stack;

public class Ventas {
    // Pila para recordar qué producto se vendió
    private Stack<Producto> historialProductos;
    // Pila paralela para recordar cuántas unidades se vendieron en esa transacción
    private Stack<Integer> historialCantidades;

    public Ventas() {
        this.historialProductos = new Stack<>();
        this.historialCantidades = new Stack<>();
    }

    public void realizarVenta(Producto productoParaVender, int cantidadSolicitada) {
        if (productoParaVender.getStockActual() >= cantidadSolicitada) {
            // 1. Reducir el stock físicamente en el objeto producto
            productoParaVender.vender(cantidadSolicitada);
            
            // 2. Guardar en las pilas para permitir el "Deshacer"
            historialProductos.push(productoParaVender);
            historialCantidades.push(cantidadSolicitada);
            
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
}