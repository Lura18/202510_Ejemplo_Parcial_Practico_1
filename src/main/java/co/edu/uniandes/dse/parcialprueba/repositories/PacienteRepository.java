package co.edu.uniandes.dse.parcialprueba.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.uniandes.dse.parcialprueba.entities.PacienteEntity;

public interface PacienteRepository extends JpaRepository<PacienteEntity, Long> {

} 