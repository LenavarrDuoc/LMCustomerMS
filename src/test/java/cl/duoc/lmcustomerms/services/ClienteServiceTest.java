package cl.duoc.lmcustomerms.services;

import cl.duoc.lmcustomerms.dtos.*;
import cl.duoc.lmcustomerms.exceptions.*;
import cl.duoc.lmcustomerms.mappers.*;
import cl.duoc.lmcustomerms.models.Cliente;
import cl.duoc.lmcustomerms.repositories.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteInputMapper clienteInputMapper;

    @Mock
    private ClienteResponseMapper clienteResponseMapper;

    @Mock
    private ClienteOrderResponseMapper clienteOrderResponseMapper;

    @Mock
    private ClienteUpdateMapper clienteUpdateMapper;

    @InjectMocks
    private ClienteService clienteService;

    // ==========================================
    // TESTS DE CREACIÓN (CREATE)
    // ==========================================

    @Test
    @DisplayName("Debe guardar cliente y retornar DTO cuando los datos son válidos")
    void save_ValidCliente_ReturnsClienteResponseDTO() {
        ClienteInputDTO inputDto = new ClienteInputDTO();
        inputDto.setNumrun(12345678);
        inputDto.setEmail("test@duocuc.cl");

        Cliente clienteEntity = new Cliente();
        Cliente savedEntity = new Cliente();
        ClienteResponseDTO expectedResponse = new ClienteResponseDTO();
        expectedResponse.setRun("12345678-5");

        when(clienteRepository.existsByNumrun(inputDto.getNumrun())).thenReturn(false);
        when(clienteRepository.existsByEmail(inputDto.getEmail())).thenReturn(false);
        when(clienteInputMapper.toEntity(inputDto)).thenReturn(clienteEntity);
        when(clienteRepository.save(clienteEntity)).thenReturn(savedEntity);
        when(clienteResponseMapper.toDto(savedEntity)).thenReturn(expectedResponse);

        ClienteResponseDTO result = clienteService.save(inputDto);

        assertNotNull(result);
        assertEquals("12345678-5", result.getRun());
        verify(clienteRepository, times(1)).save(clienteEntity);
    }

    @Test
    @DisplayName("Debe lanzar excepción al guardar si el RUN ya existe")
    void save_DuplicateNumrun_ThrowsClienteNumrunExisteException() {
        ClienteInputDTO inputDto = new ClienteInputDTO();
        inputDto.setNumrun(12345678);

        when(clienteRepository.existsByNumrun(inputDto.getNumrun())).thenReturn(true);

        assertThrows(ClienteNumrunExisteException.class, () -> clienteService.save(inputDto));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción al guardar si el Email ya existe")
    void save_DuplicateEmail_ThrowsClienteEmailExisteException() {
        ClienteInputDTO inputDto = new ClienteInputDTO();
        inputDto.setNumrun(12345678);
        inputDto.setEmail("test@duocuc.cl");

        when(clienteRepository.existsByNumrun(inputDto.getNumrun())).thenReturn(false);
        when(clienteRepository.existsByEmail(inputDto.getEmail())).thenReturn(true);

        assertThrows(ClienteEmailExisteException.class, () -> clienteService.save(inputDto));
        verify(clienteRepository, never()).save(any());
    }

    // ==========================================
    // TESTS DE LECTURA (READ)
    // ==========================================

    @Test
    @DisplayName("Debe retornar lista con todos los clientes")
    void findAll_ReturnsListOfClientes() {
        Cliente entity = new Cliente();
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setNombre("Juan Pérez");

        when(clienteRepository.findAll()).thenReturn(List.of(entity));
        when(clienteResponseMapper.toDto(entity)).thenReturn(dto);

        List<ClienteResponseDTO> result = clienteService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Juan Pérez", result.get(0).getNombre());
    }

    @Test
    @DisplayName("Debe retornar cliente detallado (OrderResponseDTO) por ID")
    void findById_ExistingId_ReturnsClienteOrderResponseDTO() {
        Long id = 1L;
        Cliente entity = new Cliente();
        ClienteOrderResponseDTO dto = new ClienteOrderResponseDTO();
        dto.setId(id);
        dto.setNombre("Juan Pérez");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(entity));
        when(clienteOrderResponseMapper.toDto(entity)).thenReturn(dto);

        ClienteOrderResponseDTO result = clienteService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    @DisplayName("Debe lanzar excepción al buscar por ID inexistente")
    void findById_NonExistingId_ThrowsIdNoExisteException() {
        Long id = 99L;
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IdNoExisteException.class, () -> clienteService.findById(id));
    }

    @Test
    @DisplayName("Debe retornar cliente al buscar por Email existente")
    void findByEmail_ExistingEmail_ReturnsClienteResponseDTO() {
        String email = "juan@duocuc.cl";
        Cliente entity = new Cliente();
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setEmail(email);

        when(clienteRepository.findByEmail(email)).thenReturn(entity);
        when(clienteResponseMapper.toDto(entity)).thenReturn(dto);

        ClienteResponseDTO result = clienteService.findByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    @DisplayName("Debe lanzar excepción al buscar por Email inexistente")
    void findByEmail_NonExistingEmail_ThrowsClienteEmailNoExisteException() {
        String email = "falso@duocuc.cl";
        when(clienteRepository.findByEmail(email)).thenReturn(null);

        assertThrows(ClienteEmailNoExisteException.class, () -> clienteService.findByEmail(email));
    }

    // ==========================================
    // TESTS DE ACTUALIZACIÓN (UPDATE)
    // ==========================================

    @Test
    @DisplayName("Debe actualizar y retornar el cliente si el ID existe")
    void update_ExistingId_ReturnsUpdatedClienteResponseDTO() {
        ClienteUpdateDTO updateDto = new ClienteUpdateDTO();
        updateDto.setId(1L);
        updateDto.setPnombre("Pedro");

        Cliente existingEntity = new Cliente();
        Cliente updatedEntity = new Cliente();
        ClienteResponseDTO expectedResponse = new ClienteResponseDTO();
        expectedResponse.setId(1L);
        expectedResponse.setNombre("Pedro");

        when(clienteRepository.findById(updateDto.getId())).thenReturn(Optional.of(existingEntity));
        when(clienteUpdateMapper.toEntity(existingEntity, updateDto)).thenReturn(updatedEntity);
        when(clienteRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(clienteResponseMapper.toDto(updatedEntity)).thenReturn(expectedResponse);

        ClienteResponseDTO result = clienteService.update(updateDto);

        assertNotNull(result);
        assertEquals("Pedro", result.getNombre());
        verify(clienteRepository, times(1)).save(updatedEntity);
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar actualizar un ID inexistente")
    void update_NonExistingId_ThrowsIdNoExisteException() {
        ClienteUpdateDTO updateDto = new ClienteUpdateDTO();
        updateDto.setId(99L);

        when(clienteRepository.findById(updateDto.getId())).thenReturn(Optional.empty());

        assertThrows(IdNoExisteException.class, () -> clienteService.update(updateDto));
        verify(clienteRepository, never()).save(any());
    }

    // ==========================================
    // TESTS DE ELIMINACIÓN (DELETE)
    // ==========================================

    @Test
    @DisplayName("Debe eliminar cliente y retornar true si el ID existe")
    void deleteClienteById_ExistingId_ReturnsTrue() {
        Long id = 1L;
        when(clienteRepository.existsById(id)).thenReturn(true);

        Boolean result = clienteService.deleteClienteById(id);

        assertTrue(result);
        verify(clienteRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar eliminar un ID inexistente")
    void deleteClienteById_NonExistingId_ThrowsIdNoExisteException() {
        Long id = 99L;
        when(clienteRepository.existsById(id)).thenReturn(false);

        assertThrows(IdNoExisteException.class, () -> clienteService.deleteClienteById(id));
        verify(clienteRepository, never()).deleteById(anyLong());
    }
}