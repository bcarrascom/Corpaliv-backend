package cl.corpaliv.colaboraciones.exception;

import java.util.Map;

// Cuerpo JSON de toda respuesta de error. Ejemplo:
// { "mensaje": "Datos inválidos", "errores": { "correo": "El correo es obligatorio" } }
// "errores" va vacío cuando el problema no es de un campo en particular.
public record RespuestaError(String mensaje, Map<String, String> errores) {
}
