package cl.conaf.abastecimiento.repository;

import cl.conaf.abastecimiento.entity.PuntoAbastecimiento;
import cl.conaf.abastecimiento.enums.EstadoPunto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PuntoAbastecimientoRepository 
    extends JpaRepository<PuntoAbastecimiento, Long> {

    List<PuntoAbastecimiento> findByEstado(EstadoPunto estado);
    List<PuntoAbastecimiento> findByRegion(String region);
}