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
        
        // 1. Carga inicial de prueba (Asegúrate de que los IDs sean únicos)
        inventario.agregarProducto(new Producto(50, "Laptop Gaming", "Tecnologia", 2, 5, 750.0, 10.0, 15.0));
        inventario.agregarProducto(new Producto(10, "Mouse Optico", "Perifericos", 20, 10, 25.0, 12.0, 18.0));
        inventario.agregarProducto(new Producto(30, "Teclado Mecanico", "Perifericos", 3, 5, 80.0, 12.5, 18.5));

        // 2. Sincronización inicial de la cola de prioridad
        inventario.actualizarPedidosUrgentes(colaPrioridad);

        while (!salir) {
            System.out.println("\n--- SISTEMA DE GESTION DE INVENTARIO ---");
            System.out.println("1. Registrar Venta");
            System.out.println("2. Consultar Inventario Completo");
            System.out.println("3. Ver Pedidos de Reabastecimiento (Urgentes)");
            System.out.println("4. Calcular Ruta de Distribucion (Proximamente)");
            System.out.println("5. Salir");
            
            try {
                System.out.print("Seleccione una opcion: ");
                opcion = leer.nextInt();

                switch (opcion) {
                    case 1:
                        registrarVenta();
                        break;
                    case 2:
                        inventario.listarTodoElInventario();
                        break;
                    case 3:
                        verPedidosUrgentes();
                        break;
                    case 4:
                        System.out.println("Modulo de Logistica en desarrollo para el viernes...");
                        break;
                    case 5:
                        salir = true;
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }
            } catch (Exception e) {
                System.out.println("Error: Ingrese un numero valido.");
                leer.next(); // Limpiar el scanner
            }
        }
    }

    private static void registrarVenta() {
        System.out.println("\n--- REGISTRO DE VENTA ---");

        System.out.print("Ingrese la categoría: ");
        String categoriaDigitada = leer.next();

        System.out.print("Ingrese el ID del producto: ");
        int idBuscado = leer.nextInt();

        Producto productoEncontrado = inventario.buscarProducto(categoriaDigitada, idBuscado);

        if (productoEncontrado != null) {
            System.out.println("Producto: " + productoEncontrado.getNombre() + " | Stock actual: " + productoEncontrado.getStockActual());
            System.out.print("¿Cuántas unidades desea vender?: ");
            int cantidadAVender = leer.nextInt();

            // Validamos si hay suficiente stock antes de llamar al módulo de ventas
            if (cantidadAVender <= 0) {
                System.out.println("❌ Cantidad no válida.");
                return;
            }

            if (cantidadAVender <= productoEncontrado.getStockActual()) {
                int existenciaPrevia = productoEncontrado.getStockActual();

                // Pasamos la cantidad elegida al módulo de ventas
                moduloVentas.realizarVenta(productoEncontrado, cantidadAVender);

                System.out.println("✅ Venta exitosa. Stock actualizado: " + productoEncontrado.getStockActual());

                // Actualizamos la cola de prioridad
                inventario.actualizarPedidosUrgentes(colaPrioridad);

                if (productoEncontrado.esCritico()) {
                    System.out.println("⚠️ ALERTA: Stock crítico. Se requiere reabastecimiento.");
                }
            } else {
                System.out.println("❌ Error: No hay suficiente stock. Solo quedan " + productoEncontrado.getStockActual() + " unidades.");
            }
        } else {
            System.out.println("❌ Error: Producto no encontrado.");
        }
}

    private static void verPedidosUrgentes() {
        System.out.println("\n--- LISTA DE REABASTECIMIENTO (Prioridad por bajo stock) ---");
        
        // Sincronizamos antes de mostrar para asegurar que los datos esten frescos
        inventario.actualizarPedidosUrgentes(colaPrioridad);

        if (colaPrioridad.isEmpty()) {
            System.out.println("✅ No hay productos criticos en este momento.");
            return;
        }

        // Mostramos y vaciamos la cola para la siguiente actualizacion
        while (!colaPrioridad.isEmpty()) {
            Producto urgente = colaPrioridad.poll(); 
            System.out.println("[URGENTE] " + urgente);
        }
    }
}