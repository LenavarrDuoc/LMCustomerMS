package cl.duoc.lmcustomerms.mappers;

import cl.duoc.lmcustomerms.dtos.DireccionResponseDTO;
import cl.duoc.lmcustomerms.models.Direccion;
import org.springframework.stereotype.Component;

@Component
public class DireccionResponseMapper {

    public DireccionResponseDTO toDto(Direccion ent){
        if (ent != null){
            DireccionResponseDTO dto = new DireccionResponseDTO();

            dto.setId(ent.getId());
            dto.setClienteId(ent.getCliente().getId());
            dto.setNumero(ent.getNumero());
            if(ent.getNroDepto() != 0){
                dto.setNroDepto(dto.getNroDepto());
            } else {
                dto.setNroDepto(null);
            }
            dto.setCalle(ent.getCalle());
            dto.setComuna(ent.getComuna());
            dto.setRegion(ent.getRegion());
            dto.setEsDefault(ent.getEsDefault());

            //Devuelve nro de departamento como null si viene como 0 desde la BD para facilitar tratameinto de datos en frontend de ser solicitado el dato.
            dto.setNroDepto(ent.getNroDepto() == 0 ? null : ent.getNroDepto());

            return dto;
        }
        return null;
    }
}
