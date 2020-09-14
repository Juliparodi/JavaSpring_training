package bbva.training2.service;

import bbva.training2.exceptions.UserAlreadyOwnException;
import bbva.training2.exceptions.errors.UserHttpErrors;
import bbva.training2.models.User;
import bbva.training2.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    List<User> users;

    @ApiOperation(value = "Given username, return user object and if it's empty, return an exception", response = User.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Optional<User> findByUserName(String userName) {
        if (!userRepository.findByUserName(userName).isPresent()) {
            new UserHttpErrors("user with given UserName doesn't exist in our database")
                    .userNotFound();
        }
        return userRepository.findByUserName(userName);
    }

    @ApiOperation(value = "add user object to our DB and if it's already there, return an UserHttpError", response = User.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public User insertUser(User user) {
        users = userRepository.findAll();
        if (users.stream().anyMatch(x -> user.equals(x))) {
            throw new UserAlreadyOwnException("User is already in our DB");
        }
        return userRepository.save(user);
    }

    @ApiOperation(value = "delete User by userName filter and if it's empty, return an UserHttpError", response = User.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Integer deleteByUserName(String userName) {
        users = userRepository.findAll();
        if (!userRepository.findByUserName(userName).isPresent()) {
            new UserHttpErrors("User is not in our DB").userNotFound();
        }
        return userRepository.deleteByUserName(userName);
    }

    @ApiOperation(value = "filter User by name and if it's found, update with new JSON otherwise, return an exception", response = User.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public User updateUser(User user, String name) {
        if (!userRepository.findByName(name).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "cannot update user with that name since is not in our DB");
        }
        User userToUpdate = userRepository.findByName(name).get();
        userToUpdate.setUserName(user.getUserName());
        userToUpdate.setBirthDate(user.getBirthDate());
        userToUpdate.setName(name);
        return userToUpdate;
    }

    public List<User> foundUserByBetweenBirthday(LocalDate date1, LocalDate date2) {
        return userRepository.findAll().stream()
                .filter(x -> x.getBirthDate().isAfter(date1) && x.getBirthDate().isBefore(date2))
                .collect(Collectors.toList());
    }
}
