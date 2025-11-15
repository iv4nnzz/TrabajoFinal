/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author USUARIO
 */

public class ItemFactura {
    private Producto producto;
    private int cantidad;
    private double subtotal;
    
    public ItemFactura(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.subtotal = calcularSubtotal();
    }
    
    public double calcularSubtotal() {
        return producto.getPrecio() * cantidad;
    }
    
    @Override
    public String toString() {
        return producto.getNombre() + " | Cantidad: " + cantidad + 
               " | Precio Unit: $" + producto.getPrecio() + 
               " | Subtotal: $" + subtotal;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
}