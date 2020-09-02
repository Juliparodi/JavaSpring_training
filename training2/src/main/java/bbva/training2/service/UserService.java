package bbva.training2.service;

import bbva.training2.models.Book;
import bbva.training2.models.User;

import bbva.training2.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;
    List<User> users;

    public List<User> addAll(List<User> users) {
        return userRepository.saveAll(users);
    }

    public List<User> getAll() { return userRepository.findAll(); }

    public Optional<User> findByUserName(String userName) {
        if (!userRepository.findByUserName(userName).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "user with given UserName doesn't exist in our database");
        }
        return userRepository.findByUserName(userName);
    }

    public User insertUser(User user) {
        users = userRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).equals(user)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "User with given name is already defined in our database");
            }
        }
        return userRepository.save(user);
    }

    public Integer deleteByUserName(String userName) {
        users = userRepository.findAll();
        if (!userRepository.findByUserName(userName).isPresent()) {
            throw new ResponseStatusException((HttpStatus.NOT_FOUND),
                "user with given USerName is not in our database");
        }
        return userRepository.deleteByUserName(userName);
    }


    public User updateUser(User user, String name) {
        if (!userRepository.findByName(name).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "cannot update user with that name since is not in our DB");
        }
        User userToUpdate = userRepository.findByName(name).get();
        userToUpdate.setUserName(user.getUserName());
        userToUpdate.setBirthDate(user.getBirthDate());
        return userToUpdate;
    }

    public List<User> foundUserByBetweenBirthday(LocalDate date1, LocalDate date2) {
        List<User> booksFound = userRepository.findAll();
        booksFound.stream().filter(x -> x.getBirthDate().isAfter(date1) && x.getBirthDate().isBefore(date2))
            .collect(Collectors.toList());
        return booksFound;
    }
}
