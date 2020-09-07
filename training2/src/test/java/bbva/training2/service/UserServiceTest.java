package bbva.training2.service;

import static org.junit.jupiter.api.Assertions.*;

import bbva.training2.models.User;
import bbva.training2.repository.UserRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;


    @MockBean
    User userTest;

    @BeforeEach
    void setUp() {
        userTest = new User("Julian", "juliparodi", LocalDate.of(1997, 10, 1));

        Mockito.when(userRepository.findByUserName(userTest.getUserName())).thenReturn(Optional.of(userTest));
    }

    @Test
    void whenValidName_ThenUserShouldBeFound() {
        User userFound = userService.findByUserName(userTest.getUserName()).get();
        assertEquals(userService.findByUserName(userFound.getUserName()).get().getUserName(), userTest.getUserName());
    }
}