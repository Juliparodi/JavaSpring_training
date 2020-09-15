package bbva.training2.controllers;

import bbva.training2.external.OpenAPI.services.OpenLibraryService;
import bbva.training2.models.Book;
import bbva.training2.models.User;
import bbva.training2.repository.BookRepository;
import bbva.training2.service.BookService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/v1/books/")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    @PostMapping
    @ApiOperation(value = "add a User to our repository", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "succesfully book added"),
            @ApiResponse(code = 404, message = "Book not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 401, message = "Access unauthorized."),
            @ApiResponse(code = 403, message = "Access unauthorized."),
            @ApiResponse(code = 409, message = "Book already on the DB."),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Book> add(@Valid @RequestBody Book book) {
        return new ResponseEntity<>(bookService.insertOrUpdate(book), HttpStatus.CREATED);
    }

    @PostMapping("all")
    @ApiOperation(value = "given a Book list, add all to our repository", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "succesfully books added"),
            @ApiResponse(code = 404, message = "User/users not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 401, message = "Access unauthorized."),
            @ApiResponse(code = 403, message = "Access unauthorized."),
            @ApiResponse(code = 409, message = "Book/books already on the DB."),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<List<Book>> addAll(@RequestBody List<Book> books) {
        return new ResponseEntity<>(bookRepository.saveAll(books), HttpStatus.CREATED);
    }

    @PutMapping("/{idBook}")
    @ApiOperation(value = "given a Book object and a given id, update Book repository", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "succesfully books updated"),
            @ApiResponse(code = 404, message = "Book/books not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 401, message = "Access unauthorized."),
            @ApiResponse(code = 403, message = "Access unauthorized."),
            @ApiResponse(code = 409, message = "Book/books already on the DB."),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Optional<Book>> updateBookById(@RequestBody Book book,
            @PathVariable long idBook) {
        log.info("----- BOOK OBJECT: '{}', ID '{}'", book, idBook);
        if (book.getIdBook() != idBook) {
            log.error("OBJECT and ID parameter are not the same");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "ID object and ID parameter are not the same");
        } else if (!bookService.findById(idBook).isPresent()) {
            log.error("ID not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID not found");
        }
        return new ResponseEntity<>(
                Optional.of(bookService.updateById(book, idBook)),
                HttpStatus.OK);
    }

    //https://stackoverflow.com/questions/32269192/spring-no-entitymanager-with-actual-transaction-available-for-current-thread
    @Transactional //i used this annotation since i was giving NoEntityManager Exception
    @DeleteMapping("{title}")
    @ApiOperation(value = "given a Book title. delete that book object from our DB", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "succesfully book deleted"),
            @ApiResponse(code = 404, message = "Book/books not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 401, message = "Access unauthorized."),
            @ApiResponse(code = 403, message = "Access unauthorized."),
            @ApiResponse(code = 409, message = "Book/books already on the DB."),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Integer> deleteByTitle(@PathVariable String title) {
        if (bookService.findByTitle(title) == (null)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Book with given title not found");
        }
        return new ResponseEntity<>(bookRepository.deleteByTitle(title), HttpStatus.OK);
    }

    @GetMapping("all")
    @ApiOperation(value = "retrieve all list of books", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "succesfully list of books showed on the screen"),
            @ApiResponse(code = 404, message = "Book/books not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 401, message = "Access unauthorized."),
            @ApiResponse(code = 403, message = "Access unauthorized."),
            @ApiResponse(code = 409, message = "Book/books already on the DB."),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<List<Book>> findAll() {
        log.info("returning all books");
        return new ResponseEntity<>(bookRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping()
    @ApiOperation(value = "given a title param, retrieve Book object that matches given title", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "succesfully book showed"),
            @ApiResponse(code = 404, message = "Book/books not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 401, message = "Access unauthorized."),
            @ApiResponse(code = 403, message = "Access unauthorized."),
            @ApiResponse(code = 409, message = "Book/books already on the DB."),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Book> findByTitle(@RequestParam(value = "title") String title) {
        log.info("---- title: '{}'", title);
        return new ResponseEntity(Optional.of(bookService.findByTitle(title))
                , HttpStatus.OK);
    }

    @GetMapping("{isbn}") //0765304368
    @ApiOperation(value = "given a isbn of a book, retrieve from our local DB or from external service", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "succesfully book showed"),
            @ApiResponse(code = 404, message = "Book/books not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 401, message = "Access unauthorized."),
            @ApiResponse(code = 403, message = "Access unauthorized."),
            @ApiResponse(code = 409, message = "Book/books already on the DB."),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Book> findByIsbn(@PathVariable String isbn) {
        log.info("-------- isbn: '{}'", isbn);
        return new ResponseEntity<>(bookService.findByIsbn(isbn, openLibraryService),
                HttpStatus.OK);
    }

    @GetMapping("/query")
    @ApiOperation(value = "given a isbn, execute SQL query", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "succesfully book showed"),
            @ApiResponse(code = 404, message = "Book/books not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 401, message = "Access unauthorized."),
            @ApiResponse(code = 403, message = "Access unauthorized."),
            @ApiResponse(code = 409, message = "Book/books already on the DB."),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Book> getBooksCustomerQuery(@RequestParam("isbn") String isbn) {
        log.info("-------- isbn: '{}'", isbn);
        return new ResponseEntity<>(bookRepository.getBooksCustomQuery(isbn), HttpStatus.OK);
    }

    @GetMapping("customQuery")
    @ApiOperation(value = "given a some parameters, execute customer SQL query", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "succesfully list of books showed"),
            @ApiResponse(code = 404, message = "Book/books not found"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 401, message = "Access unauthorized."),
            @ApiResponse(code = 403, message = "Access unauthorized."),
            @ApiResponse(code = 409, message = "Book/books already on the DB."),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<List<Book>> findByQuery(
            @RequestParam(name = "genre", required = false) String genre,
            @RequestParam(name = "publisher", required = false) String publisher,
            @RequestParam(name = "year", required = false) String year) {
        log.info("---- genre: '{}', publisher: '{}', year: '{}'", genre, publisher, year);
        return new ResponseEntity<>(bookService.getByFilterQuery(genre, publisher, year),
                HttpStatus.OK);
    }

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model) {
        model.addAttribute("name", name);
        return "greeting.html";
    }

    /* Methods i created but did not used:
    @PutMapping()
    public ResponseEntity<Book> updateBookByTitle(@RequestParam("Title") String Title, @RequestBody Book book) {
        if (!book.getTitle().equals(Title)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Object genre and parameter genre are not the same");
        } else if (bookService.findByTitle(Title)==(null)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with given Title not found");
        }
        return new ResponseEntity<>(bookService.insertOrUpdate(book), HttpStatus.OK);
    }
     */

}

