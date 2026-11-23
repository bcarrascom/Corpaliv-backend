package cl.corpaliv.productos.repository;

import cl.corpaliv.productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

// Acceso a la tabla "productos".
// Spring Data genera la implementación: findAll, findById, save, deleteById, etc.
// <Producto, Long> = entidad que maneja y tipo de su id.
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
