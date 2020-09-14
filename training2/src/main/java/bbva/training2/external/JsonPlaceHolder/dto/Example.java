package bbva.training2.external.JsonPlaceHolder.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Example implements Serializable {

    private Integer userId;
    private Integer id;
    private String title;
    private String body;

}
