package co.edu.uniandes.dse.parcialprueba.services;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.ArrayList;
import java.util.Date;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import co.edu.uniandes.dse.parcialprueba.entities.ConsultaMedicaEntity;
import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;



@DataJpaTest
@Transactional
@Import(PacienteConsultaMedicaService.class)
public class PacienteConsultaMedicaServiceTest {
    @Autowired
    private PacienteConsultaMedicaService pacienteConsultaMedicaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<PacienteEntity> pacientesList = new ArrayList<PacienteEntity>();
    private List<ConsultaMedicaEntity> consultasList = new ArrayList<ConsultaMedicaEntity>();


	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ConsultaMedicaEntity");   
        entityManager.getEntityManager().createQuery("delete from PacienteEntity");
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ConsultaMedicaEntity consulta = factory.manufacturePojo(ConsultaMedicaEntity.class);
            entityManager.persist(consulta);
            consultasList.add(consulta);
        }
            
        
        for (int i = 0; i < 3; i++) {
            PacienteEntity pEntity = factory.manufacturePojo(PacienteEntity.class);
            entityManager.persist(pEntity);
            pacientesList.add(pEntity);
            if (i == 0) {
                consultasList.get(i).setPaciente(pEntity);
                pEntity.getConsultas().add(consultasList.get(i));
            }
        }
    }

    //Test addConsulta
    @Test
    void testAddConsulta() throws EntityNotFoundException {
        PacienteEntity paciente = pacientesList.get(0);
        ConsultaMedicaEntity consulta = factory.manufacturePojo(ConsultaMedicaEntity.class);
        entityManager.persist(consulta);
        ConsultaMedicaEntity response = pacienteConsultaMedicaService.addConsulta(paciente.getId(), consulta.getId());
        
        assertNotNull(response);
        assertEquals(consulta.getId(), response.getId());
    }

    //Test addConsulta fallido: el paciente no puede tener, dentro de sus consultas asignadas, consultas cuyas fechas coincidan
    @Test
    void testAddConsultaFail() {
        PacienteEntity paciente = pacientesList.get(0);
        ConsultaMedicaEntity consulta = consultasList.get(0);
        consulta.setFecha(new Date(System.currentTimeMillis() + 1));
        ConsultaMedicaEntity consulta2 = consultasList.get(1);
        consulta2.setFecha(new Date(System.currentTimeMillis() + 1));
        paciente.getConsultas().add(consulta);
        paciente.getConsultas().add(consulta2);
        entityManager.persist(paciente);
        assertThrows(EntityNotFoundException.class, () -> pacienteConsultaMedicaService.addConsulta(paciente.getId(), consulta.getId()));
    }

    //Test getConsultasProgramadas
    @Test
    void testGetConsultasProgramadas() throws EntityNotFoundException {
        PacienteEntity paciente = pacientesList.get(0);
        ConsultaMedicaEntity consulta = factory.manufacturePojo(ConsultaMedicaEntity.class);
        consulta.setFecha(new Date(System.currentTimeMillis() + 100000));
        paciente.getConsultas().add(consulta);
        entityManager.persist(paciente);
        List<ConsultaMedicaEntity> response = pacienteConsultaMedicaService.getConsultasProgramadas(paciente.getId());
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(consulta.getId(), response.get(0).getId());
    }

    //Test getConsultasProgramadas no hay ninguna consulta programada para el paciente
    @Test
    void testGetConsultasProgramadasNula() throws EntityNotFoundException {
        PacienteEntity paciente = pacientesList.get(0);
        ConsultaMedicaEntity consulta = factory.manufacturePojo(ConsultaMedicaEntity.class);
        consulta.setFecha(new Date(System.currentTimeMillis() - 100000));
        paciente.getConsultas().add(consulta);
        entityManager.persist(paciente);
        List<ConsultaMedicaEntity> response = pacienteConsultaMedicaService.getConsultasProgramadas(paciente.getId());
        assertNotNull(response);
        assertEquals(0, response.size()); 
    }

}
