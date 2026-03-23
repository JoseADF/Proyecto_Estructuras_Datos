import java.util.Scanner;
import java.util.PriorityQueue;

public class Principal {
    static Scanner leer = new Scanner(System.in);
    private static Inventario inventario = new Inventario();
    private static PriorityQueue<Producto> colaPrioridad = new PriorityQueue<>();
    private static Ventas moduloVentas = new Ventas();
    
    
    public static void main(String[] args) {
        boolean salir = false;
        int opcion; 
        
        // Prueba de productos en inventario
        inventario.agregarProducto(new Producto(50, "Laptop Gaming", "Tecnología", 2, 5, 750.0, 10.0, 15.0));
        inventario.agregarProducto(new Producto(10, "Mouse Óptico", "Periféricos", 20, 10, 25.0, 12.0, 18.0));

        while (!salir) {
            System.out.println("SISTEMA DE GESTIÓN DE INVENTARIO");
            System.out.println("1. Registrar Venta (Usa Pilas/Ventas)");
            System.out.println("2. Consultar Inventario");
            System.out.println("3. Ver Pedidos Urgentes");
            System.out.println("4. Calcular Ruta de Distribución");
            System.out.println("5. Salir");
            
            try {
                System.out.print("Seleccione una opción: ");
                opcion = leer.nextInt();

                switch (opcion) {
                    case 1:
                        registrarVenta();
                        break;
                    case 2:
                        // PRUEBA RÁPIDA DE INTEGRACIÓN
                        System.out.println("*** Mostrando inventario del sistema ***");

                        // Procesar niveles críticos
                        inventario.actualizarPedidosUrgentes(colaPrioridad);

                        // Mostrar resultados
                        inventario.listarTodoElInventario();
                        break;
                    case 3:
                        System.out.println("Mostrando productos críticos...");
                        inventario.actualizarPedidosUrgentes(colaPrioridad);
                        verPedidosUrgentes();
                        break;
                    case 4:
                        System.out.println("Calculando ruta óptima con Dijkstra...");
                        break;
                    case 5:
                        salir = true;
                        break;
                    default:
                        System.out.println("Solo números entre 1 y 5");
                }
            } catch (Exception e) {
                System.out.println("Debes insertar un número");
                leer.next();
            }
        }
    }

    private static void registrarVenta() {
        System.out.println("\n--- REGISTRAR VENTA ---");
        System.out.print("Ingrese la categoría: ");
        String cat = leer.next();
        System.out.print("Ingrese el ID del producto: ");
        int id = leer.nextInt();

        // Busqueda del producto en el inventario
        Producto productoEncontrado = inventario.buscarProducto(cat, id);

        if (productoEncontrado != null) {
            moduloVentas.realizarVenta(productoEncontrado, 1);
            
            // Actualizamos la cola de pedidos por si el stock bajó a nivel crítico
            inventario.actualizarPedidosUrgentes(colaPrioridad);

            System.out.println("¿Desea deshacer la venta? (1. Sí / 2. No)");
            if (leer.nextInt() == 1) {
                moduloVentas.deshacerVenta();
                inventario.actualizarPedidosUrgentes(colaPrioridad);
            }
        } else {
            System.out.println("⚠️ Producto no encontrado en esa categoría.");
        }
    }
    
    
    private static void verPedidosUrgentes() {
    System.out.println("\n--- LISTA DE REABASTECIMIENTO (Prioridad por bajo stock) ---");
    
    if (colaPrioridad.isEmpty()) {
        System.out.println("✅ No hay productos críticos en este momento.");
        return;
    }

    // Usamos un ciclo para sacar y mostrar cada producto de la cola
    // Importante: poll() saca al elemento de la cola definitivamente
    while (!colaPrioridad.isEmpty()) {
        Producto urgente = colaPrioridad.poll(); 
        System.out.println("[PEDIDO] " + urgente); // Aquí se usa tu toString() automáticamente
    }
}
}
