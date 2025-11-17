/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author USUARIO
 */
public class ProductoNoPerecedero extends Producto {
    private int mesesGarantia;
    
    public ProductoNoPerecedero(String codigo, String nombre, double precio, 
                               int cantidadStock, int mesesGarantia) {
        super(codigo, nombre, precio, cantidadStock);
        this.mesesGarantia = mesesGarantia;
    }
    
    @Override
    public double calcularValorInventario() {
        double valorBase = precio * cantidadStock;
        if (mesesGarantia > 12) {
            return valorBase * 1.10; 
        }
        return valorBase;
    }
    
    @Override
    public String toString() {
        return super.toString() + " | Garantía: " + mesesGarantia + 
               " meses | Tipo: NO PERECEDERO";
    }
    
    @Override
    public double aplicarDescuento(double porcentaje) { //nuevo
        double descuentoFinal = (mesesGarantia > 12) ? porcentaje / 2 : porcentaje;
        return precio * (1 - descuentoFinal / 100.0);
    }
    
    public int getMesesGarantia() {
        return mesesGarantia;
    }
    
    public void setMesesGarantia(int mesesGarantia) {
        this.mesesGarantia = mesesGarantia;
    }
}