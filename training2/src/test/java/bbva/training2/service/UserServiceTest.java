package bbva.training2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import bbva.training2.exceptions.UserAlreadyOwnException;
import bbva.training2.exceptions.UserNotFoundException;
import bbva.training2.models.User;
import bbva.training2.repository.UserRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    User userTest;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userTest = new User("Julian", "juliparodi", LocalDate.of(1997, 10, 1));
    }

    //GET METHODS
    @Test
    void whenValidName_ThenUserShouldBeFound() {
        Mockito.when(userRepository.findByUserName(userTest.getUserName()))
                .thenReturn(Optional.of(userTest));

        User userFound = userService.findByUserName(userTest.getUserName()).get();
        assertEquals(userRepository.findByUserName(userTest.getUserName()).get().getUserName(),
                userFound.getUserName());
    }

    @Test
    void whenUserNotFound_ThenUserIsEmpty() {
        Mockito.when(userRepository.findByUserName(userTest.getUserName()))
                .thenReturn(Optional.empty());

        assertThat(userService.findByUserName(userTest.getUserName())).isEmpty();
    }

    //POST METHODS
    @Test
    void whenNewUserThatNotExistsIsAdded_ThenUserIsReturned() {
        User newUser = new User("Matias", "matibenitez", LocalDate.of(1997, 10, 1));

        Mockito.when(userRepository.findAll()).thenReturn(Collections.singletonList(userTest));
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);

        assertThat(userService.insertUser(newUser)).isNotNull();
        assertThat(userService.insertUser(newUser)).isEqualTo(newUser);
    }

    @Test
    void whenNewUserThatExistsIsAdded_ThenUserAlreadyOwnExceptionIsThrow() {
        Mockito.when(userRepository.findAll()).thenReturn(Collections.singletonList(userTest));

        assertThrows(UserAlreadyOwnException.class, () -> userService.insertUser(userTest));
    }

    //DELETE METHODS
    @Test
    void whenExistingUserIsDeleted_ThenCountOfDeletionsAreReturned() {
        Mockito.when(userRepository.findByUserName(userTest.getUserName()))
                .thenReturn(Optional.of(userTest));
        Mockito.when(userRepository.deleteByUserName(userTest.getUserName())).thenReturn(1);

        assertThat(userService.deleteByUserName(userTest.getUserName())).isEqualTo(1);

    }

    @Test
    void whenUserThatNotExistsIsDeleted_ThenUserNotFoundIsThrown() {
        Mockito.when(userRepository.findByUserName(userTest.getUserName()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteByUserName(
                userTest.getUserName()));
    }

    //UPDATE METHODS
    @Test
    void whenUpdateUserThatExists_ThenReturnUser() {
        User newUser = new User("Julian", "matibenitez", LocalDate.of(1997, 10, 1));

        Mockito.when(userRepository.findByName(userTest.getName()))
                .thenReturn(Optional.of(userTest));
        userTest.setUserName(newUser.getUserName());
        userTest.setBirthDate(newUser.getBirthDate());

        assertThat(userService.updateUser(userTest, userTest.getName()))
                .isEqualTo(Optional.of(newUser));
    }

    //
    @Test
    void whenUpdateUserThatNotExists_ThenUserNotFoundExceptionIsThrown() {
        Mockito.when(userRepository.findByName(userTest.getName())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userTest,
                userTest.getName()));
    }
}
