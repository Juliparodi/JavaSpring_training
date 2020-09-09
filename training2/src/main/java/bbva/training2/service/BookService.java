package bbva.training2.service;

import bbva.training2.exceptions.BookAlreadyOwnException;
import bbva.training2.exceptions.BookNotFoundException;
import bbva.training2.external.services.OpenLibraryService;
import bbva.training2.models.Book;
import bbva.training2.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book findByTitle(String title) {
        if (bookRepository.findByTitle(title) == (null)) {
            log.error("Book with title: '{}' is not present in our catalog", title);
            throw new BookNotFoundException(
                    "Book with given title is not present in our catalog");
        }
        return bookRepository.findByTitle(title);
    }

    public Book findByIsbn(String isbn, OpenLibraryService openLibraryService) {
        if (bookRepository.findByIsbn(isbn) == (null)) {
            return openLibraryService.bookInfo(isbn);
        }
        return bookRepository.findByIsbn(isbn);
    }

    public Book insertOrUpdate(Book book) {
        List<Book> books = bookRepository.findAll();
        if (books.stream().anyMatch(x -> book.equals(x))) {
            log.error("Book  '{}' is already in our DB", book);
            throw new BookAlreadyOwnException("Book is already in our DB");
        }
        return bookRepository.save(book);
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(
                bookRepository.findByIdBook((id)).orElseThrow(
                        () -> new BookNotFoundException(
                                "Book with given ID is not present in catalog")));
    }

    public List<Book> getByFilterQuery(String genre, String publisher, String year) {
        List<Book> books = bookRepository.getByFilterQuery(genre, publisher, year);
        if (books.isEmpty()) {
            throw new BookNotFoundException("Book Not found");
        }
        return books;
    }
}
