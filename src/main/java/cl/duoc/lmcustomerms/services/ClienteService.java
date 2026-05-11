package cl.duoc.lmcustomerms.services;

import cl.duoc.lmcustomerms.dtos.ClienteInputDTO;
import cl.duoc.lmcustomerms.dtos.ClienteOrderResponseDTO;
import cl.duoc.lmcustomerms.dtos.ClienteResponseDTO;
import cl.duoc.lmcustomerms.dtos.ClienteUpdateDTO;
import cl.duoc.lmcustomerms.exceptions.ClienteEmailExisteException;
import cl.duoc.lmcustomerms.exceptions.ClienteNumrunExisteException;
import cl.duoc.lmcustomerms.exceptions.IdNoExisteException;
import cl.duoc.lmcustomerms.mappers.ClienteInputMapper;
import cl.duoc.lmcustomerms.mappers.ClienteOrderResponseMapper;
import cl.duoc.lmcustomerms.mappers.ClienteResponseMapper;
import cl.duoc.lmcustomerms.mappers.ClienteUpdateMapper;
import cl.duoc.lmcustomerms.models.Cliente;
import cl.duoc.lmcustomerms.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
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
    public ClienteResponseDTO save(ClienteInputDTO objAux){
        if (clienteRepository.existsByNumrun(objAux.getNumrun())){
            throw new ClienteNumrunExisteException("R.U.N. de cliente ya existe.");
        } else if (clienteRepository.existsByEmail(objAux.getEmail())){
            throw new ClienteEmailExisteException("Correo electrónico de cliente ya existe.");
        }

        //permite dejar la primera dirección ingresada como la por defecto para el cliente.

        return clienteResponseMapper.toDto(clienteRepository.save(clienteInputMapper.toEntity(objAux)));
    }


    //READ:
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> findAll(){
        return clienteRepository.findAll().stream().map(clienteResponseMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ClienteOrderResponseDTO findById(Long id){
        return clienteOrderResponseMapper.toDto(clienteRepository.findById(id).orElse(null));
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO findByNumRun(Integer numrun){
        return clienteResponseMapper.toDto(clienteRepository.findByNumrun(numrun));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> findAllByPnombre(String pnombre){
        return clienteRepository.findAllByPnombre(pnombre).stream().map(clienteResponseMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO findByEmail(String email){
        return clienteResponseMapper.toDto(clienteRepository.findByEmail(email));
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO findByFono(String fono){
        return clienteResponseMapper.toDto(clienteRepository.findByFono(fono));
    }


    //UPDATE:
    @Transactional
    public ClienteResponseDTO update(ClienteUpdateDTO objAux){

        //TODO: Se debe arregllar: expone a la entidad Cliente directamente en Service cuando se podría procesar en el mapper.
        Cliente pio = clienteRepository.findById(objAux.getId()).orElseThrow(() -> new IdNoExisteException("ID de cliente no existe."));
        return clienteResponseMapper.toDto(clienteRepository.save(clienteUpdateMapper.toEntity(pio, objAux)));
    }

    //DELETE:
    @Transactional
    public Boolean deleteClienteById(Long id){
        Boolean centinela = false;
        if (clienteRepository.existsById(id)){
            clienteRepository.deleteById(id);
            centinela = true;
        }
        return centinela;
    }
    /*TODO: se debe agregar un crud parcial de gestión de direcciones para agregar, borrar, actualizar dirección de un client
        y establecer la dirección por defecto del cliente, además de devolver dirección elegiada para consumo de venta/boleta */
}
