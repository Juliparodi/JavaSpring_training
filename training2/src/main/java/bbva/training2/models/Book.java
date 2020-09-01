package bbva.training2.models;

import bbva.training2.utils.ErrorConstants;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBook;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String subtitle;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String year;

    @Column(nullable = false)
    private Integer pages;

    @NotNull
    @Column(nullable = false, unique = true)
    private String isbn;

    @ManyToMany(mappedBy = "books")
    private List<User> users = new ArrayList<>();

    public Book(String genre, String author, String image, String title,
        String subtitle, String publisher, String year, Integer pages, String isbn) {
        this.genre = genre;
        this.author = author;
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
        this.publisher = publisher;
        this.year = year;
        this.pages = pages;
        this.isbn = isbn;
    }

    public Book(String title, String isbn) {
        this.title = title;
        this.isbn = isbn;
    }

    public void setGenre(String genre) {
        Preconditions.checkNotNull(genre, String.format(ErrorConstants.NOT_NULL, genre));
        Preconditions
            .checkArgument(!genre.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, genre));
        this.genre = genre;
    }

    public void setAuthor(String author) {
        Preconditions.checkNotNull(author, String.format(ErrorConstants.NOT_NULL, author));
        Preconditions
            .checkArgument(!author.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, author));
        this.author = author;
    }

    public void setImage(String image) {
        Preconditions.checkNotNull(image, String.format(ErrorConstants.NOT_NULL, image));
        Preconditions
            .checkArgument(!image.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, image));
        this.image = image;
    }

    public void setTitle(String title) {
        Preconditions.checkNotNull(title, String.format(ErrorConstants.NOT_NULL, title));
        Preconditions
            .checkArgument(!title.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, title));
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        Preconditions.checkNotNull(subtitle, String.format(ErrorConstants.NOT_NULL, subtitle));
        Preconditions
            .checkArgument(!subtitle.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, subtitle));
        this.subtitle = subtitle;
    }

    public void setPublisher(String publisher) {
        Preconditions.checkNotNull(publisher, String.format(ErrorConstants.NOT_NULL, publisher));
        Preconditions
            .checkArgument(!publisher.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, publisher));
        this.publisher = publisher;
    }

    public void setYear(String year) {
        Preconditions.checkNotNull(year, String.format(ErrorConstants.NOT_NULL, year));
        Preconditions
            .checkArgument(!year.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, year));
        this.year = year;
    }

    public void setPages(Integer pages) {
        Preconditions.checkNotNull(pages, String.format(ErrorConstants.NOT_NULL, pages));
        Preconditions
            .checkArgument(pages > 0, String.format(ErrorConstants.NOT_GRADER_THAN, "0"));
        this.pages = pages;
    }

    public void setIsbn(String isbn) {
        Preconditions.checkNotNull(isbn, String.format(ErrorConstants.NOT_NULL, isbn));
        Preconditions
            .checkArgument(!isbn.isEmpty(), String.format(ErrorConstants.NOT_EMPTY, isbn));
        Preconditions
            .checkArgument(StringUtils.isNumeric(isbn), String.format(ErrorConstants.NOT_NUMERIC, "isbn"));
        this.isbn = isbn;
    }

    public List<User> getUser() {
        return (List<User>) Collections.unmodifiableList(users);
    }

    @Override
    public boolean equals(Object o) {
        Book book = (Book) o;
        return idBook == book.idBook &&
            Objects.equals(title, book.title) &&
            Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBook, title, isbn);
    }
}
