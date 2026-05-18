package cl.duoc.lmcustomerms.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1000000)
    @Max(value = 99999999)
    @Column(name = "num_run", nullable = false, unique = true)
    private Integer numrun;

    @Pattern(regexp = "^[0-9Kk]$", message = "dìgito verificador debe ser nùmero de 0 a 9 o k")
    @Column(name = "dv_run", nullable = false)
    private String dvrun;

    @NotBlank(message = "Debe ingresar primer calle.")
    @Column(name = "p_nombre", nullable = false)
    private String pnombre;

    @Column(name = "s_nombre")
    private String snombre;

    @NotBlank(message = "Debe ingresar apellido paterno.")
    @Column(name = "ap_paterno", nullable = false)
    private String appaterno;

    @NotBlank(message = "Debe ingresar apellido materno.")
    @Column(name = "ap_materno", nullable = false)
    private String apmaterno;

    @NotBlank
    @Email(message = "Debe ser formato de correo (@ y dominio incluido)")
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank
    @Length(message = "Debe incluir símbolo '+', código de país y 9 dìgitos,.", min = 12, max = 12)
    @Column(name = "fono", nullable = false)
    private String fono;

    //Función pendiente para cuando tenga claro cómo agregar listas.
    @NotNull(message = "Debe ingresar dirección.")
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Direccion> direcciones =  new ArrayList<>();

    //Usar mientras no esté utilizándose la función de lista de direcciones
//    @NotNull(message = "Debe ingresar dirección.")
//    @Column(name = "direccion")
//    private String direccion;

    @NotNull
    @Column(name = "fec_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotNull
    @Column(name = "fec_creacion", nullable = false)
    private Date fechaCreacion;

    @NotNull
    @Column(name = "fec_actualizacion", nullable = false)
    private Date fechaActualizacion;

    public void addDireccion(Direccion direccionAux){
        this.direcciones.add(direccionAux);
        direccionAux.setCliente(this); //le pasa el cliente creado a la instancia de dirección para que la agrege a sus atributos (a modo de ligar en forma de indetificador)
    }

}
