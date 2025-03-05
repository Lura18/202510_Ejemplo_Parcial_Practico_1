package co.edu.uniandes.dse.parcialprueba.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialprueba.entities.ConsultaMedicaEntity;
import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialprueba.repositories.ConsultaMedicaRepository;
import co.edu.uniandes.dse.parcialprueba.repositories.PacienteRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class PacienteConsultaMedicaService {
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaMedicaRepository consultaMedicaRepository;  

    @Transactional
    //Agregar una consulta médica a un paciente
    public ConsultaMedicaEntity addConsulta(Long pacienteId, Long consultaMedicaId) throws EntityNotFoundException {
        log.info("Inicia proceso de agregar consulta médica al paciente con id {}", pacienteId);
        Optional <PacienteEntity> paciente = pacienteRepository.findById(pacienteId);
        if (paciente.isEmpty()) {
            throw new EntityNotFoundException("No existe un paciente con el id dado."); 
        }
        Optional <ConsultaMedicaEntity> consultaMedica = consultaMedicaRepository.findById(consultaMedicaId);
        if (consultaMedica.isEmpty()) {
            throw new EntityNotFoundException("No existe una consulta médica con el id dado."); 
        }
        for (ConsultaMedicaEntity consulta : paciente.get().getConsultas()) {
            if (consulta.getFecha().equals(consultaMedica.get().getFecha())) {
                throw new EntityNotFoundException("El paciente ya tiene una consulta médica asignada en la misma fecha.");
            }
        }
        consultaMedica.get().setPaciente(paciente.get());   
        log.info("Termina proceso de agregar una consulta médica al paciente con id = {0}", pacienteId);
        return consultaMedica.get();
    }

    //getConsultasProgramadas: este método recibe como parámetro el id del paciente y devuelve las consultas asignadas cuyas fechas son superiores a la fecha actual.
    @Transactional
    public List<ConsultaMedicaEntity> getConsultasProgramadas(Long pacienteId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar las consultas programadas del paciente con id {}", pacienteId);
        Optional <PacienteEntity> paciente = pacienteRepository.findById(pacienteId);
        if (paciente.isEmpty()) {
            throw new EntityNotFoundException("No existe un paciente con el id dado."); 
        }
        List<ConsultaMedicaEntity> consultasProgramadas = paciente.get().getConsultas();
        List<ConsultaMedicaEntity> consultasProgramadasAux = new ArrayList<>();

        for (ConsultaMedicaEntity consulta : consultasProgramadas) {
            if (consulta.getFecha().after(new Date())) {
                consultasProgramadasAux.add(consulta);
            }
        }
        log.info("Termina proceso de consultar las consultas programadas del paciente con id = {0}", pacienteId);
        return consultasProgramadasAux;
    }
}
