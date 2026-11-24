package cl.corpaliv.programas.controller;

import cl.corpaliv.programas.model.Programa;
import cl.corpaliv.programas.service.ProgramaService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/programas")
public class ProgramaController {

    private final ProgramaService programaService;

    // Inyección de dependencias.
    public ProgramaController(ProgramaService programaService) {
        this.programaService = programaService;
    }

    // GET: listar todos los programas.
    @GetMapping
    public List<Programa> listarTodos() {
        return programaService.listarTodos();
    }

    // GET: buscar un programa por ID.
    @GetMapping("/{id}")
    public Programa buscarPorId(@PathVariable Long id) {
        return programaService.buscarPorId(id);
    }

    // POST: registrar un nuevo programa.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Programa crear(@Valid @RequestBody Programa programa) {
        return programaService.crear(programa);
    }

    // PUT: actualizar un programa existente.
    @PutMapping("/{id}")
    public Programa actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Programa programa) {
        return programaService.actualizar(id, programa);
    }

    // DELETE: eliminar un programa.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        programaService.eliminar(id);
    }
}
