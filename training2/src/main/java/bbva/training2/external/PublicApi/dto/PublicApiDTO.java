package bbva.training2.external.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicApiDTO implements Serializable {

    private Integer count;
    private Entries[] entries;
}
