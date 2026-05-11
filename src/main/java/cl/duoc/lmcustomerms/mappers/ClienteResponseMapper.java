package cl.duoc.lmcustomerms.mappers;

import cl.duoc.lmcustomerms.dtos.ClienteResponseDTO;
import cl.duoc.lmcustomerms.models.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteResponseMapper {

    private final DireccionResponseMapper direccionResponseMapper;

    public ClienteResponseMapper(DireccionResponseMapper direccionResponseMapper) {
        this.direccionResponseMapper = direccionResponseMapper;
    }

    public ClienteResponseDTO toDto (Cliente ent){
        if (ent != null){
            ClienteResponseDTO dto = new ClienteResponseDTO();


            dto.setId(ent.getId());
            dto.setRun(ent.getNumrun().toString() + '-' + ent.getDvrun());
            dto.setNombre(ent.getPnombre() + ' ' + ent.getAppaterno());
            dto.setEmail(ent.getEmail());
            dto.setFono(ent.getFono());
            dto.setFechaNacimiento(ent.getFechaNacimiento());
            dto.setFechaIngreso(ent.getFechaCreacion());
            dto.setFechaUltimaActualizacion(ent.getFechaActualizacion());

            //Mapeo de direcciones DTO:
            if (ent.getDirecciones() != null){
                dto.setDirecciones(ent.getDirecciones().stream().map(direccionResponseMapper::toDto).toList());
            }

            return dto;
        }
        return null;
    }
}
