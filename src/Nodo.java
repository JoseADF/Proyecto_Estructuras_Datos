public class Nodo {
    Producto producto;
    Nodo izquierda, derecha;

    public Nodo(Producto producto) {
        this.producto = producto;
        this.izquierda = this.derecha = null;
    }
}