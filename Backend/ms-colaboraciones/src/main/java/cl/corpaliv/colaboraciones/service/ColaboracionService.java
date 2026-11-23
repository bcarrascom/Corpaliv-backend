package cl.corpaliv.colaboraciones.service;

import cl.corpaliv.colaboraciones.exception.ColaboracionNoEncontradaException;
import cl.corpaliv.colaboraciones.model.Colaboracion;
import cl.corpaliv.colaboraciones.repository.ColaboracionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

// Lógica de negocio de las solicitudes de colaboración.
@Service
public class ColaboracionService {

    private final ColaboracionRepository colaboracionRepository;

    public ColaboracionService(ColaboracionRepository colaboracionRepository) {
        this.colaboracionRepository = colaboracionRepository;
    }

    // Todas, las más nuevas primero (panel del equipo de vinculación).
    public List<Colaboracion> listarTodas() {
        return colaboracionRepository.findAllByOrderByIdDesc();
    }

    // Solo las de un correo ("Mis solicitudes" en la app).
    public List<Colaboracion> listarPorCorreo(String correo) {
        return colaboracionRepository.findByCorreoOrderByIdDesc(correo.trim());
    }

    public Colaboracion buscarPorId(Long id) {
        Optional<Colaboracion> encontrada = colaboracionRepository.findById(id);
        if (encontrada.isEmpty()) {
            throw new ColaboracionNoEncontradaException(id);
        }
        return encontrada.get();
    }

    public Colaboracion crear(Colaboracion colaboracion) {
        // El id lo asigna la BD y toda solicitud nueva parte como "Pendiente".
        colaboracion.setId(null);
        colaboracion.setEstado(Colaboracion.ESTADO_INICIAL);
        return colaboracionRepository.save(colaboracion);
    }

    // Edita los datos de la solicitud. El estado no se toca aquí.
    public Colaboracion actualizar(Long id, Colaboracion datosNuevos) {
        Colaboracion existente = buscarPorId(id);
        existente.setNombreContacto(datosNuevos.getNombreContacto());
        existente.setOrganizacion(datosNuevos.getOrganizacion());
        existente.setCorreo(datosNuevos.getCorreo());
        existente.setTipoColaboracion(datosNuevos.getTipoColaboracion());
        existente.setCantidadPersonas(datosNuevos.getCantidadPersonas());
        existente.setFechaContacto(datosNuevos.getFechaContacto());
        existente.setAceptaContacto(datosNuevos.getAceptaContacto());
        return colaboracionRepository.save(existente);
    }

    // Solo cambia el estado (lo usa el equipo de vinculación).
    public Colaboracion cambiarEstado(Long id, String nuevoEstado) {
        Colaboracion existente = buscarPorId(id);
        existente.setEstado(nuevoEstado);
        return colaboracionRepository.save(existente);
    }

    public void eliminar(Long id) {
        Colaboracion existente = buscarPorId(id);
        colaboracionRepository.delete(existente);
    }
}
