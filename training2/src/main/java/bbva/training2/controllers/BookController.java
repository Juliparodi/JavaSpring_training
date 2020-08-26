package bbva.training2.controllers;

import bbva.training2.models.Book;
import bbva.training2.repository.BookRepository;
import bbva.training2.service.BookService;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/book")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/all")
    public ResponseEntity<Book> findAll(){
        return new ResponseEntity(bookService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{title}")
    public ResponseEntity<Book> getTitle(@PathVariable String title){
        return new ResponseEntity(bookService.findByTitle(title), HttpStatus.OK);
    }

    @GetMapping("/greeting")
    public String greeting(
        @RequestParam(name = "name", required = false, defaultValue = "World") String name,
        Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @PostMapping("/add")
    public ResponseEntity<Book> add(@RequestBody Book book) {
        return new ResponseEntity(bookService.insertOrUpdate(book), HttpStatus.OK);
    }

    @GetMapping("/addbook")
    public ResponseEntity<Book> addBook(){
       Book book = new Book("julian", "parodi");
       return new ResponseEntity(bookService.insertOrUpdate(book), HttpStatus.OK);
    }

    @GetMapping("/addbooks")
    public BodyBuilder addBooks(){
        Book book = new Book("juli", "123");
        Book book1 = new Book("parodi", "423");
        List<Book> books = Arrays.asList(book, book1);
        bookService.saveAll(books);
        return ResponseEntity.accepted();
    }



/*
    @GetMapping("query")
    public List<Book> getBooksCustomerQuery(){
        return bookService.getBooksCustomQuery();
    }
*/




}
