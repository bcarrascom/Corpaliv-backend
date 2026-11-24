package cl.corpaliv.programas.repository;

import cl.corpaliv.programas.model.Programa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramaRepository extends JpaRepository<Programa, Long> {

    // Buscar programas publicados que además estén activos.
    List<Programa> findByEstadoPublicacionAndActivoTrue(String estadoPublicacion);
}

