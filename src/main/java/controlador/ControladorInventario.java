package controlador;

import modelo.Producto;
import modelo.ProductoPerecedero;
import modelo.ProductoNoPerecedero;
import java.util.ArrayList;
import java.io.*;

public class ControladorInventario {
    private ArrayList<Producto> productos;
    private static final String ARCHIVO_PRODUCTOS = "productos.txt";
    
    public ControladorInventario() {
        this.productos = new ArrayList<>();
        cargarProductosDesdeArchivo(); // Cargar datos al iniciar
        
        // Solo cargar datos de prueba si no hay productos guardados
        if (productos.isEmpty()) {
            cargarDatosPrueba();
            guardarProductosEnArchivo();
        }
    }
    
    private void guardarProductosEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_PRODUCTOS))) {
            for (Producto p : productos) {
                String linea = convertirProductoALinea(p);
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar productos: " + e.getMessage());
        }
    }
    
    private String convertirProductoALinea(Producto p) {
        StringBuilder sb = new StringBuilder();
        
        if (p instanceof ProductoPerecedero) {
            ProductoPerecedero pp = (ProductoPerecedero) p;
            sb.append("PERECEDERO|");
            sb.append(p.getCodigo()).append("|");
            sb.append(p.getNombre()).append("|");
            sb.append(p.getPrecio()).append("|");
            sb.append(p.getCantidadStock()).append("|");
            sb.append(pp.getFechaVencimiento());
        } else if (p instanceof ProductoNoPerecedero) {
            ProductoNoPerecedero pnp = (ProductoNoPerecedero) p;
            sb.append("NO_PERECEDERO|");
            sb.append(p.getCodigo()).append("|");
            sb.append(p.getNombre()).append("|");
            sb.append(p.getPrecio()).append("|");
            sb.append(p.getCantidadStock()).append("|");
            sb.append(pnp.getMesesGarantia());
        }
        
        return sb.toString();
    }
   
    private void cargarProductosDesdeArchivo() {
        File archivo = new File(ARCHIVO_PRODUCTOS);
        
        if (!archivo.exists()) {
            System.out.println("Archivo de productos no encontrado. Se creará uno nuevo.");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    Producto p = convertirLineaAProducto(linea);
                    if (p != null) {
                        productos.add(p);
                    }
                }
            }
            System.out.println("Productos cargados: " + productos.size());
        } catch (IOException e) {
            System.err.println("Error al cargar productos: " + e.getMessage());
        }
    }
    
    private Producto convertirLineaAProducto(String linea) {
        try {
            String[] partes = linea.split("\\|");
            
            if (partes.length < 6) {
                System.err.println("Línea inválida: " + linea);
                return null;
            }
            
            String tipo = partes[0];
            String codigo = partes[1];
            String nombre = partes[2];
            double precio = Double.parseDouble(partes[3]);
            int stock = Integer.parseInt(partes[4]);
            String datoEspecial = partes[5];
            
            if (tipo.equals("PERECEDERO")) {
                return new ProductoPerecedero(codigo, nombre, precio, stock, datoEspecial);
            } else if (tipo.equals("NO_PERECEDERO")) {
                int mesesGarantia = Integer.parseInt(datoEspecial);
                return new ProductoNoPerecedero(codigo, nombre, precio, stock, mesesGarantia);
            }
        } catch (Exception e) {
            System.err.println("Error al procesar línea: " + linea + " - " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean agregarProducto(Producto producto) {
        if (buscarProducto(producto.getCodigo()) != null) {
            return false; 
        }
        productos.add(producto);
        guardarProductosEnArchivo(); 
        return true;
    }
    
    public boolean eliminarProducto(String codigo) {
        Producto producto = buscarProducto(codigo);
        if (producto != null) {
            productos.remove(producto);
            guardarProductosEnArchivo(); 
            return true;
        }
        return false;
    }
    
    public boolean editarProducto(String codigo, Producto productoNuevo) {
        Producto producto = buscarProducto(codigo);
        if (producto != null) {
            int indice = productos.indexOf(producto);
            productos.set(indice, productoNuevo);
            guardarProductosEnArchivo(); 
            return true;
        }
        return false;
    }
    
    public void reducirStock(String codigo, int cantidad) {
        Producto producto = buscarProducto(codigo);
        if (producto != null) {
            int nuevoStock = producto.getCantidadStock() - cantidad;
            producto.setCantidadStock(nuevoStock);
            guardarProductosEnArchivo();
        }
    }
        
    private void cargarDatosPrueba() {
        productos.add(new ProductoPerecedero("P001", "Leche Entera", 3500, 50, "15/03/2025"));
        productos.add(new ProductoPerecedero("P002", "Queso Campesino", 12000, 30, "20/03/2025"));
        productos.add(new ProductoNoPerecedero("NP001", "Arroz Diana 1kg", 4200, 100, 6));
        productos.add(new ProductoNoPerecedero("NP002", "Aceite Girasol", 8500, 60, 12));
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
            if (p instanceof ProductoPerecedero) {
                contador++;
            }
        }
        return contador;
    }
    
    public int contarProductosNoPerecederos() {
        int contador = 0;
        for (Producto p : productos) {
            if (p instanceof ProductoNoPerecedero) {
                contador++;
            }
        }
        return contador;
    }
    
    public void ordenarPorNombre() {
        productos.sort((p1, p2) -> p1.getNombre().compareTo(p2.getNombre()));
        guardarProductosEnArchivo(); 
    }
    
    public void ordenarPorPrecio() {
        productos.sort((p1, p2) -> Double.compare(p1.getPrecio(), p2.getPrecio()));
        guardarProductosEnArchivo(); 
    }
    
    public void ordenarPorStock() {
        productos.sort((p1, p2) -> Integer.compare(p1.getCantidadStock(), p2.getCantidadStock()));
        guardarProductosEnArchivo(); 
    }
}