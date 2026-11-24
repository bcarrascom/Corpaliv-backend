package cl.corpaliv.programas.repository;

import cl.corpaliv.programas.model.Programa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository encargado de las operaciones de acceso a datos de Programa.
@Repository
public interface ProgramaRepository extends JpaRepository<Programa, Long> {

}
