package cl.duoc.lmcustomerms.repositories;

import cl.duoc.lmcustomerms.models.Cliente;
import cl.duoc.lmcustomerms.models.Direccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DireccionRepositoryTest {

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private TestEntityManager entityManager; //permite crear las entidades externas que tienen relación con la entidad bajo testeo en este repositorio. En este caso, Cliente es requerido para testear direcciones a nivel de BD. ESto permite mantener el test unitario en vez de importar ClienteRepository, lo que rompería la unicidad del test.

    private Cliente clienteTest;
    private Direccion direccionTest;

    @BeforeEach
    void setUp() {
        // 1. Preparamos un Cliente válido para cumplir las validaciones de la BD
        clienteTest = new Cliente();
        clienteTest.setNumrun(12345678);
        clienteTest.setDvrun("5");
        clienteTest.setPnombre("Juan");
        clienteTest.setAppaterno("Pérez");
        clienteTest.setApmaterno("Soto");
        clienteTest.setEmail("test@duocuc.cl");
        clienteTest.setFono("+56912345678");
        clienteTest.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        clienteTest.setFechaCreacion(new Date());
        clienteTest.setFechaActualizacion(new Date());

        // 2. Preparamos una Dirección válida
        direccionTest = new Direccion();
        direccionTest.setCalle("Av. Siempre Viva");
        direccionTest.setNumero(742);
        direccionTest.setNroDepto(1);
        direccionTest.setComuna("Springfield");
        direccionTest.setRegion("Metropolitana");
        direccionTest.setEsDefault(true);

        // 3. Vinculamos ambas entidades y las guardamos en la BD de prueba
        clienteTest.addDireccion(direccionTest);

        // Al guardar el cliente, el cascade = CascadeType.ALL guardará la dirección automáticamente
        entityManager.persistAndFlush(clienteTest);
    }

    // ==========================================
    // TESTS PARA LOS MÉTODOS DEL REPOSITORIO
    // ==========================================

    @Test
    @DisplayName("findByCalle - Debe retornar la dirección correcta")
    void findByCalle_ExistingCalle_ReturnsDireccion() {
        // Act
        Direccion result = direccionRepository.findByCalle("Av. Siempre Viva");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCalle()).isEqualTo("Av. Siempre Viva");
        assertThat(result.getComuna()).isEqualTo("Springfield");
    }

    @Test
    @DisplayName("findByCalle - Debe retornar null si no existe")
    void findByCalle_NonExistingCalle_ReturnsNull() {
        // Act
        Direccion result = direccionRepository.findByCalle("Calle Falsa 123");

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("exists... - Debe retornar true si coincide calle, numero y depto")
    void existsDireccion_ExistingData_ReturnsTrue() {
        // Act
        boolean exists = direccionRepository.existsDireccionByCalleAndNumeroAndNroDepto(
                "Av. Siempre Viva", 742, 1);

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("exists... - Debe retornar false si falta coincidencia")
    void existsDireccion_WrongData_ReturnsFalse() {
        // Act
        boolean exists = direccionRepository.existsDireccionByCalleAndNumeroAndNroDepto(
                "Av. Siempre Viva", 999, 1); // Número incorrecto

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("findAllByClienteId - Debe retornar la lista de direcciones del cliente")
    void findAllByClienteId_ExistingCliente_ReturnsList() {
        // Obtenemos el ID autogenerado que H2 le asignó al cliente en el BeforeEach
        Long idClienteGenerado = clienteTest.getId();

        // Act
        List<Direccion> resultados = direccionRepository.findAllByClienteId(idClienteGenerado);

        // Assert
        assertThat(resultados).isNotEmpty();
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getCalle()).isEqualTo("Av. Siempre Viva");
        assertThat(resultados.get(0).getCliente().getId()).isEqualTo(idClienteGenerado);
    }
}