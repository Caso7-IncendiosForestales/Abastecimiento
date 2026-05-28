package cl.conaf.abastecimiento.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventarioAguaDTO {

    @NotNull(message = "El punto de abastecimiento es obligatorio")
    private Long puntoAbastecimientoId;

    @NotNull(message = "El volumen disponible es obligatorio")
    @Min(value = 0, message = "El volumen no puede ser negativo")
    private Double volumenDisponibleLitros;

    @NotNull(message = "La capacidad máxima es obligatoria")
    @Min(value = 0, message = "La capacidad no puede ser negativa")
    private Double capacidadMaximaLitros;

    private Long camionAljibeId;
}