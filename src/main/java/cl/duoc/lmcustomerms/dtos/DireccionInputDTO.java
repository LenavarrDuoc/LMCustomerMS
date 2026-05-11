package cl.duoc.lmcustomerms.dtos;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"numero", "nroDepto", "calle", "comuna", "region"})
public class DireccionInputDTO {

    @NotNull
    @Positive
    private Integer numero;

    @PositiveOrZero
    private Integer nroDepto;

    @NotBlank
    private String calle;

    @NotBlank
    private String comuna;

    @NotBlank
    private String region;

}
