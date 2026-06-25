package cl.duoc.lmcustomerms.controllers;

import cl.duoc.lmcustomerms.dtos.DireccionInputDTO;
import cl.duoc.lmcustomerms.dtos.DireccionResponseDTO;
import cl.duoc.lmcustomerms.dtos.DireccionUpdateDTO;
import cl.duoc.lmcustomerms.services.DireccionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DireccionRESTController.class)
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
class DireccionRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DireccionService direccionService;

    @Autowired
    private ObjectMapper objectMapper;

    // ==========================================
    // TESTS DE CREACIÓN (POST)
    // ==========================================

    @Test
    @DisplayName("POST /api/v1/direcciones - Debe retornar 201 Created")
    void save_ValidDireccion_Returns201() throws Exception {
        // 1. Arrange
        DireccionInputDTO inputDto = new DireccionInputDTO();
        inputDto.setCalle("Av. Siempre Viva");
        inputDto.setNumero(742);
        inputDto.setNroDepto(0);
        inputDto.setComuna("Springfield");
        inputDto.setRegion("Metropolitana");

        DireccionResponseDTO responseDto = new DireccionResponseDTO();
        responseDto.setId(1L);
        responseDto.setCalle("Av. Siempre Viva");
        responseDto.setNumero(742);
        responseDto.setComuna("Springfield");
        responseDto.setRegion("Metropolitana");

        when(direccionService.save(any(DireccionInputDTO.class))).thenReturn(responseDto);

        // 2. Act & Assert
        mockMvc.perform(post("/api/v1/direcciones")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.calle").value("Av. Siempre Viva"));
    }

    // ==========================================
    // TESTS DE LECTURA (GET)
    // ==========================================

    @Test
    @DisplayName("GET /api/v1/direcciones - Debe retornar 200 OK con lista")
    void findAll_WithData_Returns200() throws Exception {
        // Arrange
        DireccionResponseDTO responseDto = new DireccionResponseDTO();
        responseDto.setId(1L);
        responseDto.setCalle("Av. Siempre Viva");

        when(direccionService.findAll()).thenReturn(List.of(responseDto));

        // Act & Assert
        mockMvc.perform(get("/api/v1/direcciones")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].calle").value("Av. Siempre Viva"));
    }

    @Test
    @DisplayName("GET /api/v1/direcciones - Debe retornar 204 No Content si está vacío")
    void findAll_EmptyList_Returns204() throws Exception {
        // Arrange
        when(direccionService.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/direcciones")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/v1/direcciones/{id} - Debe retornar 200 OK si existe")
    void findDireccionById_ExistingId_Returns200() throws Exception {
        // Arrange
        DireccionResponseDTO responseDto = new DireccionResponseDTO();
        responseDto.setId(1L);
        responseDto.setCalle("Av. Siempre Viva");

        when(direccionService.findById(1L)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/direcciones/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.calle").value("Av. Siempre Viva"));
    }

    @Test
    @DisplayName("GET /api/v1/direcciones/{id} - Debe retornar 404 Not Found si no existe")
    void findDireccionById_NonExistingId_Returns404() throws Exception {
        // Arrange
        when(direccionService.findById(99L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/v1/direcciones/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ==========================================
    // TESTS DE ACTUALIZACIÓN (PUT)
    // ==========================================

    @Test
    @DisplayName("PUT /api/v1/direcciones/{id} - Debe actualizar y retornar 200 OK")
    void update_ExistingId_Returns200() throws Exception {
        // Arrange
        DireccionUpdateDTO updateDto = new DireccionUpdateDTO();
        updateDto.setId(1L);
        updateDto.setCalle("Calle Falsa 123");
        updateDto.setNumero(123);
        updateDto.setNroDepto(0);
        updateDto.setComuna("Puente Alto");    // <- ¡Añadido para pasar el @Valid!
        updateDto.setRegion("Metropolitana");  // <- ¡Añadido para pasar el @Valid!

        DireccionResponseDTO responseDto = new DireccionResponseDTO();
        responseDto.setId(1L);
        responseDto.setCalle("Calle Falsa 123");
        responseDto.setNumero(123);
        responseDto.setComuna("Puente Alto");
        responseDto.setRegion("Metropolitana");

        when(direccionService.update(any(DireccionUpdateDTO.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put("/api/v1/direcciones/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.calle").value("Calle Falsa 123"));
    }

    // ==========================================
    // TESTS DE ELIMINACIÓN (DELETE)
    // ==========================================

    @Test
    @DisplayName("DELETE /api/v1/direcciones/{id} - Debe retornar 204 No Content")
    void deleteById_ExistingId_Returns204() throws Exception {
        // Arrange
        when(direccionService.deleteDireccionById(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/direcciones/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/direcciones/{id} - Debe retornar 404 Not Found")
    void deleteById_NonExistingId_Returns404() throws Exception {
        // Arrange
        when(direccionService.deleteDireccionById(99L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/direcciones/99")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}