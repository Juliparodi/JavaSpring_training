package bbva.training2.controllers;


import bbva.training2.exceptions.errors.UserHttpErrors;
import bbva.training2.models.Book;
import bbva.training2.models.User;
import bbva.training2.repository.UserRepository;
import bbva.training2.service.BookService;
import bbva.training2.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("v1/users/")
@Api
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("addall")
    @ApiOperation(value = "given a User list, add all to our repository", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "succesfully users added"),
        @ApiResponse(code = 404, message = "User/users not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 401, message = "Access unauthorized."),
        @ApiResponse(code = 403, message = "Access unauthorized."),
        @ApiResponse(code = 409, message = "User/users already on the DB."),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<List<User>> addUser(@RequestBody List<User> users) {
        return new ResponseEntity(userRepository.saveAll(users), HttpStatus.CREATED);
    }

    @PostMapping("add")
    @ApiOperation(value = "given a User object, add it to our repository", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully login user"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 401, message = "Access unauthorized."),
        @ApiResponse(code = 403, message = "Access unauthorized."),
        @ApiResponse(code = 409, message = "User is already on the DB."),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<User> addUser
        (@ApiParam(value = "user JSON to add it to our repo", required = true) @RequestBody User user) {
        return new ResponseEntity(userService.insertUser(user), HttpStatus.CREATED);
    }

    @GetMapping()
    @ApiOperation(value = "return all users in database", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully response"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 401, message = "Access unauthorized."),
        @ApiResponse(code = 403, message = "Access unauthorized."),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("{userName}")
    @ApiOperation(value = "Given a username, return User object", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully response"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 401, message = "Access unauthorized."),
        @ApiResponse(code = 403, message = "Access unauthorized."),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Optional<User>> getUserName(@PathVariable String userName){
        return new ResponseEntity(Optional.of(userService.findByUserName(userName)), HttpStatus.OK);
    }

    @GetMapping("books/{userName}")
    @ApiOperation(value = "Given a username, return his/her collection of book", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully collection of books response"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 401, message = "Access unauthorized."),
        @ApiResponse(code = 403, message = "Access unauthorized."),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<List<Book>> getBookCollections(@PathVariable String userName){
        User userFound = userService.findByUserName(userName).get();
        return new ResponseEntity(userFound.getBooks(), HttpStatus.OK);
    }

    @GetMapping("find/birthdate")
    @ApiOperation(value = "Given two dates, return values in between", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully collection of books response"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 401, message = "Access unauthorized."),
        @ApiResponse(code = 403, message = "Access unauthorized."),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<List<Book>> filterByDates(@RequestParam(name = "startDate") String date1,
        @RequestParam(name = "endDate") String date2) {
        return new ResponseEntity(userService.foundUserByBetweenBirthday(
            LocalDate.parse(date1), LocalDate.parse(date2)), HttpStatus.OK);
    }

    @GetMapping("find/sequence")
    @ApiOperation(value = "Given a sequence, find by contains name, and return the user list or an exception", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get list of user"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 401, message = "Access unauthorized."),
        @ApiResponse(code = 403, message = "Access unauthorized."),
        @ApiResponse(code = 409, message = "User is already on the DB."),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<List<User>> foundUserByContainsName(@RequestParam ("sequence") String sequence){
        List<User> list = userRepository.findByNameContaining(sequence);
        if (list.isEmpty()){
            new UserHttpErrors("Users not found").userNotFound();
        }
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PutMapping("update/{name}")
    @ApiOperation(value = "Given a name, update user", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get list of user"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 401, message = "Access unauthorized."),
        @ApiResponse(code = 403, message = "Access unauthorized."),
        @ApiResponse(code = 409, message = "User is already on the DB."),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable String name) {
        if (!user.getName().equalsIgnoreCase(name)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "name of user object and name of the parameter didn't match");
        }
        return new ResponseEntity(userService.updateUser(user, name), HttpStatus.OK);
    }

    @PutMapping ("books")
    @ApiOperation(value = "Given title of the book and username, update user's book collection", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get list of user"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 401, message = "Access unauthorized."),
        @ApiResponse(code = 403, message = "Access unauthorized."),
        @ApiResponse(code = 409, message = "User is already on the DB."),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> addBookToCollection(@RequestParam(name="title", required = true) String title,
        @RequestParam(name="username", required = true) String userName){

        User userFound = userService.findByUserName(userName).get();
        Book bookToAdd = bookService.findByTitle(title);
        userFound.addBook(bookToAdd);
        return new ResponseEntity(userService.updateUser(userFound, userFound.getName()), HttpStatus.OK);
    }

    @DeleteMapping("delete/{userName}")
    @ApiOperation(value = "Given a username, delete that user and return count of users deleted", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get list of user"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 401, message = "Access unauthorized."),
        @ApiResponse(code = 403, message = "Access unauthorized."),
        @ApiResponse(code = 409, message = "User is already on the DB."),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Integer> deleteUser(@PathVariable String userName){
        if (userName.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "parameter userName is empty");
        }
        return new ResponseEntity(userService.deleteByUserName(userName), HttpStatus.OK);
    }

    @DeleteMapping("delete/books")
    @ApiOperation(value = "Given title of the book and username, delete book of that user's collection", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get list of user"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 401, message = "Access unauthorized."),
        @ApiResponse(code = 403, message = "Access unauthorized."),
        @ApiResponse(code = 409, message = "User is already on the DB."),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<List<Book>> deleteBookFromCollection(@RequestParam(name="title", required = true) String title,
        @RequestParam(name="username", required = true) String userName){
        User userFound = userService.findByUserName(userName).get();
        Book bookToDelete = bookService.findByTitle(title);
        userFound.removeBook(bookToDelete);
        return new ResponseEntity(userFound.getBooks(), HttpStatus.OK);

    }
}
