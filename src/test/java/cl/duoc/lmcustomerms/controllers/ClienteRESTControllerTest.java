package cl.duoc.lmcustomerms.controllers;

import cl.duoc.lmcustomerms.dtos.ClienteInputDTO;
import cl.duoc.lmcustomerms.dtos.ClienteOrderResponseDTO;
import cl.duoc.lmcustomerms.dtos.ClienteResponseDTO;
import cl.duoc.lmcustomerms.dtos.ClienteUpdateDTO;
import cl.duoc.lmcustomerms.services.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ClienteRESTController.class)
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
class ClienteRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    // ==========================================
    // TESTS DE CREACIÓN (POST)
    // ==========================================

    @Test
    @DisplayName("POST /api/v1/clientes - Debe retornar 201 Created")
    void save_ValidCliente_Returns201() throws Exception {


        ClienteInputDTO inputDto = new ClienteInputDTO();

        // Llenamos los campos obligatorios para que pase el @Valid de tu controlador
        inputDto.setNumrun(12345678);
        inputDto.setDvrun("5");
        inputDto.setPnombre("Juan");
        inputDto.setAppaterno("Pérez");
        inputDto.setApmaterno("Soto");
        inputDto.setEmail("test@duocuc.cl");
        inputDto.setFono("+56912345678");
        inputDto.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        inputDto.setDirecciones(new ArrayList<>());

        ClienteResponseDTO responseDto = new ClienteResponseDTO();
        responseDto.setId(1L);
        responseDto.setRun("12345678-5");
        responseDto.setEmail("test@duocuc.cl");
        responseDto.setFono("+56912345678");

        when(clienteService.save(any(ClienteInputDTO.class))).thenReturn(responseDto);

        //Emula token CSRF en petición HTTP POST para documentación de testeo con surefire.
        mockMvc.perform(post("/api/v1/clientes")
                        .with(csrf()) // <- ¡AQUÍ EMULA EL CSRF!
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.run").value("12345678-5"));
    }

    // ==========================================
    // TESTS DE LECTURA (GET) - GENERALES
    // ==========================================

    @Test
    @DisplayName("GET /api/v1/clientes - Debe retornar 200 OK con lista")
    void findAll_WithData_Returns200() throws Exception {
        ClienteResponseDTO responseDto = new ClienteResponseDTO();
        responseDto.setId(1L);
        responseDto.setNombre("Juan Pérez");

        when(clienteService.findAll()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/clientes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));
    }

    @Test
    @DisplayName("GET /api/v1/clientes - Debe retornar 204 No Content si está vacío")
    void findAll_EmptyList_Returns204() throws Exception {
        when(clienteService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/clientes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/clientes/{id} - Debe retornar 200 OK")
    void findById_ExistingId_Returns200() throws Exception {
        ClienteOrderResponseDTO responseDto = new ClienteOrderResponseDTO();
        responseDto.setId(1L);
        responseDto.setNombre("Juan Pérez");

        when(clienteService.findById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/clientes/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // ==========================================
    // TESTS DE BÚSQUEDAS ESPECÍFICAS (GET)
    // ==========================================

    @Test
    @DisplayName("GET /api/v1/clientes/exists-by-id/{id} - Debe retornar true si existe")
    void existsById_ExistingId_ReturnsTrue() throws Exception {
        when(clienteService.existsById(1L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/clientes/exists-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("GET /api/v1/clientes/by-pnombre/{pnombre} - Debe retornar lista de clientes")
    void findAllByPnombre_WithMatches_Returns200() throws Exception {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(1L);
        dto.setNombre("Juan");

        when(clienteService.findAllByPnombre("Juan")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/clientes/by-pnombre/Juan")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    @DisplayName("GET /api/v1/clientes/by-numrun/{numrun} - Debe retornar cliente por RUN")
    void findByNumRun_ExistingRun_Returns200() throws Exception {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(1L);
        dto.setRun("12345678-5");

        when(clienteService.findByNumRun(12345678)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/clientes/by-numrun/12345678")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.run").value("12345678-5"));
    }

    @Test
    @DisplayName("GET /api/v1/clientes/by-email/{email} - Debe retornar cliente por Email")
    void findByEmail_ExistingEmail_Returns200() throws Exception {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(1L);
        dto.setEmail("juan@duocuc.cl");

        when(clienteService.findByEmail("juan@duocuc.cl")).thenReturn(dto);

        mockMvc.perform(get("/api/v1/clientes/by-email/juan@duocuc.cl")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@duocuc.cl"));
    }

    @Test
    @DisplayName("GET /api/v1/clientes/by-fono/{fono} - Debe retornar cliente por Teléfono")
    void findByFono_ExistingFono_Returns200() throws Exception {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(1L);
        dto.setFono("+56912345678");

        when(clienteService.findByFono("+56912345678")).thenReturn(dto);

        mockMvc.perform(get("/api/v1/clientes/by-fono/+56912345678")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fono").value("+56912345678"));
    }

    // ==========================================
    // TESTS DE ACTUALIZACIÓN (PUT)
    // ==========================================

    @Test
    @DisplayName("PUT /api/v1/clientes/{id} - Debe actualizar cliente y retornar 200")
    void update_ExistingId_Returns200AndUpdatedLocation() throws Exception {

        ClienteUpdateDTO updateDto = new ClienteUpdateDTO();

        // Llenamos los campos obligatorios para pasar el @Valid
        updateDto.setNumrun(12345678);
        updateDto.setDvrun("5");
        updateDto.setPnombre("Juan Actualizado");
        updateDto.setAppaterno("Pérez");
        updateDto.setApmaterno("Soto");
        updateDto.setEmail("test@duocuc.cl");
        updateDto.setFono("+56912345678");
        updateDto.setFechaNacimiento(LocalDate.of(1990, 1, 1));

        ClienteResponseDTO responseDto = new ClienteResponseDTO();
        responseDto.setId(1L);
        responseDto.setNombre("Juan Actualizado");

        when(clienteService.update(any(ClienteUpdateDTO.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/clientes/1")
                        .with(csrf()) // <--- ¡AQUÍ!
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.nombre").value("Juan Actualizado"));
    }

    // ==========================================
    // TESTS DE ELIMINACIÓN (DELETE)
    // ==========================================

    @Test
    @DisplayName("DELETE /api/v1/clientes/{id} - Debe retornar 204 No Content")
    void deleteById_ExistingId_Returns204() throws Exception {
        when(clienteService.deleteClienteById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/clientes/1")
                        .with(csrf())) // <--- ¡AQUÍ!
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/clientes/{id} - Debe retornar 404 Not Found")
    void deleteById_NonExistingId_Returns404() throws Exception {
        when(clienteService.deleteClienteById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/clientes/99")
                        .with(csrf())) // <--- ¡AQUÍ!
                .andExpect(status().isNotFound());
    }
}