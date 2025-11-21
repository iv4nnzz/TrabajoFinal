package controlador;

import modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ControladorInventarioTest {
    
    private ControladorInventario controlador;
    private Producto producto1;
    private Producto producto2;
    private Producto producto3;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        System.setProperty("user.dir", tempDir.toString());
        
        controlador = new ControladorInventario();
        
        producto1 = new ProductoPerecedero(
            "P001", "Leche Entera", 3500.0, 50, "15/03/2025"
        );
        
        producto2 = new ProductoNoPerecedero(
            "NP001", "Arroz Diana 1kg", 4200.0, 100, 12
        );
        
        producto3 = new ProductoPerecedero(
            "P002", "Yogurt Natural", 2500.0, 5, "20/03/2025"
        );
    }
    
    @AfterEach
    void tearDown() {
        File archivoPrueba = new File(tempDir.toFile(), "productos.txt");
        if (archivoPrueba.exists()) {
            archivoPrueba.delete();
        }
    }
    
    @Test
    @DisplayName("Agregar - Producto nuevo exitosamente")
    void testAgregarProductoNuevo() {
        boolean resultado = controlador.agregarProducto(producto1);
        
        assertTrue(resultado);
        assertEquals(1, controlador.obtenerTodos().size());
        assertNotNull(controlador.buscarProducto("P001"));
    }
    
    @Test
    @DisplayName("Agregar - Múltiples productos")
    void testAgregarMultiplesProductos() {
        controlador.agregarProducto(producto1);
        controlador.agregarProducto(producto2);
        controlador.agregarProducto(producto3);
        
        assertEquals(3, controlador.obtenerTodos().size());
    }
    
    @Test
    @DisplayName("Agregar - Producto con código duplicado")
    void testAgregarProductoCodigoDuplicado() {
        controlador.agregarProducto(producto1);
        
        Producto productoDuplicado = new ProductoPerecedero(
            "P001", "Otro Producto", 5000.0, 20, "01/01/2026"
        );
        
        boolean resultado = controlador.agregarProducto(productoDuplicado);
        
        assertFalse(resultado);
        assertEquals(1, controlador.obtenerTodos().size());
    }
    
    @Test
    @DisplayName("Agregar - Producto perecedero")
    void testAgregarProductoPerecedero() {
        assertTrue(controlador.agregarProducto(producto1));
        assertEquals(1, controlador.contarProductosPerecederos());
        assertEquals(0, controlador.contarProductosNoPerecederos());
    }
    
    @Test
    @DisplayName("Agregar - Producto no perecedero")
    void testAgregarProductoNoPerecedero() {
        assertTrue(controlador.agregarProducto(producto2));
        assertEquals(0, controlador.contarProductosPerecederos());
        assertEquals(1, controlador.contarProductosNoPerecederos());
    }
    
    @Test
    @DisplayName("Buscar - Producto existente por código")
    void testBuscarProductoExistente() {
        controlador.agregarProducto(producto1);
        
        Producto encontrado = controlador.buscarProducto("P001");
        
        assertNotNull(encontrado);
        assertEquals("P001", encontrado.getCodigo());
        assertEquals("Leche Entera", encontrado.getNombre());
    }
    
    @Test
    @DisplayName("Buscar - Producto no existente")
    void testBuscarProductoNoExistente() {
        Producto encontrado = controlador.buscarProducto("NOEXISTE");
        assertNull(encontrado);
    }
    
    @Test
    @DisplayName("Buscar - Por nombre (búsqueda parcial)")
    void testBuscarProductoPorNombre() {
        controlador.agregarProducto(producto1);
        controlador.agregarProducto(producto2);
        controlador.agregarProducto(producto3);
        
        ArrayList<Producto> resultados = controlador.buscarProducto("leche", true);
        
        assertEquals(1, resultados.size());
        assertEquals("Leche Entera", resultados.get(0).getNombre());
    }
    
    @Test
    @DisplayName("Buscar - Por nombre sin resultados")
    void testBuscarProductoPorNombreSinResultados() {
        controlador.agregarProducto(producto1);
        
        ArrayList<Producto> resultados = controlador.buscarProducto("chocolate", true);
        
        assertTrue(resultados.isEmpty());
    }
    
    @Test
    @DisplayName("Buscar - Por rango de precio")
    void testBuscarProductoPorRangoPrecio() {
        controlador.agregarProducto(producto1);
        controlador.agregarProducto(producto2); 
        controlador.agregarProducto(producto3); 
        
        ArrayList<Producto> resultados = controlador.buscarProducto(3000.0, 4000.0);
        
        assertEquals(1, resultados.size());
        assertEquals("P001", resultados.get(0).getCodigo());
    }
    
    @Test
    @DisplayName("Buscar - Todos los productos")
    void testObtenerTodosLosProductos() {
        controlador.agregarProducto(producto1);
        controlador.agregarProducto(producto2);
        
        ArrayList<Producto> todos = controlador.obtenerTodos();
        
        assertEquals(2, todos.size());
    }
    
    @Test
    @DisplayName("Editar - Producto existente")
    void testEditarProductoExistente() {
        controlador.agregarProducto(producto1);
        
        Producto productoEditado = new ProductoPerecedero(
            "P001", "Leche Deslactosada", 4000.0, 60, "20/03/2025"
        );
        
        boolean resultado = controlador.editarProducto("P001", productoEditado);
        
        assertTrue(resultado);
        
        Producto productoActualizado = controlador.buscarProducto("P001");
        assertEquals("Leche Deslactosada", productoActualizado.getNombre());
        assertEquals(4000.0, productoActualizado.getPrecio());
        assertEquals(60, productoActualizado.getCantidadStock());
    }
    
    @Test
    @DisplayName("Editar - Producto no existente")
    void testEditarProductoNoExistente() {
        Producto productoNuevo = new ProductoPerecedero(
            "P999", "Producto Inexistente", 1000.0, 10, "01/01/2026"
        );
        
        boolean resultado = controlador.editarProducto("P999", productoNuevo);
        
        assertFalse(resultado);
    }
    
    @Test
    @DisplayName("Editar - Cambiar tipo de producto (Perecedero a No Perecedero)")
    void testEditarCambiarTipoProducto() {
        controlador.agregarProducto(producto1); 
        
        Producto productoNoPerecedero = new ProductoNoPerecedero(
            "P001", "Arroz", 5000.0, 100, 12
        );
        
        boolean resultado = controlador.editarProducto("P001", productoNoPerecedero);
        
        assertTrue(resultado);
        
        Producto actualizado = controlador.buscarProducto("P001");
        assertTrue(actualizado instanceof ProductoNoPerecedero);
    }
    
    @Test
    @DisplayName("Eliminar - Producto existente")
    void testEliminarProductoExistente() {
        controlador.agregarProducto(producto1);
        controlador.agregarProducto(producto2);
        
        boolean resultado = controlador.eliminarProducto("P001");
        
        assertTrue(resultado);
        assertEquals(1, controlador.obtenerTodos().size());
        assertNull(controlador.buscarProducto("P001"));
    }
    
    @Test
    @DisplayName("Eliminar - Producto no existente")
    void testEliminarProductoNoExistente() {
        boolean resultado = controlador.eliminarProducto("NOEXISTE");
        assertFalse(resultado);
    }
    
    @Test
    @DisplayName("Eliminar - Todos los productos uno por uno")
    void testEliminarTodosLosProductos() {
        controlador.agregarProducto(producto1);
        controlador.agregarProducto(producto2);
        controlador.agregarProducto(producto3);
        
        controlador.eliminarProducto("P001");
        controlador.eliminarProducto("NP001");
        controlador.eliminarProducto("P002");
        
        assertTrue(controlador.obtenerTodos().isEmpty());
    }

    @Test
    @DisplayName("Stock - Verificar stock suficiente")
    void testVerificarStockSuficiente() {
        controlador.agregarProducto(producto1); 
        
        assertTrue(controlador.verificarStock("P001", 30));
        assertTrue(controlador.verificarStock("P001", 50));
    }
    
    @Test
    @DisplayName("Stock - Verificar stock insuficiente")
    void testVerificarStockInsuficiente() {
        // Arrange
        controlador.agregarProducto(producto1); 
        
        // Act & Assert
        assertFalse(controlador.verificarStock("P001", 51));
        assertFalse(controlador.verificarStock("P001", 100));
    }
    
    @Test
    @DisplayName("Stock - Reducir stock")
    void testReducirStock() {
        controlador.agregarProducto(producto1); 
        
        controlador.reducirStock("P001", 20);
        
        Producto producto = controlador.buscarProducto("P001");
        assertEquals(30, producto.getCantidadStock());
    }
    
    @Test
    @DisplayName("Stock - Reducir stock a cero")
    void testReducirStockACero() {
        controlador.agregarProducto(producto1);
        
        controlador.reducirStock("P001", 50);
        
        assertEquals(0, controlador.buscarProducto("P001").getCantidadStock());
    }
    
    @Test
    @DisplayName("Stock - Productos con stock bajo")
    void testObtenerProductosStockBajo() {
        controlador.agregarProducto(producto1); 
        controlador.agregarProducto(producto2); 
        controlador.agregarProducto(producto3);
        
        ArrayList<Producto> bajoStock = controlador.obtenerProductosBajoStock();
        
        assertEquals(1, bajoStock.size());
        assertEquals("P002", bajoStock.get(0).getCodigo());
    }
    
    @Test
    @DisplayName("Stock - Ningún producto con stock bajo")
    void testNingunProductoStockBajo() {
        controlador.agregarProducto(producto1);
        controlador.agregarProducto(producto2); 
        
        ArrayList<Producto> bajoStock = controlador.obtenerProductosBajoStock();
        
        assertTrue(bajoStock.isEmpty());
    }
    
    @Test
    @DisplayName("Estadísticas - Calcular valor total de inventario")
    void testCalcularValorTotalInventario() {
        controlador.agregarProducto(producto1);
        controlador.agregarProducto(producto2); 
        
        double valorTotal = controlador.calcularValorTotalInventario();
        
        assertEquals(637000.0, valorTotal, 0.01);
    }
    
    @Test
    @DisplayName("Estadísticas - Inventario vacío tiene valor cero")
    void testInventarioVacioValorCero() {
        double valorTotal = controlador.calcularValorTotalInventario();
        assertEquals(0.0, valorTotal, 0.01);
    }
    
    @Test
    @DisplayName("Estadísticas - Contar productos perecederos")
    void testContarProductosPerecederos() {
        // Arrange
        controlador.agregarProducto(producto1); 
        controlador.agregarProducto(producto2); 
        controlador.agregarProducto(producto3); 
        
        int cantidad = controlador.contarProductosPerecederos();
        
        assertEquals(2, cantidad);
    }
    
    @Test
    @DisplayName("Estadísticas - Contar productos no perecederos")
    void testContarProductosNoPerecederos() {
        controlador.agregarProducto(producto1);
        controlador.agregarProducto(producto2); 
        
        int cantidad = controlador.contarProductosNoPerecederos();
        
        assertEquals(1, cantidad);
    }
    
    @Test
    @DisplayName("Ordenar - Por nombre alfabéticamente")
    void testOrdenarPorNombre() {
        controlador.agregarProducto(producto3); 
        controlador.agregarProducto(producto1); 
        controlador.agregarProducto(producto2); 
        
        controlador.ordenarPorNombre();
        
        ArrayList<Producto> productos = controlador.obtenerTodos();
        assertEquals("Arroz Diana 1kg", productos.get(0).getNombre());
        assertEquals("Leche Entera", productos.get(1).getNombre());
        assertEquals("Yogurt Natural", productos.get(2).getNombre());
    }
    
    @Test
    @DisplayName("Ordenar - Por precio de menor a mayor")
    void testOrdenarPorPrecio() {
        controlador.agregarProducto(producto2); 
        controlador.agregarProducto(producto1);
        controlador.agregarProducto(producto3); 
        
        controlador.ordenarPorPrecio();
        
        ArrayList<Producto> productos = controlador.obtenerTodos();
        assertEquals(2500.0, productos.get(0).getPrecio());
        assertEquals(3500.0, productos.get(1).getPrecio());
        assertEquals(4200.0, productos.get(2).getPrecio());
    }
    
    @Test
    @DisplayName("Ordenar - Por stock de menor a mayor")
    void testOrdenarPorStock() {
        controlador.agregarProducto(producto2);
        controlador.agregarProducto(producto1);
        controlador.agregarProducto(producto3); 
        
        controlador.ordenarPorStock();
        
        ArrayList<Producto> productos = controlador.obtenerTodos();
        assertEquals(5, productos.get(0).getCantidadStock());
        assertEquals(50, productos.get(1).getCantidadStock());
        assertEquals(100, productos.get(2).getCantidadStock());
    }
    
    @Test
    @DisplayName("Caso extremo - Reducir más stock del disponible")
    void testReducirMasStockDelDisponible() {
        controlador.agregarProducto(producto1); // Stock: 50
        
        controlador.reducirStock("P001", 100);
        
        Producto producto = controlador.buscarProducto("P001");
        assertTrue(producto.getCantidadStock() >= -50);
    }
    
    @Test
    @DisplayName("Caso extremo - Buscar con código null")
    void testBuscarConCodigoNull() {
        Producto resultado = controlador.buscarProducto(null);
        assertNull(resultado);
    }
    
    @Test
    @DisplayName("Caso extremo - Buscar por nombre vacío")
    void testBuscarPorNombreVacio() {
        controlador.agregarProducto(producto1);
        
        ArrayList<Producto> resultados = controlador.buscarProducto("", true);
        
        assertEquals(1, resultados.size());
    }
    
    @Test
    @DisplayName("Caso extremo - Rango de precio inválido")
    void testBuscarConRangoPrecioInvalido() {
        controlador.agregarProducto(producto1);
        
        ArrayList<Producto> resultados = controlador.buscarProducto(5000.0, 1000.0);
        
        assertTrue(resultados.isEmpty());
    }
}