package bbva.training2.repository;

import bbva.training2.models.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {


    Book findByTitle(String title);

    Book findByIsbn(String isbn);

    Integer deleteByTitle(String title);

    List<Book> deleteByIsbn(String isbn);

    @Query(value = "SELECT u FROM Book u WHERE u.isbn = ?1")
    Book getBooksCustomQuery(String isbn);

    Book findByGenre(String genre);
}
