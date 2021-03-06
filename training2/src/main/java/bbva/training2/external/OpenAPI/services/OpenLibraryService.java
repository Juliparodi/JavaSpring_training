package bbva.training2.external.OpenAPI.services;

import bbva.training2.adapters.BookAdapter;
import bbva.training2.exceptions.NullOpenLibraryServiceException;
import bbva.training2.exceptions.OpenLibraryServiceException;
import bbva.training2.exceptions.errors.BookHttpErrors;
import bbva.training2.external.OpenAPI.dto.BookDTO;
import bbva.training2.models.Book;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class OpenLibraryService {

    @Autowired
    private BookAdapter bookAdapter;

    @Value("${open_library_baseUrl}")
    private String urlWithoutIsbn;

    public OpenLibraryService() {
    }

    public Book bookInfo(String isbn) {
        String param = "ISBN:" + isbn;
        String uri = String.format(urlWithoutIsbn, param);
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<Map<String, BookDTO>> parameterizedTypeReference = new ParameterizedTypeReference<Map<String, BookDTO>>() {
        };

        try {
            ResponseEntity<Map<String, BookDTO>> response = restTemplate
                    .exchange(uri, HttpMethod.GET, null,
                            parameterizedTypeReference, isbn);
            BookDTO bookDTO = (response.getBody().get(("ISBN:" + isbn)));
            log.info("---- All: '{}'", bookDTO.toString());
            log.info("---- STATUS CODE: {}", response.getStatusCode());
            log.info("---- HEADERS: {}", response.getHeaders());
            return bookAdapter.transformBookDTOToBook(bookDTO, isbn);
        } catch (NoSuchElementException e) {
            log.error("NoSuchElementException: {}", e.getMessage());
            new BookHttpErrors("Book not found").bookNotFound();
        } catch (NullPointerException e) {
            log.error("NullPointerException: '{}' ", e.getMessage());
            throw new NullOpenLibraryServiceException("Custom null exception", e);
        } catch (Exception ex) {
            log.error("--- unexpected exception: '{}'", ex.getMessage());
            throw new OpenLibraryServiceException(
                    "there was an error with OpenLibraryService integration", ex);
        }
        return null;
    }
}

/*
    public Book bookInfo(String isbn) {
        String param = "ISBN:" + isbn;
        String uri = String.format(urlWithoutIsbn, param);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            JsonNode root = mapper.readTree(response.getBody());
            log.info(String.valueOf(response.getStatusCodeValue()));
            log.info(String.valueOf(response.getHeaders()));
            log.info(response.getBody());
            log.info(String.valueOf(response.getStatusCode()));
            return bookAdapter.transformBookDTOToBook(
                    bookAdapter.createBookDTO(isbn, root.iterator().next()));
        } catch (JsonProcessingException e) {
            log.error("JsonProccessingException: ", e.getMessage());
            new BookHttpErrors("Book Not Found").bookNotFound();
        } catch (NoSuchElementException e) {
            log.error("NoSuchElementException: ", e.getMessage());
            new BookHttpErrors("Book not found").bookNotFound();
        } catch (NullPointerException e) {
            log.error("NullPointerException: ", e.getMessage());
            throw new NullPointerException("I don't know");
        }
        return null;
    }
}

 */






