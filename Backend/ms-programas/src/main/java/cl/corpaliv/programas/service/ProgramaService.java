package cl.corpaliv.programas.service;

import cl.corpaliv.programas.exception.ProgramaNoEncontradoException;
import cl.corpaliv.programas.model.Programa;
import cl.corpaliv.programas.repository.ProgramaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

// Lógica de negocio de los programas.
// El controller llama a esta clase y esta utiliza el repositorio.
@Service
public class ProgramaService {

    private final ProgramaRepository programaRepository;

    // Spring entrega el repositorio mediante inyección de dependencias.
    public ProgramaService(ProgramaRepository programaRepository) {
        this.programaRepository = programaRepository;
    }

    // Obtener todos los programas.
    public List<Programa> listarTodos() {
        return programaRepository.findAll();
    }

    // Buscar un programa por su ID.
    public Programa buscarPorId(Long id) {
        Optional<Programa> encontrado = programaRepository.findById(id);

        if (encontrado.isEmpty()) {
            throw new ProgramaNoEncontradoException(id);
        }

        return encontrado.get();
    }

    // Registrar un nuevo programa.
    public Programa crear(Programa programa) {
        // El ID será generado por la base de datos.
        programa.setId(null);
        return programaRepository.save(programa);
    }

    // Actualizar un programa existente.
    public Programa actualizar(Long id, Programa datosNuevos) {
        Programa existente = buscarPorId(id);

        existente.setNombre(datosNuevos.getNombre());
        existente.setDescripcion(datosNuevos.getDescripcion());
        existente.setBeneficiarios(datosNuevos.getBeneficiarios());
        existente.setActivo(datosNuevos.getActivo());

        return programaRepository.save(existente);
    }

    // Eliminar un programa existente.
    public void eliminar(Long id) {
        Programa existente = buscarPorId(id);
        programaRepository.delete(existente);
    }
}
