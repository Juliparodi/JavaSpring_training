package bbva.training2.external.services;

import bbva.training2.adapters.BookAdapter;
import bbva.training2.exceptions.errors.BookHttpErrors;
import bbva.training2.models.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenLibraryService {
    @Autowired
    private BookAdapter bookAdapter;

    @Autowired
    private RestTemplate restTemplate;


    @Value("${openLibrary.baseUrl}")
    private String urlWithoutIsbn;

    public OpenLibraryService(){
    }

    public Book bookInfo(String isbn) {
        String param = "ISBN:" + isbn;
        String uri = String.format(urlWithoutIsbn, param);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            JsonNode root = mapper.readTree(response.getBody());
            System.out.println(response.getStatusCodeValue());
            System.out.println(response.getHeaders());
            System.out.println(response.getBody());
            System.out.println(response.getStatusCode());
            return bookAdapter.transformBookDTOToBook(bookAdapter.createBookDTO(isbn, root.iterator().next()));
        } catch (JsonProcessingException e) {
            new BookHttpErrors("Book Not Found").bookNotFound();
        }
        catch (NoSuchElementException e){
            new BookHttpErrors("Book not found").bookNotFound();
        }
        return null;
    }
}
