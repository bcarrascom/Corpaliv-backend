package cl.corpaliv.programas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

// Programa institucional de CORPALIV.
// Hibernate crea la tabla "programas" a partir de esta clase.
// Las validaciones se ejecutan cuando el controller recibe un @Valid.
@Entity
@Table(name = "programas")
public class Programa {

    // Llave primaria; MySQL la genera automáticamente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    @Column(nullable = false, length = 500)
    private String descripcion;

    @NotNull(message = "Indica la cantidad de beneficiarios")
    @PositiveOrZero(message = "Los beneficiarios no pueden ser negativos")
    @Column(nullable = false)
    private Integer beneficiarios;

    @NotNull(message = "Indica si el programa está activo")
    @Column(nullable = false)
    private Boolean activo;

    // JPA necesita un constructor vacío.
    public Programa() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getBeneficiarios() {
        return beneficiarios;
    }

    public void setBeneficiarios(Integer beneficiarios) {
        this.beneficiarios = beneficiarios;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}