package cl.conaf.abastecimiento.repository;

import cl.conaf.abastecimiento.entity.InventarioAgua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventarioAguaRepository 
    extends JpaRepository<InventarioAgua, Long> {

    List<InventarioAgua> findByPuntoAbastecimientoId(Long puntoId);

    @Query("SELECT i FROM InventarioAgua i WHERE i.volumenDisponibleLitros > 0")
    List<InventarioAgua> findPuntosConAgua();
}