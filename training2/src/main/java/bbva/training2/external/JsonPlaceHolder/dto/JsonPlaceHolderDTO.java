package bbva.training2.external.JsonPlaceHolder.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonPlaceHolderDTO implements Serializable {

    private Integer postId;
    private Integer id;
    private String name;
    private String email;
    private String body;
}
