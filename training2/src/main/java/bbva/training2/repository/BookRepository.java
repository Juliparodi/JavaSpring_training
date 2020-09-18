package bbva.training2.repository;

import bbva.training2.models.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIdBook(long id);

    Optional<Book> findByTitle(String title);

    Book findByIsbn(String isbn);

    Integer deleteByTitle(String title);

    List<Book> deleteByIsbn(String isbn);

    @Query(value = "SELECT u FROM Book u WHERE u.isbn = ?1")
    Book getBooksCustomQuery(String isbn);

    @Query(value = "SELECT b FROM Book b "
            + "WHERE "
            + "(:publisher is null OR b.publisher = :publisher) AND "
            + "(:genre is null OR b.genre = :genre) AND "
            + "(:year is null OR b.year = :year)")
    List<Book> getByFilterQuery(String genre, String publisher, String year);
}
