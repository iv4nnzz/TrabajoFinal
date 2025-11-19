/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controlador;

/**
 *
 * @author 9spot
 */
import modelo.Producto;
import java.util.ArrayList;

public class ControladorInventario {
    private ArrayList<Producto> productos;
    
    public ControladorInventario() {
        this.productos = new ArrayList<>();
        cargarDatosPrueba();
    }
    
    private void cargarDatosPrueba() {
        productos.add(new modelo.ProductoPerecedero("P001", "Leche Entera", 3500, 50, "15/03/2025"));
        productos.add(new modelo.ProductoPerecedero("P002", "Queso Campesino", 12000, 30, "20/03/2025"));
        productos.add(new modelo.ProductoNoPerecedero("NP001", "Arroz Diana 1kg", 4200, 100, 6));
        productos.add(new modelo.ProductoNoPerecedero("NP002", "Aceite Girasol", 8500, 60, 12));
    }
    
    public boolean agregarProducto(Producto producto) {
        if (buscarProducto(producto.getCodigo()) != null) {
            return false; 
        }
        productos.add(producto);
        return true;
    }
    
    public boolean eliminarProducto(String codigo) {
        Producto producto = buscarProducto(codigo);
        if (producto != null) {
            productos.remove(producto);
            return true;
        }
        return false;
    }
    
    public boolean editarProducto(String codigo, Producto productoNuevo) {
        Producto producto = buscarProducto(codigo);
        if (producto != null) {
            int indice = productos.indexOf(producto);
            productos.set(indice, productoNuevo);
            return true;
        }
        return false;
    }
    
    public Producto buscarProducto(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        return null;
    }
    
    public ArrayList<Producto> obtenerTodos() {
        return productos;
    }
    
    public boolean verificarStock(String codigo, int cantidad) {
        Producto producto = buscarProducto(codigo);
        if (producto != null) {
            return producto.getCantidadStock() >= cantidad;
        }
        return false;
    }
    
    public void reducirStock(String codigo, int cantidad) {
        Producto producto = buscarProducto(codigo);
        if (producto != null) {
            int nuevoStock = producto.getCantidadStock() - cantidad;
            producto.setCantidadStock(nuevoStock);
        }
    }
    
    //nuevo
    
    public ArrayList<Producto> buscarProducto(String nombre, boolean porNombre) {
        ArrayList<Producto> resultados = new ArrayList<>();
        if (porNombre) {
            for (Producto p : productos) {
                if (p.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                    resultados.add(p);
                }
            }
        }
        return resultados;
    }
    
    public ArrayList<Producto> buscarProducto(double precioMin, double precioMax) {
        ArrayList<Producto> resultados = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getPrecio() >= precioMin && p.getPrecio() <= precioMax) {
                resultados.add(p);
            }
        }
        return resultados;
    }
    
    public ArrayList<Producto> obtenerProductosBajoStock() {
        ArrayList<Producto> bajoStock = new ArrayList<>();
        for (Producto p : productos) {
            if (p.tieneBajoStock()) {
                bajoStock.add(p);
            }
        }
        return bajoStock;
    }
    
    public double calcularValorTotalInventario() {
        double total = 0.0;
        for (Producto p : productos) {
            total += p.calcularValorInventario();
        }
        return total;
    }
    
    public int contarProductosPerecederos() {
        int contador = 0;
        for (Producto p : productos) {
            if (p instanceof modelo.ProductoPerecedero) {
                contador++;
            }
        }
        return contador;
    }
    
    public int contarProductosNoPerecederos() {
        int contador = 0;
        for (Producto p : productos) {
            if (p instanceof modelo.ProductoNoPerecedero) {
                contador++;
            }
        }
        return contador;
    }
    
    public void ordenarPorNombre() {
        productos.sort((p1, p2) -> p1.getNombre().compareTo(p2.getNombre()));
    }
    
    public void ordenarPorPrecio() {
        productos.sort((p1, p2) -> Double.compare(p1.getPrecio(), p2.getPrecio()));
    }
    
    public void ordenarPorStock() {
        productos.sort((p1, p2) -> Integer.compare(p1.getCantidadStock(), p2.getCantidadStock()));
    }
}