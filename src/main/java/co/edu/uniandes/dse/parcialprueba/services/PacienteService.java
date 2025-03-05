package co.edu.uniandes.dse.parcialprueba.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;
import co.edu.uniandes.dse.parcialprueba.repositories.PacienteRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;

    @Transactional
    //createPaciente
    public PacienteEntity createPaciente(PacienteEntity paciente) {
    log.info("Inicia proceso de creación de un paciente.");
        return pacienteRepository.save(paciente);
    }
}

