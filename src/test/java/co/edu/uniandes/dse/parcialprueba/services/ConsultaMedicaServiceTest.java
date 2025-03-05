package co.edu.uniandes.dse.parcialprueba.services;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.*;

import co.edu.uniandes.dse.parcialprueba.entities.ConsultaMedicaEntity;
import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.ArrayList;
import java.util.Date;
import co.edu.uniandes.dse.parcialprueba.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;

@DataJpaTest
@Transactional
@Import(ConsultaMedicaService.class)
public class ConsultaMedicaServiceTest {
    @Autowired
    private ConsultaMedicaService consultaMedicaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<ConsultaMedicaEntity> consultasList = new ArrayList<ConsultaMedicaEntity>();
    private PacienteEntity paciente;
    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

    private void clearData() {
		entityManager.getEntityManager().createQuery("delete from ConsultaMedicaEntity").executeUpdate();
	}

    private void insertData() {
        PacienteEntity pEntity = factory.manufacturePojo(PacienteEntity.class);
        entityManager.persist(pEntity);

		for (int i = 0; i < 3; i++) {
			ConsultaMedicaEntity entity = factory.manufacturePojo(ConsultaMedicaEntity.class);
            entity.setPaciente(pEntity);    
			entityManager.persist(entity);
			consultasList.add(entity);  
		}
	}

    @Test
    void createConsultaMedicaTest() throws EntityNotFoundException, IllegalOperationException {
        ConsultaMedicaEntity entity = factory.manufacturePojo(ConsultaMedicaEntity.class);
        entity.setPaciente(paciente);
        //Set fecha para que sea después de la fecha actual
        entity.setFecha(new Date(System.currentTimeMillis() + 100000));
        ConsultaMedicaEntity result = consultaMedicaService.createConsultaMedica(entity);
        assertNotNull(result);
        ConsultaMedicaEntity newEntity = entityManager.find(ConsultaMedicaEntity.class, result.getId());
        assertEquals(entity.getId(), newEntity.getId());
        assertEquals(entity.getFecha(), newEntity.getFecha());
        assertEquals(entity.getCausa(), newEntity.getCausa());
    }

}
