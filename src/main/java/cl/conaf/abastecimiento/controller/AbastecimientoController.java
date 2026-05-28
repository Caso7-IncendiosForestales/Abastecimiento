package cl.conaf.abastecimiento.controller;

import cl.conaf.abastecimiento.dto.InventarioAguaDTO;
import cl.conaf.abastecimiento.dto.PuntoAbastecimientoDTO;
import cl.conaf.abastecimiento.entity.InventarioAgua;
import cl.conaf.abastecimiento.entity.PuntoAbastecimiento;
import cl.conaf.abastecimiento.service.AbastecimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/abastecimiento")
@RequiredArgsConstructor
public class AbastecimientoController {

    private final AbastecimientoService abastecimientoService;

    // --- Puntos ---
    @PostMapping("/puntos")
    public ResponseEntity<PuntoAbastecimiento> crearPunto(
            @Valid @RequestBody PuntoAbastecimientoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(abastecimientoService.crearPunto(dto));
    }

    @GetMapping("/puntos")
    public ResponseEntity<List<PuntoAbastecimiento>> obtenerTodos() {
        return ResponseEntity.ok(abastecimientoService.obtenerTodos());
    }

    @GetMapping("/puntos/{id}")
    public ResponseEntity<PuntoAbastecimiento> obtenerPorId(@PathVariable Long id) {
        return abastecimientoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/puntos/disponibles")
    public ResponseEntity<List<PuntoAbastecimiento>> obtenerDisponibles() {
        return ResponseEntity.ok(abastecimientoService.obtenerDisponibles());
    }

    // --- Inventario ---
    @PostMapping("/inventario")
    public ResponseEntity<InventarioAgua> registrarInventario(
            @Valid @RequestBody InventarioAguaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(abastecimientoService.registrarInventario(dto));
    }

    @PutMapping("/inventario/{id}/volumen")
    public ResponseEntity<InventarioAgua> actualizarVolumen(
            @PathVariable Long id,
            @RequestParam Double nuevoVolumen) {
        return ResponseEntity.ok(abastecimientoService.actualizarVolumen(id, nuevoVolumen));
    }
}