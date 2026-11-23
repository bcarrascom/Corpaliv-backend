package cl.corpaliv.colaboraciones.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Atrapa las excepciones de todos los controllers y las convierte en JSON con su código HTTP.
@RestControllerAdvice
public class ManejadorErrores {

    // 404: el id pedido no existe.
    @ExceptionHandler(ColaboracionNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RespuestaError colaboracionNoEncontrada(ColaboracionNoEncontradaException excepcion) {
        return new RespuestaError(excepcion.getMessage(), Map.of());
    }

    // 400: falló alguna validación (@NotBlank, @Email, @Min, etc.).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RespuestaError datosInvalidos(MethodArgumentNotValidException excepcion) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError error : excepcion.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        return new RespuestaError("Datos inválidos", errores);
    }

    // 400: el cuerpo no es un JSON válido (por ejemplo, una fecha que no es dd/MM/yyyy).
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RespuestaError jsonInvalido(HttpMessageNotReadableException excepcion) {
        return new RespuestaError("El cuerpo de la petición no es un JSON válido", Map.of());
    }
}
