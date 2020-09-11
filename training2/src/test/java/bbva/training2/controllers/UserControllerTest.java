package bbva.training2.controllers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bbva.training2.models.Book;
import bbva.training2.models.User;
import bbva.training2.repository.UserRepository;
import bbva.training2.service.BookService;
import bbva.training2.service.UserService;
import bbva.training2.utils.VariableConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserController userController;

    @Autowired
    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private UserService userService;
    @MockBean
    private BookService bookService;
    private User userTest;
    private Book bookTest;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userTest = new User("julianparodi", "juli", LocalDate
                .of(1997, 10, 01));
        bookTest = new Book("terror", "Shakespere", "imagen", "IT", "NoSe", "No se", "2019",
                150, "123");
    }


    @Test
    void whenFindByUsernameWhichExist_thenUserIsReturned() throws Exception {
        JsonNode jsonUser = mapper
                .readValue(new File("./JsonFiles/createUser.json"), JsonNode.class);
        Mockito.when(userService.findByUserName(userTest.getUserName()))
                .thenReturn(Optional.of(userTest));
        String url = VariableConstants.USER_URL.concat(userTest.getUserName());
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(jsonUser)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is("julianparodi")))
                .andReturn();
    }

    @Test
    void whenFindAllUsers_thenUsersAreReturned() throws Exception {
        JsonNode jsonUser = mapper
                .readValue(new File("./JsonFiles/createUsers.json"), JsonNode.class);
        List<User> userList = Arrays.asList(new User("julianparodi", "juli", LocalDate
                .of(1997, 10, 01)), new User("mati", "MatiBenitez", LocalDate
                .of(1997, 10, 02)), new User("lau", "LauFernandez", LocalDate
                .of(1997, 10, 03)));
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        String url = VariableConstants.USER_URL;
        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(jsonUser)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].userName", is("juli")))
                .andReturn();
    }

    @Test
    public void whenCreateUser_thenUserIsReturned() throws Exception {
        JsonNode jsonCreateUser = mapper
                .readValue(new File("./JsonFiles/createUser.json"), JsonNode.class);
        JsonNode jsonUser = mapper
                .readValue(new File("./JsonFiles/UserwithoutBook.json"), JsonNode.class);
        Mockito.when(userService.insertUser(userTest))
                .thenReturn((userTest));
        mvc.perform(post(VariableConstants.USER_URL.concat("user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateUser.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonUser.toString()))
                .andExpect(jsonPath("$.name", is("julianparodi")))
                .andReturn();
    }

    @Test
    public void whenAddingBookToUserCollection_ThenUserIsReturned() throws Exception {
        Mockito.when(userService.findByUserName(userTest.getUserName())).thenReturn(
                Optional.ofNullable((userTest)));
        Mockito.when((bookService.findByTitle(bookTest.getTitle()))).thenReturn(bookTest);
        String url = (VariableConstants.USER_URL.concat("books") + String
                .format("?title=%s&username=%s", bookTest.getTitle(), userTest.getUserName()));
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void whenAddingNullBookToUserCollection_ThenMessageIsReturned() throws Exception {
        Mockito.when(userService.findByUserName(userTest.getUserName()))
                .thenReturn(Optional.ofNullable(userTest));
        Mockito.when((bookService.findByTitle(bookTest.getTitle()))).thenReturn(bookTest);
        String url = (VariableConstants.USER_URL.concat("books") + String
                .format("?title=%s&username=%s", "Hercules", userTest.getUserName()));
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateUserByName_thenUserIsReturned() throws Exception {
        JsonNode jsonUpdateUser = mapper
                .readValue(new File("./JsonFiles/updateUser.json"), JsonNode.class);
        Mockito.when(userService.updateUser(userTest, userTest.getName()))
                .thenReturn(userTest);
        userTest.setUserName("lau");
        userTest.setBirthDate(LocalDate.of(1997, 10, 01));
        mvc.perform(put(VariableConstants.USER_URL.concat(userTest.getName()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUpdateUser.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", is("lau")))
                .andReturn();

    }

    @Test
    public void whenUpdateUserThatNotExist_thenMessageIsReturned() throws Exception {
        JsonNode jsonUser = mapper
                .readValue(new File("./JsonFiles/updateUserNotExists.json"), JsonNode.class);

        mvc.perform(put(VariableConstants.USER_URL.concat("juli"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteUserByUserName_ThenCountOfDeletionsAreReturned() throws Exception {
        Mockito.when(userService.insertUser(userTest))
                .thenReturn((userTest));
        mvc.perform(delete(VariableConstants.USER_URL.concat("juli"))
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("username", userTest.getUserName()))
                .andExpect(status().isOk())
                .andReturn();
    }


}