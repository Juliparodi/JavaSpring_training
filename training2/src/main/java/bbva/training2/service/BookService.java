package bbva.training2.service;

import bbva.training2.exceptions.BookNotFoundException;
import bbva.training2.external.services.OpenLibraryService;
import bbva.training2.models.Book;
import bbva.training2.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public Book findByTitle(String title) {
        if (bookRepository.findByTitle(title)==(null)) {
            throw new BookNotFoundException(
                "Book with given title is not present in our catalog");
        }
        return bookRepository.findByTitle(title);
    }

    public Book findByIsbn(String isbn, OpenLibraryService openLibraryService) {
        if (bookRepository.findByIsbn(isbn)==(null)) {
            return openLibraryService.bookInfo(isbn);
        }
        return bookRepository.findByIsbn(isbn);
    }

    public Book insertOrUpdate(Book book) {
        List<Book> books = bookRepository.findAll();
        for (int i = 0; i < bookRepository.findAll().size(); i++) {
            if (book.equals(books.get(i))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Given book is already defined in our catalog");
            }
        }
        return bookRepository.save(book);
    }

    public Optional<Book> findById(Long id) {
        if (!bookRepository.findByIdBook(Math.toIntExact(id)).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Book with given ID is not present in catalog");
        }
        return bookRepository.findByIdBook(Math.toIntExact(id));
    }

    public List<Book> addAll(List<Book> books) {
        return bookRepository.saveAll(books);
    }


    public Integer deleteByTitle(String title){ return bookRepository.deleteByTitle(title); }


    public Book getBooksCustomQuery(String isbn){
        return bookRepository.getBooksCustomQuery(isbn);
    }

    public Book findByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }

    public Book getByFilterQuery(String genre, String publisher, String year) {
        return bookRepository.getByFilterQuery(genre, publisher, year);
    }
}
