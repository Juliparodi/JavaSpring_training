package bbva.training2.utils;


import bbva.training2.models.Book;
import bbva.training2.models.User;
import bbva.training2.repository.BookRepository;
import bbva.training2.repository.UserRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitialDB {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostConstruct
    public void init() {
        List<User> users = Arrays
                .asList(new User("julianparodi", "juli", LocalDate.of(1997, 10, 01)),
                        new User("matiasbenitez", "mati", LocalDate.of(1997, 10, 10)),
                        new User("laufernandez", "lau", LocalDate.of(1997, 10, 20)));

        List<Book> books = Arrays
                .asList(new Book("terror", "Shakespere", "imagen", "IT", "NoSe", "No se", "2019",
                                150, "123"),
                        new Book("Comedia", "Newton", "imagen12", "Hercules", "ni idea", "Ni idea",
                                "2015", 160, "4321"),
                        new Book("Ciencia Ficcion", "Juanito", "imagen123", "chau", "lala",
                                "lele",
                                "2010", 15, "12345"));

        userRepository.saveAll(users);
        bookRepository.saveAll(books);

    }

}
