package co.edu.uniandes.dse.parcialprueba.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class ConsultaMedicaEntity extends BaseEntity{

    private String causa;    
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @PodamExclude
    @ManyToOne
    private PacienteEntity paciente;
    
}
