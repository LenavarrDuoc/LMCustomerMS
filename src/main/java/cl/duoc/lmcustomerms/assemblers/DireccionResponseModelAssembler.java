package cl.duoc.lmcustomerms.assemblers;

import cl.duoc.lmcustomerms.controllers.DireccionRESTControllerV2;
import cl.duoc.lmcustomerms.dtos.DireccionResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DireccionResponseModelAssembler implements RepresentationModelAssembler<DireccionResponseDTO, EntityModel<DireccionResponseDTO>> {

    @Override
    public EntityModel<DireccionResponseDTO> toModel(DireccionResponseDTO dto){
        return EntityModel.of(dto,
                linkTo(methodOn(DireccionRESTControllerV2.class).findDireccionById(dto.getId())).withSelfRel(),
                linkTo(methodOn(DireccionRESTControllerV2.class).findAll()).withRel("list-all"),
                linkTo(methodOn(DireccionRESTControllerV2.class).update(null, dto.getId())).withRel("update"),
                linkTo(methodOn(DireccionRESTControllerV2.class).deleteById(dto.getId())).withRel("delete")
        );
    }
}
