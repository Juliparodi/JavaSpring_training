package bbva.training2.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bbva.training2.models.User;
import bbva.training2.utils.VariableConstants;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class UserRepositoryTest {


    @Autowired
    UserRepository userRepository;
    User userTest;
    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        userTest = new User("Julian", "juliparodi", LocalDate.of(1997, 10, 01));
        entityManager.persist(userTest);
        entityManager.flush();
    }

    //GET METHODS
    @Test
    void whenFindByUserName_ThenReturnUser() {
        Optional<User> userNameFound = userRepository.findByUserName(userTest.getUserName());
        assertTrue(userNameFound.isPresent());
        assertEquals(userNameFound.get().getUserName(), userTest.getUserName());
    }

    @Test
    void whenFindByUserNameThatNotExists_ThenReturnException() {
        String userName = VariableConstants.USER_NOT_EXISTS;
        Optional<User> userNotFound = userRepository.findByUserName(userName);
        assertFalse(userNotFound.isPresent());
    }

    //POST METHODS
    @Test
    void whenSaveUserObject_ThenReturnUserObject() {
        User userTest1 = new User("Mati", "mati123", LocalDate.of(1997, 11, 1));
        userRepository.save(userTest1);
        String name = "Mati";
        assertEquals(name, userTest1.getName());
    }

    @Test
    public void whenCreateUserwithOutUserName_thenThrowException() {
        User userTest1 = new User("juli", null, LocalDate.of(1997, 11, 1));
        assertThrows(javax.validation.ConstraintViolationException.class,
                () -> userRepository.save(userTest1));
    }
}
