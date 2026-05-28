package cl.conaf.abastecimiento.entity;

import cl.conaf.abastecimiento.enums.EstadoPunto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "punto_abastecimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuntoAbastecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    private Double longitud;

    @Column(name = "direccion_referencia")
    private String direccionReferencia;

    @Column(name = "region", nullable = false)
    private String region;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPunto estado;

    @OneToMany(mappedBy = "puntoAbastecimiento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InventarioAgua> inventarios;
}