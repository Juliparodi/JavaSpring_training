package bbva.training2.external.services;

import bbva.training2.adapters.PublicApiAdapter;
import bbva.training2.exceptions.errors.BookHttpErrors;
import bbva.training2.external.dto.PublicApiDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class PublicApisService {

    @Value("${publicapis.org}")
    private String urlWithoutIsbn;

    @Autowired
    private PublicApiAdapter publicApiAdapter;

    public PublicApisService() {
    }

    public PublicApiDTO publicApiInfo(String param) {
        String url = urlWithoutIsbn.concat(param);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            JsonNode root = mapper.readTree(response.getBody());
            log.info(String.valueOf(response.getStatusCodeValue()));
            log.info(String.valueOf(response.getHeaders()));
            log.info(response.getBody());
            log.info(String.valueOf(response.getStatusCode()));
            return publicApiAdapter.createApiDTO(root.iterator().next());
        } catch (JsonProcessingException e) {
            log.error("JsonProccessingException: ", e.getMessage());
            new BookHttpErrors("Book Not Found").bookNotFound();
        } catch (NoSuchElementException e) {
            log.error("NoSuchElementException: ", e.getMessage());
            new BookHttpErrors("Book not found").bookNotFound();
        }
        return null;
    }
}
