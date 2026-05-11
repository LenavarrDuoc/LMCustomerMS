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
            Cliente ent = new Cliente();    //ent = entidad en capa de persistencia.


            ent.setNumrun(dto.getNumrun());
            ent.setDvrun(dto.getDvrun());
            ent.setPnombre(dto.getPnombre());
            ent.setSnombre(dto.getSnombre());
            ent.setAppaterno(dto.getAppaterno());
            ent.setApmaterno(dto.getApmaterno());
            ent.setEmail(dto.getEmail());
            ent.setFono(dto.getFono());

            ent.setFechaNacimiento(dto.getFechaNacimiento());
            ent.setFechaCreacion(new Date());
            ent.setFechaActualizacion(new Date());

            //Mapear direcciones:
            if (dto.getDirecciones() != null) {
                dto.getDirecciones().forEach(direccionDTO -> {
                    Direccion direccionEntity = direccionInputMapper.toEntity(direccionDTO);

                    ent.addDireccion(direccionEntity);
                    if (ent.getDirecciones() != null && ent.getDirecciones().size() == 1) {
                        ent.getDirecciones().getFirst().setEsDefault(true);
                    }
                });
            }
            return ent;
        }
        return null;
    }
}
