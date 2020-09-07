package bbva.training2.external.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO implements Serializable {

    @NotNull
    private String isbn;
    @NotNull
    private String title;
    private String subtitle;
    private String publishers;
    private String publishDate;
    private Integer numberPages;
    @NotNull
    private String authors;

    public BookDTO (String isbn, String title, String subtitle, String publishers, String publishDate, Integer numberPages, String authors){
        this.setIsbn(isbn);
        this.setTitle(title);
        this.setSubtitle(subtitle);
        this.setPublishers(publishers);
        this.setPublishDate(publishDate);
        this.setNumberPages(numberPages);
        this.setAuthors(authors);
    }

}
