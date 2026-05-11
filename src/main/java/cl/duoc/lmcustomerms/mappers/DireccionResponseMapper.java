package cl.duoc.lmcustomerms.mappers;

import cl.duoc.lmcustomerms.dtos.DireccionResponseDTO;
import cl.duoc.lmcustomerms.models.Direccion;
import org.springframework.stereotype.Component;

@Component
public class DireccionResponseMapper {

    public DireccionResponseDTO toDto(Direccion objAux){
        if (objAux != null){
            DireccionResponseDTO dto = new DireccionResponseDTO();

            dto.setId(objAux.getId());
            dto.setNumero(objAux.getNumero());
            if(objAux.getNroDepto() != 0){
                dto.setNroDepto(dto.getNroDepto());
            } else {
                dto.setNroDepto(null);
            }
            dto.setCalle(objAux.getCalle());
            dto.setComuna(objAux.getComuna());
            dto.setRegion(objAux.getRegion());
            dto.setEsDefault(objAux.getEsDefault());

            //Devuelve nro de departamento como null si viene como 0 desde la BD para facilitar tratameinto de datos en frontend de ser solicitado el dato.
            dto.setNroDepto(objAux.getNroDepto() == 0 ? null : objAux.getNroDepto());

            return dto;
        }
        return null;
    }
}
