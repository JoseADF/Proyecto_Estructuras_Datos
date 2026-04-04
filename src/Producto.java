import java.util.ArrayList;

public class Producto implements Comparable<Producto> {
    private int id;
    private String nombre;
    private String categoria;
    private int stockActual;
    private int stockMinimo; // Para manejo de cola de prioridad por nivel crítico.
    private double precio;
    private ArrayList<Integer> historialVentas = new ArrayList<>();
    
    // Atributos para el Grafo (Clase Logistica y reabesticimiento)
    private double coordX; 
    private double coordY;

    public Producto(int id, String nombre, String categoria, int stockActual, int stockMinimo, double precio, double x, double y) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.precio = precio;
        this.coordX = x;
        this.coordY = y;
    }
    
    
    // Registrar venta del producto
    public void vender(int cantidad) {
        if (this.stockActual >= cantidad) {
            this.stockActual -= cantidad;
        }
    }

    //Registro de ventas
    public void registrarVenta(int cantidad) {
        historialVentas.add(cantidad);
    }
    // Se encarga de verificar si el producto cuenta con prioridad para ser reabestecido.
    public boolean esCritico() {
        return this.stockActual <= this.stockMinimo;
    }

    // Getters y Setters de la clase debido al uso de encapsulamiento
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public int getStockActual() { return stockActual; }
    public void setStockActual(int stock) { this.stockActual = stock; }
    public double getCoordX() { return coordX; }
    public double getCoordY() { return coordY; }
    public ArrayList<Integer> getHistorialVentas() {return historialVentas;}

    
    @Override
    public String toString() {
        return "ID: " + id + " | " + nombre + " | Stock: " + stockActual + (esCritico() ? " [CRÍTICO]" : "");
    }
    
    @Override
    public int compareTo(Producto otro) {
        //Clausula para indicarle a java que el que tenga menor stock es el más urgente
        return Integer.compare(this.stockActual, otro.stockActual);
}
    
}
