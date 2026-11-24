package cl.corpaliv.programas.controller;

import cl.corpaliv.programas.model.Programa;
import cl.corpaliv.programas.service.ProgramaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programas")
@CrossOrigin(origins = "*")
public class ProgramaController {

    private final ProgramaService programaService;

    public ProgramaController(ProgramaService programaService) {
        this.programaService = programaService;
    }

    // Obtener todos los programas
    @GetMapping
    public ResponseEntity<List<Programa>> listarTodos() {
        return ResponseEntity.ok(programaService.listarTodos());
    }

    // Obtener solamente los programas publicados y activos
    @GetMapping("/publicados")
    public ResponseEntity<List<Programa>> listarPublicados() {
        return ResponseEntity.ok(programaService.listarPublicados());
    }

    // Buscar un programa por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Programa> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(programaService.buscarPorId(id));
    }

    // Crear un programa
    @PostMapping
    public ResponseEntity<Programa> crear(@Valid @RequestBody Programa programa) {
        Programa nuevoPrograma = programaService.crear(programa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPrograma);
    }

    // Actualizar un programa
    @PutMapping("/{id}")
    public ResponseEntity<Programa> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Programa programa) {

        return ResponseEntity.ok(programaService.actualizar(id, programa));
    }

    // Eliminar un programa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        programaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
