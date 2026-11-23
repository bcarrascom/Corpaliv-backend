package cl.corpaliv.colaboraciones.controller;

import cl.corpaliv.colaboraciones.dto.CambioEstado;
import cl.corpaliv.colaboraciones.model.Colaboracion;
import cl.corpaliv.colaboraciones.service.ColaboracionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// Endpoints REST de las solicitudes de colaboración.
@RestController
@RequestMapping("/api/colaboraciones")
public class ColaboracionController {

    private final ColaboracionService colaboracionService;

    public ColaboracionController(ColaboracionService colaboracionService) {
        this.colaboracionService = colaboracionService;
    }

    // GET /api/colaboraciones                     -> todas
    // GET /api/colaboraciones?correo=ana@mail.cl  -> solo las de ese correo
    @GetMapping
    public List<Colaboracion> listar(@RequestParam(required = false) String correo) {
        if (correo == null || correo.isBlank()) {
            return colaboracionService.listarTodas();
        }
        return colaboracionService.listarPorCorreo(correo);
    }

    // GET /api/colaboraciones/5
    @GetMapping("/{id}")
    public Colaboracion buscarPorId(@PathVariable Long id) {
        return colaboracionService.buscarPorId(id);
    }

    // POST /api/colaboraciones
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Colaboracion crear(@Valid @RequestBody Colaboracion colaboracion) {
        return colaboracionService.crear(colaboracion);
    }

    // PUT /api/colaboraciones/5  (edita los datos, no el estado)
    @PutMapping("/{id}")
    public Colaboracion actualizar(@PathVariable Long id, @Valid @RequestBody Colaboracion colaboracion) {
        return colaboracionService.actualizar(id, colaboracion);
    }

    // PATCH /api/colaboraciones/5/estado  con { "estado": "Contactado" }
    @PatchMapping("/{id}/estado")
    public Colaboracion cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambioEstado cambio) {
        return colaboracionService.cambiarEstado(id, cambio.estado());
    }

    // DELETE /api/colaboraciones/5
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        colaboracionService.eliminar(id);
    }
}
