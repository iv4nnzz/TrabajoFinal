package controlador;

import modelo.Cliente;
import modelo.Factura;
import modelo.ItemFactura;
import modelo.Producto;
import modelo.ProductoPerecedero;
import modelo.ProductoNoPerecedero;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

public class ControladorFacturacion {
    private ArrayList<Factura> facturas;
    private ControladorInventario controladorInventario;
    private int contadorFacturas;
    private static final String ARCHIVO_FACTURAS = "facturas.txt";
    private static final String ARCHIVO_CONTADOR = "contador_facturas.txt";
    
    public ControladorFacturacion(ControladorInventario controladorInventario) {
        this.facturas = new ArrayList<>();
        this.controladorInventario = controladorInventario;
        this.contadorFacturas = 1;
        cargarContadorDesdeArchivo();
        cargarFacturasDesdeArchivo();
    }
    
    private void guardarFacturasEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_FACTURAS))) {
            for (Factura f : facturas) {
                String linea = convertirFacturaALinea(f);
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar facturas: " + e.getMessage());
        }
    }
    
    private String convertirFacturaALinea(Factura f) {
        StringBuilder sb = new StringBuilder();
        Cliente cliente = f.getCliente();
        
        sb.append("FACTURA|");
        sb.append(f.getNumero()).append("|");
        sb.append(f.getFecha()).append("|");
        sb.append(cliente.getCedula()).append("|");
        sb.append(cliente.getNombre()).append("|");
        sb.append(cliente.getTelefono()).append("|");
        sb.append(f.getTotal()).append("|");
        sb.append(f.getItems().size());
        sb.append("\n");
        
        for (ItemFactura item : f.getItems()) {
            sb.append("ITEM|");
            sb.append(item.getProducto().getCodigo()).append("|");
            sb.append(item.getProducto().getNombre()).append("|");
            sb.append(item.getProducto().getPrecio()).append("|");
            sb.append(item.getCantidad()).append("|");
            sb.append(item.getSubtotal());
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    private void cargarFacturasDesdeArchivo() {
        File archivo = new File(ARCHIVO_FACTURAS);
        
        if (!archivo.exists()) {
            System.out.println("Archivo de facturas no encontrado. Se creará uno nuevo.");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            Factura facturaActual = null;
            
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    if (linea.startsWith("FACTURA|")) {
                        if (facturaActual != null) {
                            facturas.add(facturaActual);
                        }
                        facturaActual = crearFacturaDesdeLinea(linea);
                    } else if (linea.startsWith("ITEM|") && facturaActual != null) {
                        ItemFactura item = crearItemDesdeLinea(linea);
                        if (item != null) {
                            facturaActual.agregarItem(item);
                        }
                    }
                }
            }
            
            if (facturaActual != null) {
                facturas.add(facturaActual);
            }
            
            System.out.println("Facturas cargadas: " + facturas.size());
        } catch (IOException e) {
            System.err.println("Error al cargar facturas: " + e.getMessage());
        }
    }
    
    private Factura crearFacturaDesdeLinea(String linea) {
        try {
            String[] partes = linea.split("\\|");
            
            if (partes.length < 8) {
                System.err.println("Línea de factura inválida: " + linea);
                return null;
            }
            
            String numero = partes[1];
            String fecha = partes[2];
            String cedula = partes[3];
            String nombreCliente = partes[4];
            String telefono = partes[5];
            
            Cliente cliente = new Cliente(cedula, nombreCliente, telefono);
            return new Factura(numero, cliente, fecha);
            
        } catch (Exception e) {
            System.err.println("Error al procesar factura: " + e.getMessage());
        }
        
        return null;
    }
    
    private ItemFactura crearItemDesdeLinea(String linea) {
        try {
            String[] partes = linea.split("\\|");
            
            if (partes.length < 6) {
                System.err.println("Línea de item inválida: " + linea);
                return null;
            }
            
            String codigoProducto = partes[1];
            String nombreProducto = partes[2];
            double precio = Double.parseDouble(partes[3]);
            int cantidad = Integer.parseInt(partes[4]);
            
            Producto producto = controladorInventario.buscarProducto(codigoProducto);
            
            if (producto == null) {
                producto = new ProductoPerecedero(codigoProducto, nombreProducto, precio, 0, "N/A");
            }
            
            return new ItemFactura(producto, cantidad);
            
        } catch (Exception e) {
            System.err.println("Error al procesar item: " + e.getMessage());
        }
        
        return null;
    }
    
    private void guardarContadorEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_CONTADOR))) {
            writer.write(String.valueOf(contadorFacturas));
        } catch (IOException e) {
            System.err.println("Error al guardar contador: " + e.getMessage());
        }
    }
    
    private void cargarContadorDesdeArchivo() {
        File archivo = new File(ARCHIVO_CONTADOR);
        
        if (!archivo.exists()) {
            contadorFacturas = 1;
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea = reader.readLine();
            if (linea != null && !linea.trim().isEmpty()) {
                contadorFacturas = Integer.parseInt(linea.trim());
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al cargar contador: " + e.getMessage());
            contadorFacturas = 1;
        }
    }
        
    public Factura crearFactura(Cliente cliente) {
        String numeroFactura = generarNumeroFactura();
        String fecha = obtenerFechaActual();
        return new Factura(numeroFactura, cliente, fecha);
    }
    
    private String generarNumeroFactura() {
        String numero = String.format("FAC-%04d", contadorFacturas);
        contadorFacturas++;
        guardarContadorEnArchivo();
        return numero;
    }

    private String obtenerFechaActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(new Date());
    }
    
    public void agregarFactura(Factura factura) {
        facturas.add(factura);
        guardarFacturasEnArchivo(); 
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
    
    public double calcularTotalVentas() {
        double total = 0.0;
        for (Factura f : facturas) {
            total += f.getTotal();
        }
        return total;
    }
    
    public int obtenerCantidadFacturas() {
        return facturas.size();
    }
    
    public boolean exportarFactura(Factura factura, String rutaArchivo) {
        try {
            java.io.FileWriter writer = new java.io.FileWriter(rutaArchivo);
            writer.write(factura.toString());
            writer.close();
            return true;
        } catch (java.io.IOException e) {
            return false;
        }
    }
}