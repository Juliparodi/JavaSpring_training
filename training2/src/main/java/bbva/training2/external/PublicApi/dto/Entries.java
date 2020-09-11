package bbva.training2.external.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entries {

    private String Description;

    private String Category;

    private String HTTPS;

    private Boolean Auth;

    private String API;

    private String Cors;

    private String Link;

    @Override
    public String toString() {
        return "ClassPojo [Description = " + Description + ", Category = " + Category + ", HTTPS = "
                + HTTPS + ", Auth = " + Auth + ", API = " + API + ", Cors = " + Cors + ", Link = "
                + Link + "]";
    }

}
