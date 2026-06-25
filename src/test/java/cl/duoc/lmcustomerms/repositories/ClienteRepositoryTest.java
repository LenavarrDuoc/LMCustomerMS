package cl.duoc.lmcustomerms.repositories;

import cl.duoc.lmcustomerms.models.Cliente;
import cl.duoc.lmcustomerms.models.Direccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//Se importó Dependencia de H2 Database. @DataJpaTest enviará todo testeo a esta base da datos temporal.
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente clienteTest;

    //Dejamos configurado una instanciación de cliente para cada prueba del repositorio.
    @BeforeEach
    void setUp() {
        //Como Flyway ingresa datos en la bd temporal de H2, borramos todo en la tabla clientes:
        //clienteRepository.deleteAll();

        //Creamos entidad a mano:
        clienteTest = new Cliente();
        clienteTest.setNumrun(12345678);
        clienteTest.setDvrun("k");
        clienteTest.setPnombre("Juan");
        clienteTest.setAppaterno("Pérez");
        clienteTest.setApmaterno("González");
        clienteTest.setEmail("juan@correo.cl");
        clienteTest.setFono("+56912345678");
        clienteTest.setFechaNacimiento(LocalDate.of(1995, 5, 20));
        Direccion direccionTest = new Direccion();
        direccionTest.setCalle("Av. Concha y Toro");
        direccionTest.setNumero(1340);
        direccionTest.setNroDepto(0);
        direccionTest.setComuna("Puente Alto");
        direccionTest.setRegion("Metropolitana");
        direccionTest.setEsDefault(true);

        //El modelo cliente tiene fijados sus métodos para agregar dirección dada desde el DTO y completar fecha de creación y modificación automáticamente al guardar o actualizar.
        clienteTest.addDireccion(direccionTest);
        clienteTest.setFechaCreacion(new Date());
        clienteTest.setFechaActualizacion(new Date());

        //Se guarda el clienteTest nuevamente mediante la capa repository para verificar
        clienteTest = clienteRepository.save(clienteTest);
    }

    @Test
    /*Verifica que la dirección ingresada al guardar un nuevo cliente
    sí se guarda en cascada en la tabla Dirección, y sí se asocia al cliente.*/
    void save_WithDireccion_MustPersistOnCascade() {
        //Corroboración de cliente:
        Cliente clienteResult = clienteRepository.findByNumrun(12345678);

        assertNotNull(clienteResult);
        assertFalse(clienteResult.getDirecciones().isEmpty(), "Lista de direcciones de cliente no debe estar vacía.");
        assertEquals(1, clienteResult.getDirecciones().size());

        //Corrobación de dirección:
        Direccion direccionResult = clienteResult.getDirecciones().get(0);
        assertEquals("Av. Concha y Toro", direccionResult.getCalle());

        //Corroboración de relación de claves entre cliente y dirección:
        assertNotNull(direccionResult.getCliente());
        assertEquals(clienteResult.getId(), direccionResult.getCliente().getId());

    }

    @Test
    void add_ExistingDireccion_Cliente_MustThrowException() {
        Direccion existingDireccion = new Direccion();
        existingDireccion.setCalle("Av. Concha y Toro");
        existingDireccion.setNumero(1340);
        existingDireccion.setNroDepto(0);
        existingDireccion.setComuna("Puente Alto");
        existingDireccion.setRegion("Metropolitana");

        clienteTest.addDireccion(existingDireccion);
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> clienteRepository.saveAndFlush(clienteTest));
    }

    @Test
    void save_ExistingNumrun_MustThrowException() {
        // Intentamos crear un nuevo cliente con el mismo numrun del clienteTest (12345678)
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNumrun(12345678);
        nuevoCliente.setDvrun("k");
        nuevoCliente.setPnombre("Pedro");
        nuevoCliente.setAppaterno("Tapia");
        nuevoCliente.setApmaterno("Soto");
        nuevoCliente.setEmail("pedro@correo.cl");
        nuevoCliente.setFono("+56987654321");
        nuevoCliente.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        nuevoCliente.setFechaCreacion(new Date());
        nuevoCliente.setFechaActualizacion(new Date());

        // Usamos saveAndFlush para forzar la sincronización con H2
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            clienteRepository.saveAndFlush(nuevoCliente);
        });
    }

    // Pruebas de encontrar a cliente con búsquedas generales y personalizadas:
    @Test
    void findAllByPnombre_ExistingName_ReturnsList() {
        // clienteTest ya se llama "Juan"
        List<Cliente> resultados = clienteRepository.findAllByPnombre("Juan");

        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
        assertEquals("Juan", resultados.get(0).getPnombre());
    }

    @Test
    void findByEmail_ExistingEmail_ReturnsCliente() {
        Cliente resultado = clienteRepository.findByEmail("juan@correo.cl");

        assertNotNull(resultado);
        assertEquals(12345678, resultado.getNumrun());
        assertEquals("Juan", resultado.getPnombre());

    }

    @Test
    void findByFono_ExistingFono_ReturnsCliente() {
        Cliente resultado = clienteRepository.findByFono("+56912345678");

        assertNotNull(resultado);
        assertEquals(12345678, resultado.getNumrun());
        assertEquals("Juan", resultado.getPnombre());
    }

    @Test
    void findByEmail_NonExistingEmail_ReturnsNull() {
        Cliente resultado = clienteRepository.findByEmail("no_existe@correo.cl");
        assertNull(resultado);
    }

    @Test
    void existsByNumrun_ExistingRun_ReturnsTrue() {
        boolean existe = clienteRepository.existsByNumrun(12345678);
        assertTrue(existe);
    }

    // Pruebas de existencia de cliente:
    @Test
    void existsByNumrun_NonExistingRun_ReturnsFalse() {
        boolean existe = clienteRepository.existsByNumrun(99999999);
        assertFalse(existe);
    }

    @Test
    void existsByEmail_ExistingEmail_ReturnsTrue() {
        boolean existe = clienteRepository.existsByEmail("juan@correo.cl");
        assertTrue(existe);
    }

    //Prueba de borrado de direcciones huérfanas (sugerido por IA). Una dirección quitada de una lista de clientes debería borrarse de la BD.
    @Test
    void removeDireccion_FromClienteList_MustDeleteFromDatabase() {
        // Obtenemos el cliente guardado en @BeforeEach
        Cliente cliente = clienteRepository.findByNumrun(12345678);

        // Removemos la única dirección que tiene
        cliente.getDirecciones().clear();

        // Guardamos los cambios
        clienteRepository.saveAndFlush(cliente);

        // Volvemos a buscar el cliente
        Cliente clienteActualizado = clienteRepository.findByNumrun(12345678);

        // Verificamos que la lista esté vacía. H2 habrá eliminado la fila en la tabla direccion.
        assertTrue(clienteActualizado.getDirecciones().isEmpty());
    }

}


