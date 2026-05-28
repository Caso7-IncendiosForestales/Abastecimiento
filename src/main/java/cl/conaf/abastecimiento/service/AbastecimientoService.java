package cl.conaf.abastecimiento.service;

import cl.conaf.abastecimiento.dto.InventarioAguaDTO;
import cl.conaf.abastecimiento.dto.PuntoAbastecimientoDTO;
import cl.conaf.abastecimiento.entity.InventarioAgua;
import cl.conaf.abastecimiento.entity.PuntoAbastecimiento;
import cl.conaf.abastecimiento.enums.EstadoPunto;
import cl.conaf.abastecimiento.repository.InventarioAguaRepository;
import cl.conaf.abastecimiento.repository.PuntoAbastecimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbastecimientoService {

    private final PuntoAbastecimientoRepository puntoRepository;
    private final InventarioAguaRepository inventarioRepository;

    // --- Puntos de Abastecimiento ---

    public PuntoAbastecimiento crearPunto(PuntoAbastecimientoDTO dto) {
        PuntoAbastecimiento punto = new PuntoAbastecimiento();
        punto.setNombre(dto.getNombre());
        punto.setLatitud(dto.getLatitud());
        punto.setLongitud(dto.getLongitud());
        punto.setDireccionReferencia(dto.getDireccionReferencia());
        punto.setRegion(dto.getRegion());
        punto.setEstado(dto.getEstado());
        return puntoRepository.save(punto);
    }

    public List<PuntoAbastecimiento> obtenerTodos() {
        return puntoRepository.findAll();
    }

    public Optional<PuntoAbastecimiento> obtenerPorId(Long id) {
        return puntoRepository.findById(id);
    }

    public List<PuntoAbastecimiento> obtenerDisponibles() {
        return puntoRepository.findByEstado(EstadoPunto.DISPONIBLE);
    }

    // --- Inventario de Agua ---

    public InventarioAgua registrarInventario(InventarioAguaDTO dto) {
        PuntoAbastecimiento punto = puntoRepository.findById(dto.getPuntoAbastecimientoId())
                .orElseThrow(() -> new RuntimeException("Punto de abastecimiento no encontrado"));

        InventarioAgua inventario = new InventarioAgua();
        inventario.setPuntoAbastecimiento(punto);
        inventario.setVolumenDisponibleLitros(dto.getVolumenDisponibleLitros());
        inventario.setCapacidadMaximaLitros(dto.getCapacidadMaximaLitros());
        inventario.setUltimaActualizacion(LocalDateTime.now());
        inventario.setCamionAljibeId(dto.getCamionAljibeId());

        // Log estructurado cuando un camión inicia proceso de carga
        if (dto.getCamionAljibeId() != null) {
    String mensajeLog = String.format(
        "[%s] Camión aljibe ID=%d inició proceso de carga en punto '%s'. Volumen disponible: %.0f L",
        LocalDateTime.now(), dto.getCamionAljibeId(), punto.getNombre(), dto.getVolumenDisponibleLitros()
    );
    inventario.setLogCarga(mensajeLog);
    log.info(mensajeLog);
}

        return inventarioRepository.save(inventario);
    }

    public InventarioAgua actualizarVolumen(Long inventarioId, Double nuevoVolumen) {
        InventarioAgua inventario = inventarioRepository.findById(inventarioId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        inventario.setVolumenDisponibleLitros(nuevoVolumen);
        inventario.setUltimaActualizacion(LocalDateTime.now());

        if (nuevoVolumen == 0) {
            inventario.getPuntoAbastecimiento().setEstado(EstadoPunto.SIN_AGUA);
            puntoRepository.save(inventario.getPuntoAbastecimiento());
        }

        return inventarioRepository.save(inventario);
    }
}