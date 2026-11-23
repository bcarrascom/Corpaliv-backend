package cl.corpaliv.productos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

// Producto o servicio del catálogo de Alba Lab.
// Hibernate crea la tabla "productos" a partir de esta clase.
// Las anotaciones de validación se revisan cuando el controller recibe un @Valid.
@Entity
@Table(name = "productos")
public class Producto {

    // Llave primaria; MySQL la genera sola (AUTO_INCREMENT).
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

    // Precio en pesos chilenos. null = precio a convenir (servicios).
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Integer precio;

    // "Producto" o "Servicio".
    @NotNull(message = "La categoría es obligatoria")
    @Pattern(regexp = "Producto|Servicio", message = "La categoría debe ser Producto o Servicio")
    @Column(nullable = false, length = 20)
    private String categoria;

    @NotNull(message = "Indica si está disponible")
    @Column(nullable = false)
    private Boolean disponible;

    // JPA necesita un constructor vacío para crear el objeto al leer de la BD.
    public Producto() {
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

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}
