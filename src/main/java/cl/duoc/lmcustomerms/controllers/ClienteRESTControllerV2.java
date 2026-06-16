package cl.duoc.lmcustomerms.controllers;


import cl.duoc.lmcustomerms.assemblers.ClienteOrderResponseModelAssembler;
import cl.duoc.lmcustomerms.assemblers.ClienteResponseModelAssembler;
import cl.duoc.lmcustomerms.dtos.ClienteInputDTO;
import cl.duoc.lmcustomerms.dtos.ClienteOrderResponseDTO;
import cl.duoc.lmcustomerms.dtos.ClienteResponseDTO;
import cl.duoc.lmcustomerms.dtos.ClienteUpdateDTO;
import cl.duoc.lmcustomerms.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/v2/clientes")
@Tag(name = "Clientes V2", description = "Gestión de clientes.")
public class ClienteRESTControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(ClienteRESTControllerV2.class.getName());

    //TODO: para pruebas unitarias, implementar @RequiredArgsConstructor, borrar anotación @Autowired de implementaciones que la tengan, y convertir a private final todas aquellas implementaciones.
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteResponseModelAssembler clienteResponseModelAssembler;

    @Autowired
    private ClienteOrderResponseModelAssembler clienteOrderResponseModelAssembler;

    //CREATE:
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Se ha creado registro",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EntityModel.class),
                            examples = @ExampleObject(name = "Respuesta 201 guardado de Cliente.", externalValue = "/cl/duoc/lmcustomerms/examples/ClienteSave201Example.json")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true),
                            examples = @ExampleObject(name = "Respuesta 400 guardado de Cliente.", externalValue = "/cl/duoc/lmcustomerms/examples/ClienteSave400Example.json")
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto al hacer solicitud (ej: clienteId ya existe)",
                    content = @Content(schema = @Schema(hidden = true),
                            examples = @ExampleObject(name = "Respuesta 409 guardado de Cliente.", externalValue = "/cl/duoc/lmcustomerms/examples/ClienteSave409Example.json")
                    )
            )
    }
    )

    @PostMapping
    @Operation(summary = "Crear cliente.", description = "Guardar un registro de nuevo cliente.")
    public ResponseEntity<EntityModel<ClienteResponseDTO>> save(@Valid @RequestBody ClienteInputDTO dto){
        String logMsgRequest = "Recibiendo solicitud para crear/guardar cliente.";
        String logMsg = "Solicitud para crear/guardar cliente.";
        logger.info(logMsgRequest);
        ClienteResponseDTO created = clienteService.save(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
                    //de componentes de constructor URI // de la actual request //ruta de id // sacar la id del obj creado // transformar a URI.
        logger.info(logMsg + "=> creado con ID: {}, R.U.N.: {}, correo: {}, fono: {}.", created.getId(), created.getRun(), created.getEmail(), created.getFono());
        return ResponseEntity.created(location).body(clienteResponseModelAssembler.toModel(created));
                    //devuelve el estado y la locación //devuelve el objeto creado
    }


    //READ:
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se han encontrado registros.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No contiene registros.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @GetMapping
    @Operation(summary = "Listar todos los clientes.", description = "Muestra todos los registros de clientes.")
    public ResponseEntity<CollectionModel<EntityModel<ClienteResponseDTO>>> findAll(){
        String logMsgRequest = "Recibiendo solicitud para buscar listado de clientes.";
        String logMsg = "Solicitud para buscar listado de clientes.";
        logger.info(logMsgRequest);
        List<EntityModel<ClienteResponseDTO>> listadoDTO = clienteService.findAll().stream().map(dto -> clienteResponseModelAssembler.toModel(dto)).collect(Collectors.toList());

        if (!listadoDTO.isEmpty()){
            logger.info(logMsg + "=> encontrado(s) y enlistado(s).");
            return ResponseEntity.ok(CollectionModel.of(listadoDTO, linkTo(methodOn(ClienteRESTControllerV2.class).findAll()).withSelfRel()));
        }
        logger.info(logMsg + "=> sin coincidencias (vacío).");
        return ResponseEntity.noContent().build();
    }


    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registro de cliente con ID ingresado existe.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado registro perteneciente a ID ingresado.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @GetMapping("/exists-by-id/{id}")
    @Operation(summary = "Corrobora existencia de cliente por ID", description = "Corrobora si un cliente existe en registro según ID ingresado.")
    public ResponseEntity<Boolean> existsById(@Parameter(description = "ID de cliente", required = true) @PathVariable Long id) {
        String logMsgRequest = "Recibiendo solicitud para verificar existencia de cliente con ID: " + id + ".";
        String logMsg = "Solicitud para verificar existencia de cliente con ID: " + id + ".";
        logger.info(logMsgRequest);
        if (clienteService.existsById(id)) {
            logger.info(logMsg + " => encontrado.");
            return ResponseEntity.ok(true);
        }
        logger.info(logMsg + " => no encontrado.");
        return ResponseEntity.ok(false);
    }


    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se han encontrado registros de clientes con coincidencia de Nombre ingresado.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No se han encontrado registros de clientes con coincidencia de nombre ingresado.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha indicado nombre en la URL.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @GetMapping("/by-pnombre/{pnombre}")
    @Operation(summary = "Encontrar clientes por nombre", description = "Trae el registro peteneciente a todos los clientes coincidentes con primer nombre ingresado.")
    public ResponseEntity<CollectionModel<EntityModel<ClienteResponseDTO>>> findAllByPnombre(@Parameter(description = "Primer nombre de cliente(s) a buscar", required = true) @PathVariable String pnombre){
        String logMsgRequest = "Recibiendo solicitud para buscar listado de clientes coincidentes con primer nombre: " + pnombre + ".";
        String logMsg = "Solicitud para buscar listado de clientes coincidentes con primer nombre: " + pnombre + ".";
        logger.info(logMsgRequest);
        List<EntityModel<ClienteResponseDTO>> listadoDTO = clienteService.findAllByPnombre(pnombre).stream().map(dto -> clienteResponseModelAssembler.toModel(dto)).collect(Collectors.toList());

        if (!listadoDTO.isEmpty()){
            logger.info(logMsg + "=> encontrado(s) y enlistado(s).");

            return ResponseEntity.ok(CollectionModel.of(listadoDTO, linkTo(methodOn(ClienteRESTControllerV2.class).findAllByPnombre(pnombre.split(" ")[0])).withSelfRel()));
        }
        logger.info(logMsg + "=> sin coincidencias (vacío).");
        return ResponseEntity.noContent().build();
    }

    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se ha encontrado registro perteneciente a cliente según ID ingresado.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado registro perteneciente a cliente según ID ingresado.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @GetMapping("/{id}")
    @Operation(summary = "Encuentra cliente por ID", description = "Trae el registro perteneciente a un cliente según ID ingresado.")
    public ResponseEntity<EntityModel<ClienteOrderResponseDTO>> findById(@Parameter(description = "ID de cliente", required = true) @PathVariable Long id){
        String logMsgRequest = "Recibiendo solicitud para buscar cliente por ID: " + id + ".";
        String logMsg = "Solicitud para buscar cliente por ID: " + id + ".";
        logger.info(logMsgRequest);
        ClienteOrderResponseDTO dto = clienteService.findById(id);
        if (dto != null){
            logger.info(logMsg + "=> encontrado.");
            return ResponseEntity.ok(clienteOrderResponseModelAssembler.toModel(dto));
        }
        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }


    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se ha encontrado registro perteneciente a cliente según número de R.U.N. ingresado.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado registro perteneciente a cliente según número de R.U.N. ingresado.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @GetMapping("/by-numrun/{numrun}")
    @Operation(summary = "Encuentra cliente por Número de R.U.N.", description = "Trae el registro perteneciente a cliente según Número de R.U.N. ingresado.")
    public ResponseEntity<EntityModel<ClienteResponseDTO>> findByNumRun(@Parameter(description = "Número de R.U.N. de cliente", required = true) @PathVariable Integer numrun){
        String logMsgRequest = "Recibiendo solicitud para buscar cliente por R.U.N.: " + numrun + ".";
        String logMsg = "Solicitud para buscar cliente por R.U.N.: " + numrun + ".";
        logger.info(logMsgRequest);
        ClienteResponseDTO dto = clienteService.findByNumRun(numrun);
        if (dto != null){
            logger.info(logMsg + "=> encontrado con ID:{}", dto.getId() + ".");
            return ResponseEntity.ok(clienteResponseModelAssembler.toModel(dto));
        }
        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }


    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se ha encontrado registro perteneciente a cliente según email ingresado.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado registro perteneciente a cliente según email ingresado.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @GetMapping("/by-email/{email}")
    @Operation(summary = "Encuentra cliente por email.", description = "Trae registro perteneciente a cliente según email ingresado.")
    public ResponseEntity<EntityModel<ClienteResponseDTO>> findByEmail(@Parameter(description = "Email de cliente", required = true) @PathVariable String email){
        String logMsgRequest = "Recibiendo solicitud para buscar cliente por correo electrónico: " + email + ".";
        String logMsg = "Solicitud para buscar cliente por correo electrónico: " + email + ".";
        logger.info(logMsgRequest);
        ClienteResponseDTO dto = clienteService.findByEmail(email);
        if (dto != null){
            logger.info(logMsg + "=> encontrado con ID:{}", dto.getId() + ".");
            return ResponseEntity.ok(clienteResponseModelAssembler.toModel(dto));
        }
        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }


    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se ha encontrado registro perteneciente a cliente según número telefónico ingresado.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se ha encontrado registro perteneciente a cliente según número telefónico ingresado.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @GetMapping("/by-fono/{fono}")
    @Operation(summary = "Encuentra cliente por número telefónico", description = "Trae el registro pertenenciente a cliente según número telefónico ingresado.")
    public ResponseEntity<EntityModel<ClienteResponseDTO>> findByFono(@Parameter(description = "Número telefónico de cliente", required = true) @PathVariable String fono){
        String logMsgRequest = "Recibiendo solicitud para buscar cliente por teléfono registrado: " + fono + ".";
        String logMsg = "Solicitud para buscar cliente por teléfono registrado: " + fono + ".";
        logger.info(logMsgRequest);
        ClienteResponseDTO dto = clienteService.findByFono(fono);
        if (dto != null){
            logger.info(logMsg + "=> encontrado con ID:{}", dto.getId() + ".");
            return ResponseEntity.ok(clienteResponseModelAssembler.toModel(dto));
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
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado registro de cliente según ID ingresado.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente por ID", description = "Actualiza información de registro perteneciente a cliente según ID ingresado.")
    public ResponseEntity<EntityModel<ClienteResponseDTO>> update(@Parameter(description = "ID de cliente", required = true) @Valid @RequestBody ClienteUpdateDTO dto, @PathVariable Long id){
        String logMsgRequest = "Recibiendo solicitud para actualizar cliente con ID: " + id + ".";
        String logMsg = "Solicitud para actualizar cliente con ID: " + id + ".";
        logger.info(logMsgRequest);
        dto.setId(id);
        ClienteResponseDTO updated = clienteService.update(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updated.getId()).toUri();
        //de componentes de constructor URI // de la actual request //ruta de id // sacar la id del obj creado // transformar a URI.
        logger.info(logMsg + " => actualizado.");
        return ResponseEntity.ok().location(location).body(clienteResponseModelAssembler.toModel(updated));
        //devuelve el estado y la locación //devuelve el objeto creado
    }


    //DELETE:

    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Se ha eliminado registro.",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Sintáxis incorrecta",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado registro de cliente según ID ingresado.",
                    content = @Content(schema = @Schema(hidden = true))
            )
    }
    )
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente por ID.", description = "Eliminar el registro perteneciente a un cliente según ID ingresado.")
    public ResponseEntity<Void> deleteById(@Parameter(description = "ID de cliente", required = true) @PathVariable Long id){
        String logMsgRequest = "Recibiendo solicitud para borrar cliente con ID: " + id + ".";
        String logMsg = "Solicitud para borrar cliente con ID: " + id + ".";
        logger.info(logMsgRequest);
        if(clienteService.deleteClienteById(id)){
            logger.info(logMsg + " => encontrado y borrado.");
            return ResponseEntity.noContent().build();
        }
        logger.info(logMsg + " => no encontrado.");
        return ResponseEntity.notFound().build();
    }
}
