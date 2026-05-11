package cl.duoc.lmcustomerms.dtos;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "run","nombre", "email", "fono", "direcciones"})
public class ClienteResponseDTO {

    private Long id;
    private String run;
    private String nombre;
    private String email;
    private String fono;
    private List<DireccionResponseDTO> direcciones;
    private LocalDate fechaNacimiento;
    private Date fechaIngreso;
    private Date fechaUltimaActualizacion;

}