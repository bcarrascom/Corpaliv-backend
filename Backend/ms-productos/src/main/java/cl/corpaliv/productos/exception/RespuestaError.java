package cl.corpaliv.productos.exception;

import java.util.Map;

// Cuerpo JSON de toda respuesta de error. Ejemplo:
// { "mensaje": "Datos inválidos", "errores": { "nombre": "El nombre es obligatorio" } }
// "errores" va vacío cuando el problema no es de un campo en particular.
public record RespuestaError(String mensaje, Map<String, String> errores) {
}
