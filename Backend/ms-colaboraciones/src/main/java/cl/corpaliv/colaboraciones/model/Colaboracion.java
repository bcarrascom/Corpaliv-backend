package cl.corpaliv.colaboraciones.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

// Solicitud de colaboración enviada desde la app.
// Las reglas son las mismas de ColaboracionValidador en la app Android.
@Entity
@Table(name = "colaboraciones")
public class Colaboracion {

    public static final String ESTADO_INICIAL = "Pendiente";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Empieza y termina con letra; en medio solo letras y espacios (3 a 100 caracteres).
    @NotNull(message = "El nombre es obligatorio")
    @Pattern(regexp = "^\\p{L}[\\p{L} ]{1,98}\\p{L}$",
            message = "El nombre debe tener al menos 3 letras, sin números ni símbolos")
    @Column(nullable = false, length = 100)
    private String nombreContacto;

    @NotBlank(message = "La organización es obligatoria")
    @Size(max = 100, message = "La organización no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String organizacion;

    @NotBlank(message = "El correo es obligatorio")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Ingresa un correo válido, por ejemplo nombre@correo.cl")
    @Column(nullable = false, length = 100)
    private String correo;

    @NotNull(message = "Elige un tipo de colaboración")
    @Pattern(regexp = "Donación|Alianza|Voluntariado|Compra de productos|Capacitación",
            message = "Tipo de colaboración no válido")
    @Column(nullable = false, length = 30)
    private String tipoColaboracion;

    @NotNull(message = "La cantidad de personas es obligatoria")
    @Min(value = 1, message = "Debe ser entre 1 y 500 personas")
    @Max(value = 500, message = "Debe ser entre 1 y 500 personas")
    @Column(nullable = false)
    private Integer cantidadPersonas;

    // En el JSON viaja como "dd/MM/yyyy", igual que en la app. En MySQL es tipo DATE.
    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha no puede ser anterior a hoy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate fechaContacto;

    @NotNull(message = "Debes aceptar ser contactado para enviar")
    @AssertTrue(message = "Debes aceptar ser contactado para enviar")
    @Column(nullable = false)
    private Boolean aceptaContacto;

    // Seguimiento del equipo de vinculación: Pendiente, Contactado o Cerrada.
    // Lo asigna el servidor; solo cambia con PATCH /api/colaboraciones/{id}/estado.
    @Column(nullable = false, length = 20)
    private String estado;

    // JPA necesita un constructor vacío para crear el objeto al leer de la BD.
    public Colaboracion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(String organizacion) {
        this.organizacion = organizacion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipoColaboracion() {
        return tipoColaboracion;
    }

    public void setTipoColaboracion(String tipoColaboracion) {
        this.tipoColaboracion = tipoColaboracion;
    }

    public Integer getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(Integer cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }

    public LocalDate getFechaContacto() {
        return fechaContacto;
    }

    public void setFechaContacto(LocalDate fechaContacto) {
        this.fechaContacto = fechaContacto;
    }

    public Boolean getAceptaContacto() {
        return aceptaContacto;
    }

    public void setAceptaContacto(Boolean aceptaContacto) {
        this.aceptaContacto = aceptaContacto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
