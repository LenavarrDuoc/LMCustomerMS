package cl.duoc.lmcustomerms.services;

import cl.duoc.lmcustomerms.dtos.DireccionInputDTO;
import cl.duoc.lmcustomerms.dtos.DireccionResponseDTO;
import cl.duoc.lmcustomerms.dtos.DireccionUpdateDTO;
import cl.duoc.lmcustomerms.exceptions.DireccionNombreExisteException;
import cl.duoc.lmcustomerms.exceptions.IdNoExisteException;
import cl.duoc.lmcustomerms.mappers.DireccionInputMapper;
import cl.duoc.lmcustomerms.mappers.DireccionResponseMapper;
import cl.duoc.lmcustomerms.mappers.DireccionUpdateMapper;
import cl.duoc.lmcustomerms.models.Direccion;
import cl.duoc.lmcustomerms.repositories.DireccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DireccionServiceTest {

    @Mock
    private DireccionRepository direccionRepository;

    @Mock
    private DireccionResponseMapper direccionResponseMapper;

    @Mock
    private DireccionInputMapper direccionInputMapper;

    @Mock
    private DireccionUpdateMapper direccionUpdateMapper;

    // @InjectMocks inserta automáticamente los @Mock definidos arriba dentro del service
    @InjectMocks
    private DireccionService direccionService;

    // Variables globales para los tests
    private Direccion direccionEntity;
    private DireccionInputDTO inputDto;
    private DireccionUpdateDTO updateDto;
    private DireccionResponseDTO responseDto;

    @BeforeEach
    void setUp() {
        // Preparamos la Entidad Simulada
        direccionEntity = new Direccion();
        direccionEntity.setId(1L);
        direccionEntity.setCalle("Av. Siempre Viva");
        direccionEntity.setNumero(742);
        direccionEntity.setNroDepto(0);
        direccionEntity.setComuna("Springfield");
        direccionEntity.setRegion("Metropolitana");

        // Preparamos el InputDTO Simulado
        inputDto = new DireccionInputDTO();
        inputDto.setCalle("Av. Siempre Viva");
        inputDto.setNumero(742);
        inputDto.setNroDepto(0);
        inputDto.setComuna("Springfield");
        inputDto.setRegion("Metropolitana");

        // Preparamos el UpdateDTO Simulado
        updateDto = new DireccionUpdateDTO();
        updateDto.setId(1L);
        updateDto.setCalle("Av. Siempre Viva Modificada");
        updateDto.setNumero(742);
        updateDto.setNroDepto(0);

        // Preparamos el ResponseDTO Simulado
        responseDto = new DireccionResponseDTO();
        responseDto.setId(1L);
        responseDto.setCalle("Av. Siempre Viva");
        responseDto.setNumero(742);
        responseDto.setNroDepto(0);
        responseDto.setComuna("Springfield");
        responseDto.setRegion("Metropolitana");
    }

    // ==========================================
    // TESTS DE CREACIÓN (SAVE)
    // ==========================================

    @Test
    @DisplayName("save - Éxito: Guarda y retorna DTO cuando no existe duplicado")
    void save_ValidInput_ReturnsResponseDTO() {
        // Arrange
        when(direccionRepository.existsDireccionByCalleAndNumeroAndNroDepto(
                inputDto.getCalle(), inputDto.getNumero(), inputDto.getNroDepto())).thenReturn(false);
        when(direccionInputMapper.toEntity(any(DireccionInputDTO.class))).thenReturn(direccionEntity);
        when(direccionRepository.save(any(Direccion.class))).thenReturn(direccionEntity);
        when(direccionResponseMapper.toDto(any(Direccion.class))).thenReturn(responseDto);

        // Act
        DireccionResponseDTO result = direccionService.save(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals("Av. Siempre Viva", result.getCalle());
        verify(direccionRepository, times(1)).save(any(Direccion.class)); // Verifica que save() se llamó una vez
    }

    @Test
    @DisplayName("save - Falla: Lanza excepción si la dirección ya existe")
    void save_ExistingDireccion_ThrowsDireccionNombreExisteException() {
        // Arrange
        when(direccionRepository.existsDireccionByCalleAndNumeroAndNroDepto(
                inputDto.getCalle(), inputDto.getNumero(), inputDto.getNroDepto())).thenReturn(true);

        // Act & Assert
        assertThrows(DireccionNombreExisteException.class, () -> direccionService.save(inputDto));
        verify(direccionRepository, never()).save(any(Direccion.class)); // Verifica que NO se llamó al guardado
    }

    @Test
    @DisplayName("save - Éxito: Maneja NroDepto nulo convirtiéndolo a 0 en la validación")
    void save_NullNroDepto_ConvertsToZero() {
        // Arrange
        inputDto.setNroDepto(null); // Forzamos nulo

        // Esperamos que el mock reciba 0 como argumento de depto gracias a la lógica de tu service
        when(direccionRepository.existsDireccionByCalleAndNumeroAndNroDepto(
                inputDto.getCalle(), inputDto.getNumero(), 0)).thenReturn(false);
        when(direccionInputMapper.toEntity(any())).thenReturn(direccionEntity);
        when(direccionRepository.save(any())).thenReturn(direccionEntity);
        when(direccionResponseMapper.toDto(any())).thenReturn(responseDto);

        // Act
        DireccionResponseDTO result = direccionService.save(inputDto);

        // Assert
        assertNotNull(result);
        verify(direccionRepository).existsDireccionByCalleAndNumeroAndNroDepto(inputDto.getCalle(), inputDto.getNumero(), 0);
    }

    // ==========================================
    // TESTS DE LECTURA (GET)
    // ==========================================

    @Test
    @DisplayName("findAll - Retorna lista de DTOs")
    void findAll_ReturnsListOfResponseDTOs() {
        // Arrange
        when(direccionRepository.findAll()).thenReturn(List.of(direccionEntity));
        when(direccionResponseMapper.toDto(any(Direccion.class))).thenReturn(responseDto);

        // Act
        List<DireccionResponseDTO> result = direccionService.findAll();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Av. Siempre Viva", result.get(0).getCalle());
    }

    @Test
    @DisplayName("findAllDirecciones - Retorna lista filtrada por ID de cliente")
    void findAllDirecciones_ByClienteId_ReturnsList() {
        // Arrange
        Long clienteId = 99L;
        when(direccionRepository.findAllByClienteId(clienteId)).thenReturn(List.of(direccionEntity));
        when(direccionResponseMapper.toDto(any(Direccion.class))).thenReturn(responseDto);

        // Act
        List<DireccionResponseDTO> result = direccionService.findAllDirecciones(clienteId);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findById - Retorna DTO si existe")
    void findById_ExistingId_ReturnsResponseDTO() {
        // Arrange
        when(direccionRepository.findById(1L)).thenReturn(Optional.of(direccionEntity));
        when(direccionResponseMapper.toDto(any(Direccion.class))).thenReturn(responseDto);

        // Act
        DireccionResponseDTO result = direccionService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("findById - Retorna null si no existe")
    void findById_NonExistingId_ReturnsNull() {
        // Arrange
        when(direccionRepository.findById(99L)).thenReturn(Optional.empty());
        // Tu servicio llama al mapper incluso si es null. Mockeamos ese comportamiento.
        when(direccionResponseMapper.toDto(null)).thenReturn(null);

        // Act
        DireccionResponseDTO result = direccionService.findById(99L);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("findByCalle - Retorna DTO si existe")
    void findByCalle_ExistingCalle_ReturnsResponseDTO() {
        // Arrange
        when(direccionRepository.findByCalle("Av. Siempre Viva")).thenReturn(direccionEntity);
        when(direccionResponseMapper.toDto(any(Direccion.class))).thenReturn(responseDto);

        // Act
        DireccionResponseDTO result = direccionService.findByCalle("Av. Siempre Viva");

        // Assert
        assertNotNull(result);
        assertEquals("Av. Siempre Viva", result.getCalle());
    }

    // ==========================================
    // TESTS DE ACTUALIZACIÓN (UPDATE)
    // ==========================================

    @Test
    @DisplayName("update - Éxito: Actualiza y retorna DTO")
    void update_ExistingId_ReturnsUpdatedDTO() {
        // Arrange
        when(direccionRepository.existsById(updateDto.getId())).thenReturn(true);
        when(direccionUpdateMapper.toEntity(any(DireccionUpdateDTO.class))).thenReturn(direccionEntity);
        when(direccionRepository.save(any(Direccion.class))).thenReturn(direccionEntity);
        when(direccionResponseMapper.toDto(any(Direccion.class))).thenReturn(responseDto);

        // Act
        DireccionResponseDTO result = direccionService.update(updateDto);

        // Assert
        assertNotNull(result);
        verify(direccionRepository, times(1)).save(any(Direccion.class));
    }

    @Test
    @DisplayName("update - Falla: Lanza excepción si el ID no existe")
    void update_NonExistingId_ThrowsIdNoExisteException() {
        // Arrange
        when(direccionRepository.existsById(updateDto.getId())).thenReturn(false);

        // Act & Assert
        assertThrows(IdNoExisteException.class, () -> direccionService.update(updateDto));
        verify(direccionRepository, never()).save(any(Direccion.class));
    }

    // ==========================================
    // TESTS DE ELIMINACIÓN (DELETE)
    // ==========================================

    @Test
    @DisplayName("deleteDireccionById - Retorna true si existe y lo elimina")
    void deleteDireccionById_ExistingId_ReturnsTrue() {
        // Arrange
        when(direccionRepository.existsById(1L)).thenReturn(true);

        // Act
        Boolean result = direccionService.deleteDireccionById(1L);

        // Assert
        assertTrue(result);
        verify(direccionRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteDireccionById - Retorna false si no existe")
    void deleteDireccionById_NonExistingId_ReturnsFalse() {
        // Arrange
        when(direccionRepository.existsById(99L)).thenReturn(false);

        // Act
        Boolean result = direccionService.deleteDireccionById(99L);

        // Assert
        assertFalse(result);
        verify(direccionRepository, never()).deleteById(anyLong()); // Verifica que NO se llamó al delete
    }
}