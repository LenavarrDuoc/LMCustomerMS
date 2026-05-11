package cl.duoc.lmcustomerms.models;
import jakarta.persistence.*;
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
//Todo lo comentado quedará pendiente hasta cuando se puedan tratar las direcciones como una tabla independiente y una lista asociada a un cliente
@Entity
@Table(name = "direccion", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_direccion_por_cliente",
                // Incluimos fk_cliente_id en la combinación
                columnNames = {"calle", "numero", "nro_depto", "comuna", "fk_cliente_id"}
        )
})
public class Direccion {
    @Id
    @Column(name = "direccion_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    @Column(name = "numero", nullable = false)
    private Integer numero;


    @PositiveOrZero
    @Column(name = "nro_depto")
    private Integer nroDepto;

    @NotBlank
    @Column(name = "calle", nullable = false)
    private String calle;

    @NotBlank
//    @Column(name = "comuna", nullable = false, unique = true)
    private String comuna;

    @NotBlank
//    @Column(name = "region", nullable = false, unique = true)
    private String region;

    @Column(name = "es_Default")
    private Boolean esDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_cliente_id")
    private Cliente cliente;
}
