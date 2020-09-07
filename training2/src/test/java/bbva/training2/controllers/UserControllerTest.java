package bbva.training2.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import bbva.training2.models.User;
import bbva.training2.repository.UserRepository;
import bbva.training2.service.BookService;
import bbva.training2.service.UserService;
import bbva.training2.utils.VariableConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.time.LocalDate;
import java.util.Optional;
import javax.swing.Spring;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import springfox.documentation.spring.web.json.Json;

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
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userTest = new User ("julianparodi", "juli", LocalDate
            .of(1997, 10, 01));
    }


    @Test
    void whenFindByUsernameWhichExist_thenUserIsReturned() throws Exception {
        JsonNode jsonUser = mapper.readValue(new File("./JsonFiles/createUser.json"), JsonNode.class);
        Mockito.when(userService.findByUserName(userTest.getUserName()))
            .thenReturn(Optional.of(userTest));
        String url = VariableConstants.USER_URL.concat(userTest.getUserName());
        mvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(jsonUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("julianparodi")));
    }

    @Test
    public void whenCreateUser_thenUserIsReturned() throws Exception{
        JsonNode jsonCreateUser = mapper.readValue(new File("./JsonFiles/createUser.json"), JsonNode.class);
        JsonNode jsonUser = mapper.readValue(new File("./JsonFiles/UserwithoutBook.json"), JsonNode.class);
        Mockito.when(userService.insertUser(userTest))
            .thenReturn((userTest));
        mvc.perform(post(VariableConstants.USER_URL.concat("add"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonCreateUser.toString()))
            .andExpect(status().isCreated())
            .andExpect(content().json(jsonUser.toString()))
            .andExpect(jsonPath("$.name", is("julianparodi")));
    }

     @Test
    public void whenUpdateUser_thenUserIsReturned() throws Exception{
         JsonNode jsonUpdateUser = mapper.readValue(new File("./JsonFiles/updateUser.json"), JsonNode.class);
         Mockito.when(userService.updateUser(userTest, userTest.getName()))
             .thenReturn(userTest);
         userTest.setUserName("mati");
         mvc.perform(put(VariableConstants.USER_URL.concat("update/" + userTest.getName()))
             .contentType(MediaType.APPLICATION_JSON)
             .content(jsonUpdateUser.toString()))
             .andExpect(status().isOk())
             .andExpect(content().json(jsonUpdateUser.toString()))
             .andExpect(jsonPath("$.userName", is("lau")));

     }

    @Test
    public void whenUpdateUserThatNotExist_thenMessageIsReturned() throws Exception{
        JsonNode jsonUser = mapper.readValue(new File("./JsonFiles/updateUser.json"), JsonNode.class);
        mvc.perform(put(VariableConstants.USER_URL.concat("update/" + "mati"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonUser.toString()))
            .andExpect(status().isNotFound());

    }



}