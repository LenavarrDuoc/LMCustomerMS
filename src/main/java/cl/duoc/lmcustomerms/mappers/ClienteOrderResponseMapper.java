package cl.duoc.lmcustomerms.mappers;

import cl.duoc.lmcustomerms.dtos.ClienteOrderResponseDTO;
import cl.duoc.lmcustomerms.models.Cliente;
import cl.duoc.lmcustomerms.models.Direccion;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClienteOrderResponseMapper {

    private final DireccionResponseMapper direccionResponseMapper;

    public ClienteOrderResponseMapper(DireccionResponseMapper direccionResponseMapper) {
        this.direccionResponseMapper = direccionResponseMapper;
    }

    public ClienteOrderResponseDTO toDto (Cliente objAux){
        if (objAux != null){
            ClienteOrderResponseDTO dto = new ClienteOrderResponseDTO();


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
                List<Direccion> direcciones = objAux.getDirecciones();
                for (Direccion direccion : direcciones) {
                    if(direccion.getEsDefault()){}
                    dto.setDireccion(direccionResponseMapper.toDto(direccion));
                }

            }

            return dto;
        }
        return null;
    }
}
