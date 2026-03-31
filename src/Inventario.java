import java.util.HashMap;
import java.util.PriorityQueue;

public class Inventario {
    // Cambia ArbolBST por ArbolInventario
    private HashMap<String, ArbolInventario> categorias;

    public Inventario() {
        this.categorias = new HashMap<>();
    }

    public void agregarProducto(Producto producto) {
        String categoria = producto.getCategoria();
        if (!categorias.containsKey(categoria)) {
            categorias.put(categoria, new ArbolInventario());
        }
        
        ArbolInventario arbol = categorias.get(categoria);
        // Aquí usamos el método insertar de tu clase ArbolInventario
        arbol.insertar(producto); 
    }
    
    public Producto buscarProducto(String categoria, int id) {
        if (categorias.containsKey(categoria)) {
            return categorias.get(categoria).buscar(id);
        }
        return null;
    }

    public void actualizarPedidosUrgentes(PriorityQueue<Producto> colaPedidos) {
        for (ArbolInventario arbol : categorias.values()) {
            // Este método lo agregaremos abajo en ArbolInventario
            arbol.llenarColaPrioridad(arbol.raiz, colaPedidos);
        }
    }

    public void listarTodoElInventario() {
        System.out.println("\n--- ESTADO ACTUAL DEL INVENTARIO (Árboles Manuales) ---");
        for (String nombreCat : categorias.keySet()) {
            System.out.println("Categoría: " + nombreCat);
            // Cambiamos inorder por mostrarInOrder que es el que tienes en tu clase
            categorias.get(nombreCat).mostrarInOrder();
        }
    }
}