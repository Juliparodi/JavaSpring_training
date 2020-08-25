package bbva.training2.controllers;

import bbva.training2.models.Book;
import bbva.training2.repository.BookRepository;
import bbva.training2.service.BookService;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;
    Logger logger =  Logger.getLogger("");

    @GetMapping("books")
    public List<Book> findAll(){
        return bookService.findAll();
    }

    @GetMapping
    public String greeting(
        @RequestParam(name = "name", required = false, defaultValue = "World") String name,
        Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("addbook")
    public void getBooks(){
       Book book = new Book();
       book.setAuthor("shakespere");
       book.setTitle("Hercules");
       book.setIsbn("123");
       bookRepository.save(book);
    }

/*
    @GetMapping("query")
    public List<Book> getBooksCustomerQuery(){
        return bookService.getBooksCustomQuery();
    }
*/
    @PostMapping("addbook/{title}")
    public void addBook(@PathVariable("title") String author){
        bookService.saveBook(author);
        logger.info("El book se actualizo correctamente");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookService.save(book);
    }

    @RequestMapping(value = "addbooks", method = RequestMethod.POST)
    public void addBooks(){
        bookService.saveAll();
    }






}
