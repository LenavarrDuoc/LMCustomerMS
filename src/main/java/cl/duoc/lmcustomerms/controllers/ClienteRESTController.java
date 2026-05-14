package cl.duoc.lmcustomerms.controllers;


import cl.duoc.lmcustomerms.dtos.ClienteInputDTO;
import cl.duoc.lmcustomerms.dtos.ClienteOrderResponseDTO;
import cl.duoc.lmcustomerms.dtos.ClienteResponseDTO;
import cl.duoc.lmcustomerms.dtos.ClienteUpdateDTO;
import cl.duoc.lmcustomerms.services.ClienteService;
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
@RequestMapping("/api/v1/clientes")
public class ClienteRESTController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteRESTController.class.getName());

    @Autowired
    private ClienteService clienteService;

    //CREATE:
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> save(@Valid @RequestBody ClienteInputDTO dto){
        String logMsgRequest = "Recibiendo solicitud para crear/guardar cliente.";
        String logMsg = "Solicitud para crear/guardar cliente.";
        logger.info(logMsgRequest);
        ClienteResponseDTO created = clienteService.save(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
                    //de componentes de constructor URI // de la actual request //ruta de id // sacar la id del obj creado // transformar a URI.
        logger.info(logMsg + "=> creado con ID: {}, R.U.N.: {}, correo: {}, fono: {}.", created.getId(), created.getRun(), created.getEmail(), created.getFono());
        return ResponseEntity.created(location).body(created);
                    //devuelve el estado y la locación //devuelve el objeto creado
    }


    //READ:
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> findAll(){
        String logMsgRequest = "Recibiendo solicitud para buscar listado de clientes.";
        String logMsg = "Solicitud para buscar listado de clientes.";
        logger.info(logMsgRequest);
        List<ClienteResponseDTO> listadoDTO = clienteService.findAll();

        if (!listadoDTO.isEmpty()){
            logger.info(logMsg + "=> encontrado(s) y enlistado(s).");
            return ResponseEntity.ok(listadoDTO);
        }
        logger.info(logMsg + "=> sin coincidencias (vacío).");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists-by-id/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
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


    @GetMapping("/by-pnombre/{pnombre}")
    public ResponseEntity<List<ClienteResponseDTO>> findAllByPnombre(@PathVariable String pnombre){
        String logMsgRequest = "Recibiendo solicitud para buscar listado de clientes coincidentes con primer calle: " + pnombre + ".";
        String logMsg = "Solicitud para buscar listado de clientes coincidentes con primer calle: " + pnombre + ".";
        logger.info(logMsgRequest);
        List<ClienteResponseDTO> listadoDTO = clienteService.findAllByPnombre(pnombre);

        if (!listadoDTO.isEmpty()){
            logger.info(logMsg + "=> encontrado(s) y enlistado(s).");
            return ResponseEntity.ok(listadoDTO);
        }
        logger.info(logMsg + "=> sin coincidencias (vacío).");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteOrderResponseDTO> findById(@PathVariable Long id){
        String logMsgRequest = "Recibiendo solicitud para buscar cliente por ID: " + id + ".";
        String logMsg = "Solicitud para buscar cliente por ID: " + id + ".";
        logger.info(logMsgRequest);
        ClienteOrderResponseDTO dto = clienteService.findById(id);
        if (dto != null){
            logger.info(logMsg + "=> encontrado.");
            return ResponseEntity.ok(dto);
        }
        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/by-numrun/{numrun}")
    public ResponseEntity<ClienteResponseDTO> findByNumRun(@PathVariable Integer numrun){
        String logMsgRequest = "Recibiendo solicitud para buscar cliente por R.U.N.: " + numrun + ".";
        String logMsg = "Solicitud para buscar cliente por R.U.N.: " + numrun + ".";
        logger.info(logMsgRequest);
        ClienteResponseDTO dto = clienteService.findByNumRun(numrun);
        if (dto != null){
            logger.info(logMsg + "=> encontrado con ID:{}", dto.getId() + ".");
            return ResponseEntity.ok(dto);
        }
        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<ClienteResponseDTO> findByEmail(@PathVariable String email){
        String logMsgRequest = "Recibiendo solicitud para buscar cliente por correo electrónico: " + email + ".";
        String logMsg = "Solicitud para buscar cliente por correo electrónico: " + email + ".";
        logger.info(logMsgRequest);
        ClienteResponseDTO dto = clienteService.findByEmail(email);
        if (dto != null){
            logger.info(logMsg + "=> encontrado con ID:{}", dto.getId() + ".");
            return ResponseEntity.ok(dto);
        }
        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-fono/{fono}")
    public ResponseEntity<ClienteResponseDTO> findByFono(@PathVariable String fono){
        String logMsgRequest = "Recibiendo solicitud para buscar cliente por teléfono registrado: " + fono + ".";
        String logMsg = "Solicitud para buscar cliente por teléfono registrado: " + fono + ".";
        logger.info(logMsgRequest);
        ClienteResponseDTO dto = clienteService.findByFono(fono);
        if (dto != null){
            logger.info(logMsg + "=> encontrado con ID:{}", dto.getId() + ".");
            return ResponseEntity.ok(dto);
        }
        logger.info(logMsg + "=> no encontrado.");
        return ResponseEntity.notFound().build();
    }


    //UPDATE:
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> update(@Valid @RequestBody ClienteUpdateDTO objAux, @PathVariable Long id){
        String logMsgRequest = "Recibiendo solicitud para actualizar cliente con ID: " + id + ".";
        String logMsg = "Solicitud para actualizar cliente con ID: " + id + ".";
        logger.info(logMsgRequest);
        objAux.setId(id);
        ClienteResponseDTO updated = clienteService.update(objAux);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updated.getId()).toUri();
        //de componentes de constructor URI // de la actual request //ruta de id // sacar la id del obj creado // transformar a URI.
        logger.info(logMsg + " => actualizado.");
        return ResponseEntity.status(200).location(location).body(updated);
        //devuelve el estado y la locación //devuelve el objeto creado
    }


    //DELETE:
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
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
