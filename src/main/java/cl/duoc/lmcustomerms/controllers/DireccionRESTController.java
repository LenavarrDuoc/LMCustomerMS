package cl.duoc.lmcustomerms.controllers;

import cl.duoc.lmcustomerms.dtos.DireccionInputDTO;
import cl.duoc.lmcustomerms.dtos.DireccionResponseDTO;
import cl.duoc.lmcustomerms.dtos.DireccionUpdateDTO;
import cl.duoc.lmcustomerms.services.DireccionService;
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
public class DireccionRESTController {

    private static final Logger logger = LoggerFactory.getLogger(DireccionRESTController.class.getName());

    @Autowired
    private DireccionService direccionService;

    //CREATE:
    @PostMapping
    public ResponseEntity<DireccionResponseDTO> save(@Valid @RequestBody DireccionInputDTO objAux){
        String logMsg = "Recibiendo solicitud para crear/guardar direccion";
        logger.info(logMsg);
        DireccionResponseDTO created = direccionService.save(objAux);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        //de componentes de constructor URI // de la actual request //ruta de id // sacar la id del obj creado // transformar a URI.
        logger.info(logMsg + "=> creado con ID: {}, y direccion: {}.", created.getId(), (created.getCalle() + created.getNumero() + (created.getNroDepto() == null ? "" : created.getNroDepto()) + created.getComuna() + created.getComuna() + created.getRegion() +  "."));
        return ResponseEntity.created(location).body(created);
        //devuelve el estado y la locación //devuelve el objeto creado
    }


    //READ:
    @GetMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<DireccionResponseDTO> findDireccionById(@PathVariable Long id){
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
    @PutMapping("/{id}")
    public ResponseEntity<DireccionResponseDTO> update(@Valid @RequestBody DireccionUpdateDTO objAux, @PathVariable Long id){
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
    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id){
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
