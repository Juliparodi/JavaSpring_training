package bbva.training2.external.sumapuntos.repository;

import bbva.training2.external.sumapuntos.dto.PuntosDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPuntosRepository extends JpaRepository<PuntosDTO, Long> {

}
