package co.edu.uniandes.dse.parcialprueba.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.ConsultaMedicaEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialprueba.repositories.ConsultaMedicaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsultaMedicaService {
    @Autowired
    private ConsultaMedicaRepository consultaMedicaRepository;

    @Transactional
    //createConsultaMedica
    public ConsultaMedicaEntity createConsultaMedica(ConsultaMedicaEntity consultaMedica) throws IllegalOperationException {

    if(!consultaMedica.getFecha().after(new Date())) {
        throw new IllegalOperationException("La fecha de la consulta es menor a la fecha actual.");
    }
    log.info("Inicia proceso de creación de una consulta médica.");
        return consultaMedicaRepository.save(consultaMedica);
    }
}
