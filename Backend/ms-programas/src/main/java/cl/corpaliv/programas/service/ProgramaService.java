package cl.corpaliv.programas.service;

import cl.corpaliv.programas.exception.ProgramaNoEncontradoException;
import cl.corpaliv.programas.model.Programa;
import cl.corpaliv.programas.repository.ProgramaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramaService {

    private final ProgramaRepository programaRepository;

    public ProgramaService(ProgramaRepository programaRepository) {
        this.programaRepository = programaRepository;
    }

    // Obtener todos los programas
    public List<Programa> listarTodos() {
        return programaRepository.findAll();
    }

    // Obtener solamente los programas publicados y activos
    public List<Programa> listarPublicados() {
        return programaRepository.findByEstadoPublicacionAndActivoTrue("PUBLICADO");
    }

    // Buscar un programa por su ID
    public Programa buscarPorId(Long id) {
        return programaRepository.findById(id)
                .orElseThrow(() -> new ProgramaNoEncontradoException(id));
    }

    // Crear un programa
    public Programa crear(Programa programa) {
        programa.setId(null);
        return programaRepository.save(programa);
    }

    // Actualizar un programa existente
    public Programa actualizar(Long id, Programa datosActualizados) {

        Programa programa = buscarPorId(id);

        programa.setNombre(datosActualizados.getNombre());
        programa.setResumen(datosActualizados.getResumen());
        programa.setDescripcion(datosActualizados.getDescripcion());
        programa.setCategoria(datosActualizados.getCategoria());
        programa.setImagenUrl(datosActualizados.getImagenUrl());
        programa.setEstadoPublicacion(datosActualizados.getEstadoPublicacion());
        programa.setActivo(datosActualizados.getActivo());

        return programaRepository.save(programa);
    }

    // Eliminar un programa
    public void eliminar(Long id) {
        Programa programa = buscarPorId(id);
        programaRepository.delete(programa);
    }
}
