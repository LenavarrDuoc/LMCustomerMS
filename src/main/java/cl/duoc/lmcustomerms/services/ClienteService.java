package cl.duoc.lmcustomerms.services;

import cl.duoc.lmcustomerms.dtos.ClienteInputDTO;
import cl.duoc.lmcustomerms.dtos.ClienteOrderResponseDTO;
import cl.duoc.lmcustomerms.dtos.ClienteResponseDTO;
import cl.duoc.lmcustomerms.dtos.ClienteUpdateDTO;
import cl.duoc.lmcustomerms.exceptions.*;
import cl.duoc.lmcustomerms.mappers.ClienteInputMapper;
import cl.duoc.lmcustomerms.mappers.ClienteOrderResponseMapper;
import cl.duoc.lmcustomerms.mappers.ClienteResponseMapper;
import cl.duoc.lmcustomerms.mappers.ClienteUpdateMapper;
import cl.duoc.lmcustomerms.models.Cliente;
import cl.duoc.lmcustomerms.repositories.ClienteRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Service
@Validated
//@Transactional usar transactional para cada función en vez de el servicio en general permite optimizar las funciones
//@Es mejor importar @Transactional de la librería de spring que la de jakartas ya que la de sprign trae funcionalidades más potentes como las de readOnly para métodos que solo consultan datos.
public class ClienteService {



    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteResponseMapper clienteResponseMapper;

    @Autowired
    private ClienteOrderResponseMapper clienteOrderResponseMapper;

    @Autowired
    private ClienteInputMapper clienteInputMapper;
    @Autowired
    private ClienteUpdateMapper clienteUpdateMapper;

    //CREATE:
    @Transactional
    public ClienteResponseDTO save(ClienteInputDTO dto){
        if (clienteRepository.existsByNumrun(dto.getNumrun())){
            throw new ClienteNumrunExisteException("R.U.N. de cliente ya existe.");
        } else if (clienteRepository.existsByEmail(dto.getEmail())){
            throw new ClienteEmailExisteException("Correo electrónico de cliente ya existe.");
        }

        //permite dejar la primera dirección ingresada como la por defecto para el cliente.

        return clienteResponseMapper.toDto(clienteRepository.save(clienteInputMapper.toEntity(dto)));

    }


    //READ:
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> findAll(){
        return clienteRepository.findAll().stream().map(clienteResponseMapper::toDto).toList();
    }

    @Transactional
    public Boolean existsById(Long id){
        return clienteRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public ClienteOrderResponseDTO findById(Long id){
        return clienteOrderResponseMapper.toDto(clienteRepository.findById(id).orElseThrow(() -> new IdNoExisteException("ID de cliente no existe."))) ;
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO findByNumRun(Integer numRun){

        Cliente ent = clienteRepository.findByNumrun(numRun);
        if (ent == null){
            throw new ClienteNumrunNoExisteException("R.U.N. de cliente no existe.");
        }
        return clienteResponseMapper.toDto(ent);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> findAllByPnombre(String pnombre){

        return clienteRepository.findAllByPnombre(pnombre).stream().map(clienteResponseMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO findByEmail(@Email(message = "Entrada no tiene formato de correo.") String email){
        Cliente ent = clienteRepository.findByEmail(email);
        if (ent == null){
            throw new ClienteEmailNoExisteException("Correo de cliente no existe.");
        }
        return clienteResponseMapper.toDto(ent);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO findByFono(String fono){
        Cliente ent = clienteRepository.findByFono(fono);
        if (ent == null){
            throw new ClienteFonoNoExisteException("Fono de cliente no existe.");
        }
        return clienteResponseMapper.toDto(ent);
    }


    //UPDATE:
    @Transactional
    public ClienteResponseDTO update(ClienteUpdateDTO dto){

        Cliente ent = clienteRepository.findById(dto.getId()).orElseThrow(() -> new IdNoExisteException("ID de cliente no existe."));
        return clienteResponseMapper.toDto(clienteRepository.save(clienteUpdateMapper.toEntity(ent, dto)));
    }

    //DELETE:
    @Transactional
    public Boolean deleteClienteById(Long id){
        Boolean centinela = false;
        if (clienteRepository.existsById(id)){
            clienteRepository.deleteById(id);
            centinela = true;
        } else {
            throw new IdNoExisteException("ID de cliente no existe.");
        }
        return centinela;
    }
}
