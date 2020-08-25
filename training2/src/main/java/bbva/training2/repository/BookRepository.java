package bbva.training2.repository;

import bbva.training2.models.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);

    String findByAuthor(String author);

    Book save(Book Book);

    List<Book> findAll();


    /*
    @Query("select u from books u")
    public List<Book> getBooksCustomQuery();
*/
}
