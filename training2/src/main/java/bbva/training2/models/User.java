package bbva.training2.models;

import bbva.training2.exceptions.BookAlreadyOwnException;
import bbva.training2.exceptions.BookNotFoundException;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@ApiModel(description = "Entity to keep user objects")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "idUser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @NotNull
    private String userName;

    @NotNull
    private String name;

    @ApiModelProperty(notes = "date of their birthday")
    @NotNull
    private LocalDate birthDate;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable
    private List<Book> books = new ArrayList<>();

    public User(String name, String userName, LocalDate date) {
        this.setName(name);
        this.setUserName(userName);
        this.setBirthDate(date);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    public boolean addBook(Book book) {
        if (this.getBooks().contains(book)) {
            throw new BookAlreadyOwnException("User has already added this book");
        }
        return books.add(book);
    }

    public boolean removeBook(Book book) {
        if (!this.getBooks().contains(book)) {
            throw new BookNotFoundException("Book is not in our user list");
        }
        return books.remove(book);
    }

    @Override
    public boolean equals(Object o) {
        User user = (User) o;
        return Objects.equals(userName, user.userName) &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, name);
    }
}
