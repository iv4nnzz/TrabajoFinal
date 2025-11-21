package controlador;

import modelo.Producto;
import modelo.ProductoNoPerecedero;
import modelo.ProductoPerecedero;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ControladorInventarioIT {

    private static final Path ARCHIVO_PRODUCTOS = Path.of("productos.txt");

    @BeforeEach
    void setUp() throws Exception {
        Files.deleteIfExists(ARCHIVO_PRODUCTOS);
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(ARCHIVO_PRODUCTOS);
    }

    @Test
    void integration_agregarReducirEliminar_y_persistencia() throws Exception {
        ControladorInventario controlador = new ControladorInventario();

        Producto existente = controlador.buscarProducto("P001");
        assertNotNull(existente, "Se espera que el producto P001 exista en los datos de prueba");

        Producto nuevo = new ProductoNoPerecedero("TST-PROD", "Producto Test", 1500.0, 10, 6);
        boolean agregado = controlador.agregarProducto(nuevo);
        assertTrue(agregado, "El producto nuevo debe agregarse correctamente");
        Producto buscado = controlador.buscarProducto("TST-PROD");
        assertNotNull(buscado, "El producto agregado debe encontrarse en inventario");
        assertEquals(10, buscado.getCantidadStock(), "Stock inicial debe ser 10");

        controlador.reducirStock("TST-PROD", 3);
        Producto despuésReducir = controlador.buscarProducto("TST-PROD");
        assertNotNull(despuésReducir);
        assertEquals(7, despuésReducir.getCantidadStock(), "El stock debe haberse reducido en 3 unidades");

        ControladorInventario controladorRecargado = new ControladorInventario();
        Producto recargado = controladorRecargado.buscarProducto("TST-PROD");
        assertNotNull(recargado, "El producto debe persistir en el archivo y recuperarse al recargar el controlador");
        assertEquals(7, recargado.getCantidadStock(), "El stock persistido debe ser 7");

        boolean eliminado = controladorRecargado.eliminarProducto("TST-PROD");
        assertTrue(eliminado, "El producto debe eliminarse correctamente");
        assertNull(controladorRecargado.buscarProducto("TST-PROD"), "El producto ya no debe estar en inventario");

        ControladorInventario controladorFinal = new ControladorInventario();
        assertNull(controladorFinal.buscarProducto("TST-PROD"), "Después de eliminar y recargar, el producto no debe existir");
    }
}
