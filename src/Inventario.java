import java.util.HashMap;
import java.util.TreeMap;
import java.util.PriorityQueue;
import java.util.Collection;

public class Inventario {
    // El mapa para categorías y el árbol (TreeMap) para los productos
    private HashMap<String, TreeMap<Integer, Producto>> categorias;

    public Inventario() {
        this.categorias = new HashMap<>();
    }

    /**
     * Agrega un producto al sistema.
     * Si la categoría no existe en el HashMap, crea un nuevo TreeMap (Árbol).
     */
    public void agregarProducto(Producto producto) {
        String categoria = producto.getCategoria();
        if (!categorias.containsKey(categoria)) {
            categorias.put(categoria, new TreeMap<Integer, Producto>());
        }
        // Insertar en el árbol de la categoría usando el ID como llave
        categorias.get(categoria).put(producto.getId(), producto);
    }
    
    /**
     * Busca un producto en el inventario si no existe retorna nulo
     */
    public Producto buscarProducto(String categoria, int id) {
        if (categorias.containsKey(categoria)) {
            // Buscamos directamente en el árbol de esa categoría
            return categorias.get(categoria).get(id);
        }
        return null; // No se encontró la categoría o el ID
    }
    
    
    

    /**
     * Recorre todos los árboles de todas las categorías para identificar
     * productos con stock bajo y enviarlos a la cola de prioridad.
     */
    public void actualizarPedidosUrgentes(PriorityQueue<Producto> colaPedidos) {
        // Obtenemos todos los árboles (TreeMap) del mapa
        Collection<TreeMap<Integer, Producto>> todosLosArboles = categorias.values();
        
        for (TreeMap<Integer, Producto> arbol : todosLosArboles) {
            // Recorremos cada producto dentro del árbol
            for (Producto p : arbol.values()) {
                if (p.esCritico()) {
                    // Solo lo agregamos si no está ya en la cola para evitar duplicados
                    if (!colaPedidos.contains(p)) {
                        colaPedidos.add(p);
                    }
                }
            }
        }
    }

    public void listarTodoElInventario() {
        System.out.println("\n--- ESTADO ACTUAL DEL INVENTARIO ---");
        categorias.forEach((nombreCat, arbol) -> {
            System.out.println("Categoría: " + nombreCat);
            arbol.values().forEach(p -> System.out.println("  > " + p));
        });
    }
}