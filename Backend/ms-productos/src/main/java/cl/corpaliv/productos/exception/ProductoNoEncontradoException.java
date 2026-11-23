package cl.corpaliv.productos.exception;

// Se lanza cuando se pide un producto con un id que no existe.
// ManejadorErrores la transforma en una respuesta 404.
public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(Long id) {
        super("No existe un producto con id " + id);
    }
}
