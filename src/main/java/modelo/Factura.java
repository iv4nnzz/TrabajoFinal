/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package modelo;
import java.util.ArrayList;
/**
 *
 * @author USUARIO
 */

public class Factura {
    private String numero;
    private Cliente cliente;
    private ArrayList<ItemFactura> items;
    private double total;
    private String fecha;
    
    public Factura(String numero, Cliente cliente, String fecha) {
        this.numero = numero;
        this.cliente = cliente;
        this.fecha = fecha;
        this.items = new ArrayList<>();
        this.total = 0.0;
    }
    
    public void agregarItem(ItemFactura item) {
        items.add(item);
        calcularTotal();
    }
    
    public double calcularTotal() {
        total = 0.0;
        for (ItemFactura item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== FACTURA ==========\n");
        sb.append("Número: ").append(numero).append("\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Cliente: ").append(cliente.toString()).append("\n");
        sb.append("========== ITEMS ==========\n");
        for (ItemFactura item : items) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("===========================\n");
        sb.append("TOTAL: $").append(String.format("%.2f", total)).append("\n");
        sb.append("===========================");
        return sb.toString();
    }
    
    public String getNumero() {
        return numero;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public ArrayList<ItemFactura> getItems() {
        return items;
    }
    
    public double getTotal() {
        return total;
    }
    
    public String getFecha() {
        return fecha;
    }
}