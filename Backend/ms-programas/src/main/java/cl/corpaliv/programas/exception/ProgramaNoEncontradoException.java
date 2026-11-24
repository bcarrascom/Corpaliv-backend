package cl.corpaliv.programas.exception;

// Se lanza cuando se solicita un programa con un ID que no existe.
// ManejadorErrores la transformará en una respuesta 404.
public class ProgramaNoEncontradoException extends RuntimeException {

    public ProgramaNoEncontradoException(Long id) {
        super("No existe un programa con id " + id);
    }
}
