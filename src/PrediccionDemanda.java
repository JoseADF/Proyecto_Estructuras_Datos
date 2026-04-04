
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.TreeMap;
 
public class PrediccionDemanda {
    
    //Metodo para agrupar ventas con misma fecha
     private static TreeMap<LocalDate, Integer> agruparVentas(ArrayList<Ventas.RegistroVenta> ventas) {
        TreeMap<LocalDate, Integer> ventasPorDia = new TreeMap<>(); // guardar ventas por clave (fecha)

        for (Ventas.RegistroVenta rv : ventas) {
            ventasPorDia.put(rv.getFecha(), ventasPorDia.getOrDefault(rv.getFecha(), 0) + rv.getCantidad());
        }

        return ventasPorDia;
    }
    // Completar fechas sin ventas e indicar cantidad como 0
    private static TreeMap<LocalDate, Integer> completarFechas(TreeMap<LocalDate, Integer> ventasPorDia) {
        TreeMap<LocalDate, Integer> completo = new TreeMap<>();
        LocalDate inicio = ventasPorDia.firstKey();
        LocalDate fin = ventasPorDia.lastKey();
        LocalDate actual = inicio;

        while (!actual.isAfter(fin)) {
            completo.put(actual, ventasPorDia.getOrDefault(actual, 0));
            actual = actual.plusDays(1);//avanza siguiente fecha
        }

        return completo;
    }
    
    //Calculo Promedio Movil
    public static double promedioMovil(ArrayList<Ventas.RegistroVenta> ventas, int periodo) {
        TreeMap<LocalDate, Integer> VentasporDia = agruparVentas(ventas);
        VentasporDia = completarFechas(VentasporDia);
        ArrayList<Integer> valores = new ArrayList<>(VentasporDia.values());
        if (valores.size() < periodo) {
            System.out.println("No hay suficientes datos");
            return 0;
        }
        int suma = 0;

        for (int i = valores.size() - periodo; i < valores.size(); i++) {
            suma += valores.get(i);
        }
        return (double) suma / periodo;
    }
    
    //Regresion Lineal
    public static double regresionLineal(ArrayList<Ventas.RegistroVenta> ventas, int periodo) {
        TreeMap<LocalDate, Integer> ventasPorDia = agruparVentas(ventas);
        ventasPorDia = completarFechas(ventasPorDia);

        ArrayList<LocalDate> fechas = new ArrayList<>(ventasPorDia.keySet());
        ArrayList<Integer> valores = new ArrayList<>(ventasPorDia.values());
        
        int cantidadDatos  = valores.size();
        if (cantidadDatos < 2 ) {return 0;}
        // Ajustamos el inicio para que no se salga de rango si hay pocos datos
        int inicio = Math.max(0, cantidadDatos - periodo);
        int cantidadEnPeriodo = cantidadDatos - inicio;
        
        if (cantidadEnPeriodo < 2) {return 0;}
        
        double mTendencia = calcularPendiente(ventas, periodo);
        double sumTiempo = 0; // suma de los días
        double sumVentas = 0; // suma de ventas
        
        LocalDate fechaBase = fechas.get(inicio) ;
        
        for (int i = inicio; i < cantidadDatos; i++) {
            long DiasX = ChronoUnit.DAYS.between(fechaBase, fechas.get(i));  
            int VentasY = valores.get(i);
            
             sumTiempo += DiasX;
             sumVentas += VentasY;
        }
        double bInterseccion = (sumVentas - mTendencia * sumTiempo) / cantidadEnPeriodo ;
        long siguienteX = ChronoUnit.DAYS.between(fechaBase, fechas.get(cantidadDatos - 1).plusDays(1));
        
        //Prediccion de la siguiente venta
        return bInterseccion + mTendencia * siguienteX;
    } 
    //Deteccion de Tendencia
    public static String tendencia(ArrayList<Ventas.RegistroVenta> ventas, int periodo) {
        double pendiente = calcularPendiente(ventas, periodo);
        if (ventas.size() < 2) {return "Insuficiente información";}

        if (pendiente > 0) {
            return "Creciente";
        } 
        else if (pendiente < 0) {
            return "Decreciente";
        } 
        else {
            return "Estable";
        }
    }
    //Calculo Pendiente
    private static double calcularPendiente(ArrayList<Ventas.RegistroVenta> ventas, int periodo) {
        TreeMap<LocalDate, Integer> ventasPorDia = agruparVentas(ventas);
        ventasPorDia = completarFechas(ventasPorDia);

        ArrayList<LocalDate> fechas = new ArrayList<>(ventasPorDia.keySet());
        ArrayList<Integer> valores = new ArrayList<>(ventasPorDia.values());
   
        int cantidadDatos  = valores.size();
        int inicio = Math.max(0, cantidadDatos - periodo);
        int cantidadEnPeriodo = cantidadDatos - inicio;

        if (cantidadDatos < 2 ) {return 0;}

        double sumDiasX = 0; // suma de los días
        double sumVentasY = 0; // suma de ventas
        double sumXY = 0; // suma de (día × ventas)
        double sumX2 = 0; // suma de (día²)

        LocalDate fechaBase = fechas.get(inicio);

        for (int i = inicio; i < cantidadDatos; i++) {
            long PeriodoX = ChronoUnit.DAYS.between(fechaBase, fechas.get(i)); 
            int VentasY = valores.get(i);

            sumDiasX += PeriodoX; 
            sumVentasY += VentasY;
            sumXY += PeriodoX * VentasY;
            sumX2 += PeriodoX * PeriodoX;
        }
        return (cantidadEnPeriodo  * sumXY - sumDiasX * sumVentasY) / (cantidadEnPeriodo  * sumX2 - sumDiasX * sumDiasX);
    }
    //Imprimir ventas por fecha 
    public static void imprimirVentasCompletas(ArrayList<Ventas.RegistroVenta> ventas) {
        TreeMap<LocalDate, Integer> ventasPorDia = agruparVentas(ventas);
        System.out.println("\n--- VENTAS COMPLETAS ---");

        for (LocalDate fecha : ventasPorDia.keySet()) {
            System.out.println("Fecha: " + fecha + " | Cantidad: " + ventasPorDia.get(fecha));
        }
    }
}
