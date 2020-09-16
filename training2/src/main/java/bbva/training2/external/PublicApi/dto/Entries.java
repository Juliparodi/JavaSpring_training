package bbva.training2.external.PublicApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entries {

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Category")
    private String category;

    @JsonProperty("HTTPS")
    private boolean https;

    @JsonProperty("Auth")
    private String auth;

    @JsonProperty("API")
    private String api;

    @JsonProperty("Cors")
    private String cors;

    @JsonProperty("Link")
    private String link;

    @Override
    public String toString() {
        return "Entries{" +
                "description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", https='" + https + '\'' +
                ", auth=" + auth +
                ", api='" + api + '\'' +
                ", cors='" + cors + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}

