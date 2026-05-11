package cl.duoc.lmcustomerms.repositories;
import cl.duoc.lmcustomerms.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    //NOTA: Sin los @Query, el código funciona igual. Con los @Query, resulta la consulta. La diferencia está en solo la declaración manual o automática de la query, pero sigue siendo a través de JPA.
    //Los @Query declarados como nativos (native = true) son más eficientes al ir al motor directo de la BD, pero debe sopesarse el tener que definir manualmente entidades o mejor usar la query a través de JPA.

  //  @Query(value = "SELECT * FROM cliente u WHERE u.num_run = :numrun", nativeQuery = true) Cliente findByNumrun(Integer numrun);
    //En este caso, la de arriba es una query nativa. Se declara num_run ya que ese es el calle de columna indicado a nivel de BD.

    @Query("SELECT u FROM Cliente u where u.numrun=:numrun")
    Cliente findByNumrun(Integer numrun);

    //@Query("SELECT u FROM Cliente u where u.pnombre=:pnombre")
    List<Cliente> findAllByPnombre(String pnombre);

    //@Query("SELECT u FROM Cliente u where u.email=:email")
    Cliente findByEmail(String email);

    //@Query("SELECT u FROM Cliente u where u.fono=:fono")
    Cliente findByFono(String fono);

    //@Query("SELECT u FROM Cliente u where u.numrun=:numrun")
    boolean existsByNumrun(Integer numrun);

    //@Query("SELECT u FROM Cliente u where u.email=:email")
    boolean existsByEmail(String email);

}