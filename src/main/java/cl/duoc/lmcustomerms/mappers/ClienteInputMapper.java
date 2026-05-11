package cl.duoc.lmcustomerms.mappers;

import cl.duoc.lmcustomerms.dtos.ClienteInputDTO;
import cl.duoc.lmcustomerms.models.Cliente;
import cl.duoc.lmcustomerms.models.Direccion;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ClienteInputMapper {

    private final DireccionInputMapper direccionInputMapper;

    private ClienteInputMapper(DireccionInputMapper direccionInputMapper) {
        this.direccionInputMapper = direccionInputMapper;
    }
    public Cliente toEntity(ClienteInputDTO dto){

        if (dto != null) {
            Cliente pio = new Cliente();    //pio = Persistence Instance Object.


            pio.setNumrun(dto.getNumrun());
            pio.setDvrun(dto.getDvrun());
            pio.setPnombre(dto.getPnombre());
            pio.setSnombre(dto.getSnombre());
            pio.setAppaterno(dto.getAppaterno());
            pio.setApmaterno(dto.getApmaterno());
            pio.setEmail(dto.getEmail());
            pio.setFono(dto.getFono());

            pio.setFechaNacimiento(dto.getFechaNacimiento());
            pio.setFechaCreacion(new Date());
            pio.setFechaActualizacion(new Date());

            //Mapear direcciones:
            if (dto.getDirecciones() != null) {
                dto.getDirecciones().forEach(direccionDTO -> {
                    Direccion direccionEntity = direccionInputMapper.toEntity(direccionDTO);

                    pio.addDireccion(direccionEntity);
                    if (pio.getDirecciones() != null && pio.getDirecciones().size() == 1) {
                        pio.getDirecciones().getFirst().setEsDefault(true);
                    }
                });
            }
            return pio;
        }
        return null;
    }
}
