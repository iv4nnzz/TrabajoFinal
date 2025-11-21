/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package controlador;

import modelo.Cliente;
import modelo.Factura;
import modelo.ItemFactura;
import modelo.ProductoPerecedero;
import modelo.ProductoNoPerecedero;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Paths;

class ControladorFacturacionTest {

    private static final String ARCH_FACTURAS = "facturas.txt";
    private static final String ARCH_CONTADOR = "contador_facturas.txt";

    @BeforeEach
    void limpiarArchivos() throws Exception {
        Files.deleteIfExists(Paths.get(ARCH_FACTURAS));
        Files.deleteIfExists(Paths.get(ARCH_CONTADOR));
    }

    @AfterEach
    void limpiarDespues() throws Exception {
        Files.deleteIfExists(Paths.get(ARCH_FACTURAS));
        Files.deleteIfExists(Paths.get(ARCH_CONTADOR));
    }

    @Test
    void testGenerarNumeroFactura_incrementeCorrectamente() {
        ControladorFacturacion cf = new ControladorFacturacion(null); // no necesitamos inventario aquí

        Factura f1 = cf.crearFactura(new Cliente("111", "Cliente Uno", "3000000000"));
        Factura f2 = cf.crearFactura(new Cliente("222", "Cliente Dos", "3111111111"));

        assertEquals("FAC-0001", f1.getNumero(), "Primer número de factura esperado FAC-0001");
        assertEquals("FAC-0002", f2.getNumero(), "Segundo número de factura esperado FAC-0002");
    }

    @Test
    void testAgregarFactura_y_calcularTotalVentas() {
        ControladorFacturacion cf = new ControladorFacturacion(null);
        Cliente cliente = new Cliente("123", "Carlos Pérez", "3100000000");
        Factura factura = cf.crearFactura(cliente);

        ProductoPerecedero p1 = new ProductoPerecedero("P01", "Café Molido", 1000.0, 10, "01/01/2026");
        ItemFactura item1 = new ItemFactura(p1, 2);

        ProductoNoPerecedero p2 = new ProductoNoPerecedero("NP01", "Azúcar 1kg", 1500.0, 20, 12);
        ItemFactura item2 = new ItemFactura(p2, 1);

        factura.agregarItem(item1);
        factura.agregarItem(item2);

        cf.agregarFactura(factura);

        assertEquals(1, cf.obtenerCantidadFacturas(), "Debe haber 1 factura en el controlador");
        assertNotNull(cf.buscarFactura(factura.getNumero()), "Debe encontrar la factura agregada por número");
        assertEquals(3500.0, cf.calcularTotalVentas(), 1e-6, "El total de ventas debe ser 3500.0");
    }
}
