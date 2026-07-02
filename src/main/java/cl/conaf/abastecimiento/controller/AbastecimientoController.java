package cl.conaf.abastecimiento.controller;

import cl.conaf.abastecimiento.dto.InventarioAguaDTO;
import cl.conaf.abastecimiento.dto.PuntoAbastecimientoDTO;
import cl.conaf.abastecimiento.entity.InventarioAgua;
import cl.conaf.abastecimiento.entity.PuntoAbastecimiento;
import cl.conaf.abastecimiento.service.AbastecimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/abastecimiento")
@RequiredArgsConstructor
@Tag(name = "Puntos de Abastecimiento",
     description = "Gestión de geolocalización y niveles de agua para camiones aljibe - CONAF")
public class AbastecimientoController {

    private final AbastecimientoService abastecimientoService;

    @Operation(
        summary = "Crear punto de abastecimiento",
        description = "Registra un nuevo punto de agua con geolocalización " +
                      "para redirigir camiones aljibe"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Punto creado exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(
                examples = @ExampleObject(
                    value = """
                        {
                          "status": 400,
                          "error": "Bad Request",
                          "message": "El nombre es obligatorio"
                        }
                        """
                )
            )
        )
    })
    @PostMapping("/puntos")
    public ResponseEntity<PuntoAbastecimiento> crearPunto(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del punto de abastecimiento",
            required = true,
            content = @Content(
                examples = @ExampleObject(
                    name = "Punto Pencahue",
                    value = """
                        {
                          "nombre": "Punto Agua Pencahue",
                          "latitud": -35.3833,
                          "longitud": -71.8167,
                          "direccionReferencia": "Camino a Pencahue km 5",
                          "region": "Maule",
                          "estado": "DISPONIBLE"
                        }
                        """
                )
            )
        )
        @Valid @RequestBody PuntoAbastecimientoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(abastecimientoService.crearPunto(dto));
    }

    @Operation(
        summary = "Obtener todos los puntos",
        description = "Lista todos los puntos de abastecimiento registrados"
    )
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping("/puntos")
    public ResponseEntity<List<PuntoAbastecimiento>> obtenerTodos() {
        return ResponseEntity.ok(abastecimientoService.obtenerTodos());
    }

    @Operation(
        summary = "Obtener punto por ID",
        description = "Busca un punto específico por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Punto encontrado"),
        @ApiResponse(responseCode = "404", description = "Punto no encontrado")
    })
    @GetMapping("/puntos/{id}")
    public ResponseEntity<PuntoAbastecimiento> obtenerPorId(@PathVariable Long id) {
        return abastecimientoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Obtener puntos disponibles",
        description = "Filtra solo los puntos con estado DISPONIBLE para asignación de camiones"
    )
    @ApiResponse(responseCode = "200", description = "Lista de puntos disponibles")
    @GetMapping("/puntos/disponibles")
    public ResponseEntity<List<PuntoAbastecimiento>> obtenerDisponibles() {
        return ResponseEntity.ok(abastecimientoService.obtenerDisponibles());
    }

    @Operation(
        summary = "Registrar inventario de agua",
        description = "Registra el volumen disponible. Si incluye camionAljibeId " +
                      "genera log estructurado automáticamente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Inventario registrado exitosamente"),
        @ApiResponse(
            responseCode = "400",
            description = "Volumen negativo rechazado por @Min(0)",
            content = @Content(
                examples = @ExampleObject(
                    value = """
                        {
                          "status": 400,
                          "error": "Bad Request",
                          "message": "El volumen no puede ser negativo"
                        }
                        """
                )
            )
        )
    })
    @PostMapping("/inventario")
    public ResponseEntity<InventarioAgua> registrarInventario(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del inventario de agua",
            required = true,
            content = @Content(
                examples = @ExampleObject(
                    name = "Inventario con camión aljibe",
                    value = """
                        {
                          "puntoAbastecimientoId": 1,
                          "volumenDisponibleLitros": 4000,
                          "capacidadMaximaLitros": 5000,
                          "camionAljibeId": 7
                        }
                        """
                )
            )
        )
        @Valid @RequestBody InventarioAguaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(abastecimientoService.registrarInventario(dto));
    }

    @Operation(
        summary = "Actualizar volumen disponible",
        description = "Actualiza el volumen de agua. Si llega a 0 el punto " +
                      "cambia automáticamente a SIN_AGUA"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Volumen actualizado"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @PutMapping("/inventario/{id}/volumen")
    public ResponseEntity<InventarioAgua> actualizarVolumen(
            @PathVariable Long id,
            @RequestParam Double nuevoVolumen) {
        return ResponseEntity.ok(abastecimientoService.actualizarVolumen(id, nuevoVolumen));
    }
}