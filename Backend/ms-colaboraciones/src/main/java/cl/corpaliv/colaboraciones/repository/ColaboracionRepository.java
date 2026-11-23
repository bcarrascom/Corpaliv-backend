package cl.corpaliv.colaboraciones.repository;

import cl.corpaliv.colaboraciones.model.Colaboracion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// Acceso a la tabla "colaboraciones".
// Spring Data arma el SQL a partir del nombre de cada método.
public interface ColaboracionRepository extends JpaRepository<Colaboracion, Long> {

    // SELECT * FROM colaboraciones ORDER BY id DESC  (las más nuevas primero)
    List<Colaboracion> findAllByOrderByIdDesc();

    // SELECT * FROM colaboraciones WHERE correo = ? ORDER BY id DESC
    List<Colaboracion> findByCorreoOrderByIdDesc(String correo);
}
