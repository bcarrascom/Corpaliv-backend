package cl.corpaliv.productos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Se lanza cuando se pide un producto con un id que no existe.
// @ResponseStatus hace que Spring responda 404 en vez de 500.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(Long id) {
        super("No existe un producto con id " + id);
    }
}
