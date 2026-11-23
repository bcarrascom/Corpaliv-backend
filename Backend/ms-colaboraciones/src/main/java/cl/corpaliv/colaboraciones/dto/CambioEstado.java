package cl.corpaliv.colaboraciones.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// Cuerpo del PATCH de estado: { "estado": "Contactado" }.
// No es una tabla: solo transporta el dato que llega en la petición.
public record CambioEstado(
        @NotNull(message = "El estado es obligatorio")
        @Pattern(regexp = "Pendiente|Contactado|Cerrada",
                message = "El estado debe ser Pendiente, Contactado o Cerrada")
        String estado
) {
}
