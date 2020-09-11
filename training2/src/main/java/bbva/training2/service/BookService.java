package bbva.training2.service;

import bbva.training2.exceptions.BookAlreadyOwnException;
import bbva.training2.exceptions.BookNotFoundException;
import bbva.training2.external.OpenAPI.services.OpenLibraryService;
import bbva.training2.models.Book;
import bbva.training2.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    //implementing cache
    @Cacheable(cacheNames = "isbn")
    public Book findByIsbn(String isbn, OpenLibraryService openLibraryService) throws Exception {
        if (bookRepository.findByIsbn(isbn) == (null)) {
            return openLibraryService.bookInfo(isbn);
        }
        return bookRepository.findByIsbn(isbn);
    }


    @CacheEvict(cacheNames = "isbn", allEntries = true)
    public void flushCache() {
    }

    public Book insertOrUpdate(Book book) {
        List<Book> books = bookRepository.findAll();
        if (books.stream().anyMatch(x -> book.equals(x))) {
            log.error("Book  '{}' is already in our DB", book);
            throw new BookAlreadyOwnException("Book is already in our DB");
        }
        return bookRepository.save(book);
    }

    @CachePut(cacheNames = "isbn", key = "#book.id")
    public Book updateById(Book book, long id) {
        List<Book> books = bookRepository.findAll();
        if (!bookRepository.findByIdBook(id).isPresent()) {
            log.error("-------- '{}' NOT FOUND", id);
            throw new BookNotFoundException("Cannot find Book with this id");
        }
        Book bookFounded = bookRepository.findByIdBook(id).get();
        bookFounded.setPages(book.getPages());
        bookFounded.setYear(book.getYear());
        bookFounded.setPublisher(book.getPublisher());
        bookFounded.setSubtitle(book.getSubtitle());
        bookFounded.setImage(book.getImage());
        bookFounded.setGenre(book.getGenre());
        return bookFounded;
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
