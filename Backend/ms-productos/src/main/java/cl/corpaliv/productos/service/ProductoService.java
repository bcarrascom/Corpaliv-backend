package cl.corpaliv.productos.service;

import cl.corpaliv.productos.exception.ProductoNoEncontradoException;
import cl.corpaliv.productos.model.Producto;
import cl.corpaliv.productos.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

// Lógica de negocio de los productos. El controller llama a esta clase,
// y esta clase es la única que usa el repositorio.
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Spring entrega el repositorio por el constructor (inyección de dependencias).
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Long id) {
        Optional<Producto> encontrado = productoRepository.findById(id);
        if (encontrado.isEmpty()) {
            throw new ProductoNoEncontradoException(id);
        }
        return encontrado.get();
    }

    public Producto crear(Producto producto) {
        // Se ignora cualquier id que venga: lo asigna la base de datos.
        producto.setId(null);
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto datosNuevos) {
        Producto existente = buscarPorId(id);
        existente.setNombre(datosNuevos.getNombre());
        existente.setDescripcion(datosNuevos.getDescripcion());
        existente.setPrecio(datosNuevos.getPrecio());
        existente.setCategoria(datosNuevos.getCategoria());
        existente.setDisponible(datosNuevos.getDisponible());
        return productoRepository.save(existente);
    }

    public void eliminar(Long id) {
        // Si no existe, buscarPorId lanza la excepción (404).
        Producto existente = buscarPorId(id);
        productoRepository.delete(existente);
    }
}
