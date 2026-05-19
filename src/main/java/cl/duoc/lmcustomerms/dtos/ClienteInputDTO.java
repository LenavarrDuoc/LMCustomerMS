package cl.duoc.lmcustomerms.dtos;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "numrun", "pnombre", "snombre", "appaterno", "apmaterno", "rol", "direccion", "email", "fono"})
public class ClienteInputDTO {



    @Min(value = 1000000)
    @Max(value = 99999999)
    private Integer numrun;

    @Pattern(regexp = "^[0-9Kk]$", message = "dìgito verificador debe ser nùmero de 0 a 9 o k")
    private String dvrun;

    @NotBlank(message = "Debe ingresar primer calle.")
    private String pnombre;

    private String snombre;

    @NotBlank(message = "Debe ingresar apellido paterno.")
    private String appaterno;

    @NotBlank(message = "Debe ingresar apellido materno.")
    private String apmaterno;

    @NotBlank
    @Email(message = "Debe ser formato de correo (@ y dominio incluido)")
    private String email;

    @NotBlank
    @Length(message = "Debe incluir símbolo '+', código de país y 9 dìgitos,.", min = 12, max = 12)
    private String fono;

    @NotNull(message = "Debe ingresar una dirección.")
    private List<DireccionInputDTO> direcciones;

    @NotNull
    private LocalDate fechaNacimiento;

}
