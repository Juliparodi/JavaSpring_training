package bbva.training2.external.PublicApi.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicApiDTO implements Serializable {

    private Integer count;
    private List<Entries> entries;
}
