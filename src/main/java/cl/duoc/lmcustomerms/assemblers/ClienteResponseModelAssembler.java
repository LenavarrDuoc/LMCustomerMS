package cl.duoc.lmcustomerms.assemblers;

import cl.duoc.lmcustomerms.controllers.ClienteRESTControllerV2;
import cl.duoc.lmcustomerms.dtos.ClienteResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClienteResponseModelAssembler implements RepresentationModelAssembler<ClienteResponseDTO, EntityModel<ClienteResponseDTO>> {

    @Override
    public EntityModel<ClienteResponseDTO> toModel(ClienteResponseDTO dto){
        return EntityModel.of(dto,
                linkTo(methodOn(ClienteRESTControllerV2.class).findById(dto.getId())).withSelfRel(),
                linkTo(methodOn(ClienteRESTControllerV2.class).findAll()).withRel("list-all"),
                linkTo(methodOn(ClienteRESTControllerV2.class).findAllByPnombre(dto.getNombre().split(" ")[0])).withRel("list-all-by-name"),
                linkTo(methodOn(ClienteRESTControllerV2.class).existsById(dto.getId())).withRel("exists-by-id"),
                linkTo(methodOn(ClienteRESTControllerV2.class).findByNumRun(Integer.parseInt((dto.getRun() == null ?  "0" : dto.getRun().split("-")[0])))).withRel("find-by-numrun"),//Requirió tratamiento de String de rut para pasar a integer solo el número sin guión ni dígito verificador, o sea 0 si es nulo al correr pruebas.
                linkTo(methodOn(ClienteRESTControllerV2.class).findByEmail(dto.getEmail())).withRel("find-by-email"),
                linkTo(methodOn(ClienteRESTControllerV2.class).findByFono(dto.getFono())).withRel("find-by-fono"),
                linkTo(methodOn(ClienteRESTControllerV2.class).update(null, dto.getId())).withRel("update"),
                linkTo(methodOn(ClienteRESTControllerV2.class).deleteById(dto.getId())).withRel("delete")
        );
    }
}

