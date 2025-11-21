/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FacturaTest {

    @Test
    void testAgregarItemYCalcularTotal() {
        Cliente cliente = new Cliente("12345678", "Juan Perez", "3001234567");
        Factura factura = new Factura("FAC-0001", cliente, "21/11/2025");

        Producto p1 = new ProductoPerecedero("P001", "Leche Entera", 3500.0, 50, "15/03/2025");
        ItemFactura item1 = new ItemFactura(p1, 2); 

        Producto p2 = new ProductoNoPerecedero("NP001", "Arroz 1kg", 4200.0, 100, 6);
        ItemFactura item2 = new ItemFactura(p2, 1); 

        factura.agregarItem(item1);
        assertEquals(7000.0, factura.getTotal(), 1e-6, "Total tras primer item incorrecto");
        assertEquals(1, factura.getItems().size(), "La factura debe tener 1 item");

        factura.agregarItem(item2);
        assertEquals(11200.0, factura.getTotal(), 1e-6, "Total tras segundo item incorrecto");
        assertEquals(2, factura.getItems().size(), "La factura debe tener 2 items");
    }

    @Test
    void testToStringContieneDatosEsperados() {
        Cliente cliente = new Cliente("87654321", "Ana López", "3107654321");
        Factura factura = new Factura("FAC-0002", cliente, "21/11/2025");

        Producto p = new ProductoPerecedero("P002", "Queso Campesino", 12000.0, 30, "20/03/2025");
        factura.agregarItem(new ItemFactura(p, 1)); 

        String texto = factura.toString();

        assertTrue(texto.contains("FAC-0002"), "toString debe contener el número de factura");
        assertTrue(texto.contains("Ana López") || texto.contains("Ana Lopez"), "toString debe contener el nombre del cliente");
        assertTrue(texto.contains("Queso Campesino"), "toString debe listar el producto");
        assertTrue(texto.contains("TOTAL: $12000.00"), "toString debe mostrar el total formateado con 2 decimales");
    }
}
