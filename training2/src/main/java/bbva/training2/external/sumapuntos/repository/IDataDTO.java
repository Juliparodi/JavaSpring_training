package bbva.training2.external.sumapuntos.repository;

import bbva.training2.external.sumapuntos.dto.DataDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDataDTO extends JpaRepository<DataDTO, Long> {

}
