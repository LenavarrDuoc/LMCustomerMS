package cl.duoc.lmcustomerms.controllers;

import cl.duoc.lmcustomerms.dtos.*;
import cl.duoc.lmcustomerms.services.DireccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/direcciones")
@Tag(name = "Direcciones", description = "Gestión de direcciones de clientes.")
public class DireccionRESTController {

    private static final Logger logger = LoggerFactory.getLogger(DireccionRESTController.class.getName());

    @Autowired
    private DireccionService direccionService;

    //CREATE:
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Se ha creado registro",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DireccionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto al hacer solicitud (ej: direccionId ya existe)",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @PostMapping
    @Operation(summary = "Crear dirección.", description = "Guardar un registro de nueva dirección de cliente.")
    public ResponseEntity<DireccionResponseDTO> save(@Valid @RequestBody DireccionInputDTO dto){
        String logMsg = "Recibiendo solicitud para crear/guardar direccion";
        logger.info(logMsg);
        DireccionResponseDTO created = direccionService.save(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        //de componentes de constructor URI // de la actual request //ruta de id // sacar la id del obj creado // transformar a URI.
        logger.info(logMsg + "=> creado con ID: {}, y direccion: {}.", created.getId(), (created.getCalle() + created.getNumero() + (created.getNroDepto() == null ? "" : created.getNroDepto()) + created.getComuna() + created.getComuna() + created.getRegion() +  "."));
        return ResponseEntity.created(location).body(created);
        //devuelve el estado y la locación //devuelve el objeto creado
    }


    //READ:
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se han encontrado registros",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DireccionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No se han encontrado registros.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @GetMapping
    @Operation(summary = "Listar todas las direcciones de clientes.", description = "Muestra todos los registros de direcciones de clientes.")
    public ResponseEntity<List<DireccionResponseDTO>> findAll(){
        String logMsgRequest = "Recibiendo solicitud para buscar listado de direcciones.";
        String logMsg = "Solicitud para buscar listado de direcciones.";
        logger.info(logMsgRequest);
        List<DireccionResponseDTO> listadoDTO = direccionService.findAll();

        if (!listadoDTO.isEmpty()){
            logger.info(logMsg + "=> encontrado(s) y enlistado(s).");
            return ResponseEntity.ok(listadoDTO);
        }
        logger.info(logMsg + "=> vacío.");
        return ResponseEntity.noContent().build();
    }


    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se ha encontrado registro perteneciente a dirección según ID ingresado.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DireccionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado registro perteneciente a dirección según ID ingresado.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @GetMapping("/{id}")
    @Operation(summary = "Encuentra dirección de cliente por ID", description = "Trae el registro de dirección perteneciente a un cliente según ID de dirección ingresado.")
    public ResponseEntity<DireccionResponseDTO> findDireccionById(@Parameter(description = "ID de dirección", required = true) @PathVariable Long id){
        String logMsgRequest = "Recibiendo solicitud para buscar direccion por ID: " + id + ".";
        String logMsg = "Solicitud para buscar direccion por ID: " + id + ".";
        logger.info(logMsgRequest);
        DireccionResponseDTO dto = direccionService.findById(id);
        if (dto != null){
            logger.info(logMsg + " => encontrado Direccion:{}", dto.getCalle() + dto.getNumero() + (dto.getNroDepto() == null ? "" : dto.getNroDepto()) + dto.getComuna() + dto.getComuna() + dto.getRegion() +  ".");
            return ResponseEntity.ok(dto);
        }
        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }


    //UPDATE:
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se ha actualizado registro",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DireccionUpdateDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado registro de dirección según ID ingresado.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar dirección de cliente por ID", description = "Actualiza información de registro de dirección perteneciente a cliente según ID de dirección ingresado.")
    public ResponseEntity<DireccionResponseDTO> update(@Parameter(description = "ID de dirección", required = true) @Valid @RequestBody DireccionUpdateDTO objAux, @PathVariable Long id){
        String logMsgRequest = "Recibiendo solicitud para actualizar direccion con ID: " + id + ".";
        String logMsg = "Solicitud para actualizar direccion con ID: " + id + ".";
        logger.info(logMsgRequest);
        objAux.setId(id);
        DireccionResponseDTO updated = direccionService.update(objAux);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updated.getId()).toUri();
        //de componentes de constructor URI // de la actual request //ruta de id // sacar la id del obj creado // transformar a URI.
        logger.info(logMsg + "=> actualizado.");
        return ResponseEntity.status(200).location(location).body(updated);
        //devuelve el estado y la locación //devuelve el objeto creado
    }


    //DELETE:
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se ha eliminado registro.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DireccionResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado registro de dirección según ID ingresado.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @DeleteMapping("{id}")
    @Operation(summary = "Eliminar dirección de un cliente por ID.", description = "Eliminar el registro de dirección perteneciente a un cliente según ID de dirección ingresado.")
    public ResponseEntity<Boolean> deleteById(@Parameter(description = "ID de dirección", required = true) @PathVariable Long id){
        String logMsgRequest = "Recibiendo solicitud para borrar direccion con ID: " + id + ".";
        String logMsg = "Solicitud para borrar direccion con ID: " + id + ".";
        logger.info(logMsgRequest);
        if(direccionService.deleteDireccionById(id)){
            logger.info(logMsg + "=> encontrado y borrado.");
            return ResponseEntity.noContent().build();
        }
        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }
}
