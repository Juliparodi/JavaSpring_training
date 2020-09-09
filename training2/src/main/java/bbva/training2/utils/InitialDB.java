
package bbva.training2.utils;


import bbva.training2.models.Book;
import bbva.training2.models.User;
import bbva.training2.repository.BookRepository;
import bbva.training2.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitialDB {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init(){
        List<User> users = Arrays.asList(new User("julianparodi", "juli123", LocalDate.of(1997, 10, 01)),
            new User("matiasbenitez", "mati123", LocalDate.of(1997, 10, 10)),
            new User("laufernandez", "lau123", LocalDate.of(1997, 10, 20)));
        userRepository.saveAll(users);

    }

}
