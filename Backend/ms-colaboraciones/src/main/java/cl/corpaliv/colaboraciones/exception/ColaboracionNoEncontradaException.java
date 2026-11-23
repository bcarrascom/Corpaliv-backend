package cl.corpaliv.colaboraciones.exception;

// Se lanza cuando se pide una colaboración con un id que no existe.
// ManejadorErrores la transforma en una respuesta 404.
public class ColaboracionNoEncontradaException extends RuntimeException {

    public ColaboracionNoEncontradaException(Long id) {
        super("No existe una colaboración con id " + id);
    }
}
