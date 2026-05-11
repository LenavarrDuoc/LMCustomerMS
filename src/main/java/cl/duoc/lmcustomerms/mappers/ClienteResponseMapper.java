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

    public ClienteResponseDTO toDto (Cliente objAux){
        if (objAux != null){
            ClienteResponseDTO dto = new ClienteResponseDTO();


            dto.setId(objAux.getId());
            dto.setRun(objAux.getNumrun().toString() + '-' + objAux.getDvrun());
            dto.setNombre(objAux.getPnombre() + ' ' + objAux.getAppaterno());
            dto.setEmail(objAux.getEmail());
            dto.setFono(objAux.getFono());
            dto.setFechaNacimiento(objAux.getFechaNacimiento());
            dto.setFechaIngreso(objAux.getFechaCreacion());
            dto.setFechaUltimaActualizacion(objAux.getFechaActualizacion());

            //Mapeo de direcciones DTO:
            if (objAux.getDirecciones() != null){
                dto.setDirecciones(objAux.getDirecciones().stream().map(direccionResponseMapper::toDto).toList());
            }

            return dto;
        }
        return null;
    }
}
