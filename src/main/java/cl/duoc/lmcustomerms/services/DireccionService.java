package cl.duoc.lmcustomerms.services;

import cl.duoc.lmcustomerms.dtos.DireccionInputDTO;
import cl.duoc.lmcustomerms.dtos.DireccionResponseDTO;
import cl.duoc.lmcustomerms.dtos.DireccionUpdateDTO;
import cl.duoc.lmcustomerms.exceptions.DireccionNombreExisteException;
import cl.duoc.lmcustomerms.exceptions.IdNoExisteException;
import cl.duoc.lmcustomerms.mappers.DireccionInputMapper;
import cl.duoc.lmcustomerms.mappers.DireccionResponseMapper;
import cl.duoc.lmcustomerms.mappers.DireccionUpdateMapper;
import cl.duoc.lmcustomerms.repositories.DireccionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DireccionService {

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private DireccionResponseMapper direccionResponseMapper;

    @Autowired
    private DireccionInputMapper direccionInputMapper;

    @Autowired
    private DireccionUpdateMapper direccionUpdateMapper;

    //CREATE/UPDATE:
    public DireccionResponseDTO save(DireccionInputDTO objAux){
        if (direccionRepository.existsDireccionByCalleAndNumeroAndNroDepto(objAux.getCalle(), objAux.getNumero(),  (objAux.getNroDepto() == null ? 0 : objAux.getNroDepto()))) {
            throw new DireccionNombreExisteException("Nombre de direccion ya existe.");
            //       } else if (direccionRepository.existsByEmail(objAux.getEmail())){
            //           throw new DireccionEmailExisteException("Correo electrónico de direccion ya existe.");
        }

        return direccionResponseMapper.toDto(direccionRepository.save(direccionInputMapper.toEntity(objAux)));
    }

    //READ:
    public List<DireccionResponseDTO> findAll(){
        return direccionRepository.findAll().stream().map(direccionResponseMapper::toDto).toList();
    }

    public DireccionResponseDTO findById(Long id){
        return direccionResponseMapper.toDto(direccionRepository.findById(id).orElse(null));
    }
    
    public DireccionResponseDTO findByCalle(String calle){
        return direccionResponseMapper.toDto(direccionRepository.findByCalle((calle)));
    }

    //UPDATE:
    public DireccionResponseDTO update(DireccionUpdateDTO objAux){
        if (!direccionRepository.existsById(objAux.getId())){
            throw new IdNoExisteException("ID de direccion no existe.");
        }
        return direccionResponseMapper.toDto(direccionRepository.save(direccionUpdateMapper.toEntity(objAux)));
    }


    //DELETE:
    public Boolean deleteDireccionById(Long id){
        Boolean centinela = false;
        if (direccionRepository.existsById(id)){
            direccionRepository.deleteById(id);
            centinela = true;
        }
        return centinela;
    }
}
