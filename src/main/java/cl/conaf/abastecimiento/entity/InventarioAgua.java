package cl.conaf.abastecimiento.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventario_agua")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioAgua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "punto_abastecimiento_id", nullable = false)
    private PuntoAbastecimiento puntoAbastecimiento;

    @Column(name = "volumen_disponible_litros", nullable = false)
    private Double volumenDisponibleLitros;

    @Column(name = "capacidad_maxima_litros", nullable = false)
    private Double capacidadMaximaLitros;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @Column(name = "camion_aljibe_id")
    private Long camionAljibeId;

    @Column(name = "log_carga", columnDefinition = "TEXT")
    private String logCarga;
}