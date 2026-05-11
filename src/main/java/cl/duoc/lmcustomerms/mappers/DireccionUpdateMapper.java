package cl.duoc.lmcustomerms.mappers;
import cl.duoc.lmcustomerms.dtos.DireccionUpdateDTO;
import cl.duoc.lmcustomerms.models.Direccion;
import org.springframework.stereotype.Component;
@Component
public class DireccionUpdateMapper {
    public Direccion toEntity(DireccionUpdateDTO dto){
        if (dto != null) {
            Direccion pio = new Direccion(); //pio = Persistence Instance Object.

            pio.setNumero(dto.getNumero());
            if(dto.getNroDepto() != null){
                pio.setNroDepto(dto.getNroDepto());
            } else {
                pio.setNroDepto(0);
            }
            pio.setCalle(dto.getCalle());
            pio.setComuna(dto.getComuna());
            pio.setRegion(dto.getRegion());
            pio.setEsDefault(false);
            return pio;
        }
        return null;
    }
}
