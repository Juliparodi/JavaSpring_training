package bbva.training2.service;

import bbva.training2.models.Book;
import bbva.training2.repository.BookRepository;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    /*
    public List<Book> getBooksCustomQuery(){
        return bookRepository.getBooksCustomQuery();
    }
*/

    public void saveBook(String title) {
        bookRepository.save(new Book(title, "123"));
    }

    public List<Book> saveAll() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Hercules", "123"));
        books.add(new Book("IT", "124"));
        books.add(new Book("Shakespere", "125"));
        return bookRepository.saveAll(books);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }
}
