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

    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public Book insertOrUpdate(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> saveAll(List<Book> books) {
        return bookRepository.saveAll(books);
    }

       /*
    public List<Book> getBooksCustomQuery(){
        return bookRepository.getBooksCustomQuery();
    }
*/
}
