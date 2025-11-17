package controlador;

import modelo.Cliente;
import modelo.Factura;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControladorFacturacion {
    private ArrayList<Factura> facturas;
    private ControladorInventario controladorInventario;
    private int contadorFacturas;
    
    public ControladorFacturacion(ControladorInventario controladorInventario) {
        this.facturas = new ArrayList<>();
        this.controladorInventario = controladorInventario;
        this.contadorFacturas = 1;
    }
    
    public Factura crearFactura(Cliente cliente) {
        String numeroFactura = generarNumeroFactura();
        String fecha = obtenerFechaActual();
        return new Factura(numeroFactura, cliente, fecha);
    }
    
    private String generarNumeroFactura() {
        String numero = String.format("FAC-%04d", contadorFacturas);
        contadorFacturas++;
        return numero;
    }

    private String obtenerFechaActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(new Date());
    }
    
    public void agregarFactura(Factura factura) {
        facturas.add(factura);
    }
    
    public ArrayList<Factura> obtenerTodas() {
        return facturas;
    }
    
    public Factura buscarFactura(String numero) {
        for (Factura f : facturas) {
            if (f.getNumero().equals(numero)) {
                return f;
            }
        }
        return null;
    }
    
    public ControladorInventario getControladorInventario() {
        return controladorInventario;
    }
}