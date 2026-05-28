package cl.conaf.abastecimiento.dto;

import cl.conaf.abastecimiento.enums.EstadoPunto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PuntoAbastecimientoDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;

    private String direccionReferencia;

    @NotBlank(message = "La región es obligatoria")
    private String region;

    @NotNull(message = "El estado es obligatorio")
    private EstadoPunto estado;
}