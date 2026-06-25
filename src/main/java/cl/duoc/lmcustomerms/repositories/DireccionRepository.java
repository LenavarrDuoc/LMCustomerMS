package cl.duoc.lmcustomerms.repositories;
import cl.duoc.lmcustomerms.models.Direccion;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {


    Direccion findByCalle(String calle);

    boolean existsDireccionByCalleAndNumeroAndNroDepto(String calle, Integer numero, Integer nroDepto);

    List <Direccion> findAllByClienteId(Long id);
}
