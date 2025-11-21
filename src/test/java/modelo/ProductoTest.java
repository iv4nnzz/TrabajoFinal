/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {
    
    private ProductoPerecedero productoPerecedero;
    private ProductoNoPerecedero productoNoPerecedero;
    
    @BeforeEach
    void setUp() {
        productoPerecedero = new ProductoPerecedero(
            "P001", 
            "Leche Entera", 
            3500.0, 
            50, 
            "15/03/2025"
        );
        
        productoNoPerecedero = new ProductoNoPerecedero(
            "NP001", 
            "Arroz Diana 1kg", 
            4200.0, 
            100, 
            18  
        );
    }
    
    @Test
    @DisplayName("ProductoPerecedero - Verificar atributos del constructor")
    void testProductoPerecederoConstructor() {
        assertEquals("P001", productoPerecedero.getCodigo());
        assertEquals("Leche Entera", productoPerecedero.getNombre());
        assertEquals(3500.0, productoPerecedero.getPrecio());
        assertEquals(50, productoPerecedero.getCantidadStock());
        assertEquals("15/03/2025", productoPerecedero.getFechaVencimiento());
    }
    
    @Test
    @DisplayName("ProductoPerecedero - Calcular valor de inventario")
    void testProductoPerecederoCalcularValorInventario() {
        double valorEsperado = 3500.0 * 50; 
        double valorCalculado = productoPerecedero.calcularValorInventario();
        
        assertEquals(valorEsperado, valorCalculado, 0.01);
    }
    
    @Test
    @DisplayName("ProductoPerecedero - Aplicar descuento con 5% extra")
    void testProductoPerecederoAplicarDescuento() {
        double precioConDescuento = productoPerecedero.aplicarDescuento(10.0);
        
        assertEquals(2975.0, precioConDescuento, 0.01);
    }
    
    @Test
    @DisplayName("ProductoPerecedero - Verificar stock bajo (< 10)")
    void testProductoPerecederoStockBajo() {
        ProductoPerecedero productoStockBajo = new ProductoPerecedero(
            "P002", "Yogurt", 2000.0, 5, "20/03/2025"
        );
        
        assertTrue(productoStockBajo.tieneBajoStock());
        assertFalse(productoPerecedero.tieneBajoStock());
    }
    
    @Test
    @DisplayName("ProductoPerecedero - ToString contiene información completa")
    void testProductoPerecederoToString() {
        String resultado = productoPerecedero.toString();
        
        assertTrue(resultado.contains("P001"));
        assertTrue(resultado.contains("Leche Entera"));
        assertTrue(resultado.contains("3500"));
        assertTrue(resultado.contains("50"));
        assertTrue(resultado.contains("15/03/2025"));
        assertTrue(resultado.contains("PERECEDERO"));
    }
    
    @Test
    @DisplayName("ProductoPerecedero - Modificar fecha de vencimiento")
    void testProductoPerecederoSetFechaVencimiento() {
        productoPerecedero.setFechaVencimiento("20/04/2025");
        
        assertEquals("20/04/2025", productoPerecedero.getFechaVencimiento());
    }
    
    @Test
    @DisplayName("ProductoNoPerecedero - Verificar atributos del constructor")
    void testProductoNoPerecederoConstructor() {
        assertEquals("NP001", productoNoPerecedero.getCodigo());
        assertEquals("Arroz Diana 1kg", productoNoPerecedero.getNombre());
        assertEquals(4200.0, productoNoPerecedero.getPrecio());
        assertEquals(100, productoNoPerecedero.getCantidadStock());
        assertEquals(18, productoNoPerecedero.getMesesGarantia());
    }
    
    @Test
    @DisplayName("ProductoNoPerecedero - Calcular valor con garantía extendida (>12 meses)")
    void testProductoNoPerecederoCalcularValorConGarantiaExtendida() {
        double valorBase = 4200.0 * 100;
        double valorEsperado = valorBase * 1.10; 
        double valorCalculado = productoNoPerecedero.calcularValorInventario();
        
        assertEquals(valorEsperado, valorCalculado, 0.01);
    }
    
    @Test
    @DisplayName("ProductoNoPerecedero - Calcular valor sin garantía extendida (<=12 meses)")
    void testProductoNoPerecederoCalcularValorSinGarantiaExtendida() {
        ProductoNoPerecedero productoGarantiaCorta = new ProductoNoPerecedero(
            "NP002", "Aceite", 8500.0, 60, 6
        );
        
        double valorEsperado = 8500.0 * 60; 
        double valorCalculado = productoGarantiaCorta.calcularValorInventario();
        
        assertEquals(valorEsperado, valorCalculado, 0.01);
    }
    
    @Test
    @DisplayName("ProductoNoPerecedero - Aplicar descuento con garantía extendida")
    void testProductoNoPerecederoAplicarDescuentoGarantiaExtendida() {
        double precioConDescuento = productoNoPerecedero.aplicarDescuento(10.0);
        
        assertEquals(3990.0, precioConDescuento, 0.01);
    }
    
    @Test
    @DisplayName("ProductoNoPerecedero - Aplicar descuento sin garantía extendida")
    void testProductoNoPerecederoAplicarDescuentoSinGarantiaExtendida() {
        ProductoNoPerecedero productoGarantiaCorta = new ProductoNoPerecedero(
            "NP002", "Aceite", 8500.0, 60, 6
        );
        
        double precioConDescuento = productoGarantiaCorta.aplicarDescuento(10.0);
        
        assertEquals(7650.0, precioConDescuento, 0.01);
    }
    
    @Test
    @DisplayName("ProductoNoPerecedero - ToString contiene información completa")
    void testProductoNoPerecederoToString() {
        String resultado = productoNoPerecedero.toString();
        
        assertTrue(resultado.contains("NP001"));
        assertTrue(resultado.contains("Arroz Diana 1kg"));
        assertTrue(resultado.contains("4200"));
        assertTrue(resultado.contains("100"));
        assertTrue(resultado.contains("18"));
        assertTrue(resultado.contains("NO PERECEDERO"));
    }
    
    @Test
    @DisplayName("ProductoNoPerecedero - Modificar meses de garantía")
    void testProductoNoPerecederoSetMesesGarantia() {
        productoNoPerecedero.setMesesGarantia(24);
        
        assertEquals(24, productoNoPerecedero.getMesesGarantia());
    }
    
    @Test
    @DisplayName("Producto - Modificar código")
    void testProductoSetCodigo() {
        productoPerecedero.setCodigo("P999");
        assertEquals("P999", productoPerecedero.getCodigo());
    }
    
    @Test
    @DisplayName("Producto - Modificar nombre")
    void testProductoSetNombre() {
        productoPerecedero.setNombre("Leche Deslactosada");
        assertEquals("Leche Deslactosada", productoPerecedero.getNombre());
    }
    
    @Test
    @DisplayName("Producto - Modificar precio")
    void testProductoSetPrecio() {
        productoPerecedero.setPrecio(4000.0);
        assertEquals(4000.0, productoPerecedero.getPrecio());
    }
    
    @Test
    @DisplayName("Producto - Modificar stock")
    void testProductoSetCantidadStock() {
        productoPerecedero.setCantidadStock(75);
        assertEquals(75, productoPerecedero.getCantidadStock());
    }
    
    @Test
    @DisplayName("Producto - Stock bajo en el límite (exactamente 10)")
    void testProductoStockBajoEnLimite() {
        productoPerecedero.setCantidadStock(10);
        assertFalse(productoPerecedero.tieneBajoStock());
        
        productoPerecedero.setCantidadStock(9);
        assertTrue(productoPerecedero.tieneBajoStock());
    }
    
    @Test
    @DisplayName("Producto - Stock cero")
    void testProductoStockCero() {
        productoPerecedero.setCantidadStock(0);
        assertTrue(productoPerecedero.tieneBajoStock());
        assertEquals(0, productoPerecedero.getCantidadStock());
    }

    @Test
    @DisplayName("Producto - Precio cero")
    void testProductoPrecioCero() {
        ProductoPerecedero producto = new ProductoPerecedero(
            "P003", "Producto Gratis", 0.0, 10, "01/01/2026"
        );
        assertEquals(0.0, producto.getPrecio());
        assertEquals(0.0, producto.calcularValorInventario());
    }
    
    @Test
    @DisplayName("Producto - Descuento del 100%")
    void testProductoDescuentoCompleto() {
        double precioConDescuento = productoPerecedero.aplicarDescuento(95.0);
        assertEquals(0.0, precioConDescuento, 0.01);
    }
    
    @Test
    @DisplayName("Producto - Descuento del 0%")
    void testProductoSinDescuento() {
        double precioOriginal = productoPerecedero.getPrecio();
        double precioConDescuento = productoNoPerecedero.aplicarDescuento(0.0);
        
        assertEquals(productoNoPerecedero.getPrecio(), precioConDescuento, 0.01);
    }
    
    @Test
    @DisplayName("Polimorfismo - Producto como referencia")
    void testPolimorfismoProducto() {
        Producto producto1 = productoPerecedero;
        Producto producto2 = productoNoPerecedero;
        
        assertNotNull(producto1.calcularValorInventario());
        assertNotNull(producto2.calcularValorInventario());
        assertNotEquals(producto1.calcularValorInventario(), 
                       producto2.calcularValorInventario());
    }
    
    @Test
    @DisplayName("Polimorfismo - Descuentos diferentes según tipo")
    void testPolimorfismoDescuentos() {
        double porcentajeDescuento = 10.0;
        
        double descuentoPerecedero = productoPerecedero.aplicarDescuento(porcentajeDescuento);
        double descuentoNoPerecedero = productoNoPerecedero.aplicarDescuento(porcentajeDescuento);
        
        assertNotEquals(descuentoPerecedero / productoPerecedero.getPrecio(),
                       descuentoNoPerecedero / productoNoPerecedero.getPrecio());
    }
}