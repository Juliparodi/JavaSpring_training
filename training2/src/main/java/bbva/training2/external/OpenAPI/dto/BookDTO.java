package bbva.training2.external.OpenAPI.dto;

import bbva.training2.external.OpenAPI.dto.Authors;
import bbva.training2.external.OpenAPI.dto.Publishers;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @NotNull
    private String isbn;
    @NotNull
    private String title;
    @JsonProperty("publishers")
    private List<Publishers> publishers;
    private String subtitle;
    @JsonProperty("publish_date")
    private String publishDate;
    @JsonProperty("number_of_pages")
    private Integer numberPages;
    @NotNull
    @JsonProperty("authors")
    private List<Authors> authors;



}
