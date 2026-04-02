import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MotorSimulacion {
    private Inventario inventario;
    private Ventas moduloVentas;
    private Random generadorAleatorio;
    // La cola gestiona el orden de llegada de las peticiones de compra
    private Queue<Integer> colaEventosVenta;

    public MotorSimulacion(Inventario inventario, Ventas moduloVentas) {
        this.inventario = inventario;
        this.moduloVentas = moduloVentas;
        this.generadorAleatorio = new Random();
        this.colaEventosVenta = new LinkedList<>();
    }

    public void ejecutarSimulacion(String categoriaDestino, int[] listaIdsDisponibles, int cantidadDeVentas) {
        System.out.println("\n=== [MOTOR DE SIMULACIÓN ACTIVO] ===");
        System.out.println("Encolando " + cantidadDeVentas + " eventos de venta para la categoría: " + categoriaDestino);
        
        for (int i = 0; i < cantidadDeVentas; i++) {
            int idSeleccionado = listaIdsDisponibles[generadorAleatorio.nextInt(listaIdsDisponibles.length)];
            colaEventosVenta.add(idSeleccionado);
        }

        int ventasExitosas = 0;
        while (!colaEventosVenta.isEmpty()) {
            int idActual = colaEventosVenta.poll(); // Extraemos el ID de la cola
            Producto productoObjetivo = inventario.buscarProducto(categoriaDestino, idActual);

            if (productoObjetivo != null) {
                // Generamos una compra aleatoria entre 1 y 3 unidades
                int cantidadComprada = generadorAleatorio.nextInt(3) + 1;
                
                System.out.println(" > Procesando compra: " + cantidadComprada + " unidades de " + productoObjetivo.getNombre());
                moduloVentas.realizarVenta(productoObjetivo, cantidadComprada);
                ventasExitosas++;
            }
        }
        
        System.out.println("=== [SIMULACIÓN FINALIZADA] ===");
        System.out.println("Total de eventos procesados con éxito: " + ventasExitosas);
    }
}