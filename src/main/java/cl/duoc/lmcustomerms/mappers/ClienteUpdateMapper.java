package cl.duoc.lmcustomerms.mappers;

import cl.duoc.lmcustomerms.dtos.ClienteUpdateDTO;
import cl.duoc.lmcustomerms.models.Cliente;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ClienteUpdateMapper {

    public Cliente toEntity(Cliente pio, ClienteUpdateDTO dto){
        //pio = Persistence Instance Object.

        if (dto != null){


            pio.setNumrun(dto.getNumrun());
            pio.setDvrun(dto.getDvrun());
            pio.setPnombre(dto.getPnombre());
            pio.setSnombre(dto.getSnombre());
            pio.setAppaterno(dto.getAppaterno());
            pio.setApmaterno(dto.getApmaterno());
            pio.setEmail(dto.getEmail());
            pio.setFono(dto.getFono());
            /*TODO:Agregar condición que determine si existe o no dirección a actualizar, de no ser null.
            pio.addDireccion(dto.getDireccion()); */
            pio.setFechaNacimiento(dto.getFechaNacimiento());
            pio.setFechaActualizacion(new Date());

            return pio;
        }
        return null;
    }
}
