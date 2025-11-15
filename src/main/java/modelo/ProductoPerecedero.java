/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author 9spot
 */
public class ProductoPerecedero extends Producto {
    private String fechaVencimiento;

    public ProductoPerecedero(String codigo, String nombre, double precio, 
                             int cantidadStock, String fechaVencimiento) {
        super(codigo, nombre, precio, cantidadStock);
        this.fechaVencimiento = fechaVencimiento;
    }
    
    @Override
    public double calcularValorInventario() {
        return precio * cantidadStock;
    }

    @Override
    public String toString() {
        return super.toString() + " | Vencimiento: " + fechaVencimiento + 
               " | Tipo: PERECEDERO";
    }
    
    public String getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}