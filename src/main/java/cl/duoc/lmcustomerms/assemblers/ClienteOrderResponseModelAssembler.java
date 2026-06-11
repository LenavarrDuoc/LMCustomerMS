package cl.duoc.lmcustomerms.assemblers;

import cl.duoc.lmcustomerms.controllers.ClienteRESTControllerV2;
import cl.duoc.lmcustomerms.dtos.ClienteOrderResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClienteOrderResponseModelAssembler implements RepresentationModelAssembler<ClienteOrderResponseDTO, EntityModel<ClienteOrderResponseDTO>> {

    @Override
    public EntityModel<ClienteOrderResponseDTO> toModel(ClienteOrderResponseDTO dto){
        return EntityModel.of(dto,
                linkTo(methodOn(ClienteRESTControllerV2.class).findById(dto.getId())).withSelfRel(),
                linkTo(methodOn(ClienteRESTControllerV2.class).findByNumRun(Integer.parseInt(dto.getRun() == null ? "0" : dto.getRun().split("-")[0]))).withRel("find-by-numrun"),
                linkTo(methodOn(ClienteRESTControllerV2.class).existsById(dto.getId())).withRel("exists-by-id")
                );
    }
}
