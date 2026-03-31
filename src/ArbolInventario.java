import java.util.PriorityQueue;

public class ArbolInventario {
    public Nodo raiz;

    // Método para insertar
    public void insertar(Producto producto) {
        raiz = insertarRecursivo(raiz, producto);
    }

    private Nodo insertarRecursivo(Nodo actual, Producto nuevo) {
        if (actual == null) return new Nodo(nuevo);

        // Comparamos por ID del producto
        if (nuevo.getId() < actual.producto.getId())
            actual.izquierda = insertarRecursivo(actual.izquierda, nuevo);
        else if (nuevo.getId() > actual.producto.getId())
            actual.derecha = insertarRecursivo(actual.derecha, nuevo);

        return actual;
    }

    // Método de búsqueda
    public Producto buscar(int id) {
        return buscarRecursivo(raiz, id);
    }

    private Producto buscarRecursivo(Nodo actual, int id) {
        if (actual == null || actual.producto.getId() == id)
            return (actual != null) ? actual.producto : null;

        if (id < actual.producto.getId())
            return buscarRecursivo(actual.izquierda, id);
        
        return buscarRecursivo(actual.derecha, id);
    }

    // Recorrido In-Order para el reporte de inventario
    public void mostrarInOrder() {
        ayudanteInOrder(raiz);
    }

    private void ayudanteInOrder(Nodo nodo) {
        if (nodo != null) {
            ayudanteInOrder(nodo.izquierda);
            System.out.println("  > " + nodo.producto);
            ayudanteInOrder(nodo.derecha);
        }
    }
    
    // Añade esto dentro de la clase ArbolInventario
    public void llenarColaPrioridad(Nodo nodo, PriorityQueue<Producto> cola) {
        if (nodo != null) {
            llenarColaPrioridad(nodo.izquierda, cola);

            // Verificamos si el producto es crítico y si no está ya en la cola
            if (nodo.producto.esCritico() && !cola.contains(nodo.producto)) {
                cola.add(nodo.producto);
            }

            llenarColaPrioridad(nodo.derecha, cola);
        }
    }
}