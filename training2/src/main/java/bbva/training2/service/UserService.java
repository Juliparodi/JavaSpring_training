package bbva.training2.service;

import bbva.training2.exceptions.UserAlreadyOwnException;
import bbva.training2.exceptions.UserNotFoundException;
import bbva.training2.models.User;
import bbva.training2.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    List<User> users;

    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


    @ApiOperation(value = "add user object to our DB and if it's already there, return an UserHttpError", response = User.class)
    public User insertUser(User user) {
        users = userRepository.findAll();
        if (users.stream().anyMatch(user::equals)) {
            throw new UserAlreadyOwnException("User is already in our DB");
        }
        return userRepository.save(user);
    }

    @ApiOperation(value = "delete User by userName filter and if it's empty, return an UserHttpError", response = User.class)
    public Integer deleteByUserName(String userName) {
        if (!userRepository.findByUserName(userName).isPresent()) {
            throw new UserNotFoundException("User is not in our DB");
        }
        return userRepository.deleteByUserName(userName);
    }

    @ApiOperation(value = "filter User by name and if it's found, update with new JSON otherwise, return an exception", response = User.class)
    public Optional<User> updateUser(User user, String name) {
        if (!userRepository.findByName(name).isPresent()) {
            throw new UserNotFoundException(
                    "cannot update user with that name since is not in our DB");
        }
        User userToUpdate = userRepository.findByName(name).get();
        userToUpdate.setUserName(user.getUserName());
        userToUpdate.setBirthDate(user.getBirthDate());
        return Optional.of(userToUpdate);
    }

    @ApiOperation(value = "Filter by birtdate and return users in between", response = User.class)
    public List<User> foundUserByBetweenBirthday(LocalDate date1, LocalDate date2) {
        return userRepository.findAll().stream()
                .filter(user -> user.getBirthDate().isAfter(date1) && user.getBirthDate()
                        .isBefore(date2))
                .collect(Collectors.toList());
    }
}
