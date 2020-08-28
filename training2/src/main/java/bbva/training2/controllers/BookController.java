package bbva.training2.controllers;

import bbva.training2.exceptions.BookNotFoundException;
import bbva.training2.models.Book;
import bbva.training2.repository.BookRepository;
import bbva.training2.service.BookService;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/v1/books")
public class BookController {

    //CHEQUEAR STATUS CODE
    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<Book> add(@RequestBody Book book) {
        return new ResponseEntity(bookService.insertOrUpdate(book), HttpStatus.CREATED);
    }

    @PostMapping("/addall")
    public ResponseEntity<List<Book>> addAll(@RequestBody List<Book> books){
        return new ResponseEntity(bookService.addAll(books), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Optional<Book>> updateBookById(@RequestBody Book book,
        @PathVariable Long id) {
        if (book.getId() != id) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "El id del objeto y parametro difieren");
        } else if (!bookService.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el ID solicitado");
        }
        return new ResponseEntity(
            Optional.of(bookService.insertOrUpdate(book)),
            HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Book> updateBookByTitle(@RequestParam("Title") String Title, @RequestBody Book book) {
        if (!book.getTitle().equals(Title)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Los generos del objeto y parametro difieren");
        } else if (bookService.findByTitle(Title)==(null)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with given Title not found");
        }
        return new ResponseEntity(bookService.insertOrUpdate(book), HttpStatus.OK);
    }

   @DeleteMapping("/delete/{title}")
   public ResponseEntity<Integer> deleteByTitle(@PathVariable String title){
       if(bookService.findByTitle(title)==(null)){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with given title not found");
       }
       return new ResponseEntity(bookService.deleteByTitle(title), HttpStatus.OK);
   }

    @GetMapping("/all")
    public ResponseEntity<Book> findAll() {
        return new ResponseEntity(bookService.findAll(), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Book> findByTitle(@RequestParam(value = "title") String title) {
            return new ResponseEntity(Optional.of(bookService.findByTitle(title))
                , HttpStatus.OK);
        }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Book>> findById(@PathVariable long id) {
        return new ResponseEntity(
            Optional.of(bookService.findById(id).get()),
            HttpStatus.OK);
    }

   @GetMapping("/find/{isbn}")
    public ResponseEntity<Book> findByIsbn(@PathVariable String isbn){
        return new ResponseEntity(bookService.findByIsbn(isbn), HttpStatus.OK);
   }

    @GetMapping("/query")
    public ResponseEntity<Book> getBooksCustomerQuery(@RequestParam ("isbn") String isbn){
        return new ResponseEntity(bookService.getBooksCustomQuery(isbn), HttpStatus.OK);
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting.html";
    }
}

