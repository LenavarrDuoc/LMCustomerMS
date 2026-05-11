package cl.duoc.lmcustomerms.mappers;

import cl.duoc.lmcustomerms.dtos.ClienteUpdateDTO;
import cl.duoc.lmcustomerms.models.Cliente;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ClienteUpdateMapper {

    public Cliente toEntity(Cliente ent, ClienteUpdateDTO dto){
        //ent = entidad de Persistencia.

        if (dto != null){


            ent.setNumrun(dto.getNumrun());
            ent.setDvrun(dto.getDvrun());
            ent.setPnombre(dto.getPnombre());
            ent.setSnombre(dto.getSnombre());
            ent.setAppaterno(dto.getAppaterno());
            ent.setApmaterno(dto.getApmaterno());
            ent.setEmail(dto.getEmail());
            ent.setFono(dto.getFono());
            /*TODO:Agregar condición que determine si existe o no dirección a actualizar, de no ser null.
            ent.addDireccion(dto.getDireccion()); */
            ent.setFechaNacimiento(dto.getFechaNacimiento());
            ent.setFechaActualizacion(new Date());

            return ent;
        }
        return null;
    }
}
