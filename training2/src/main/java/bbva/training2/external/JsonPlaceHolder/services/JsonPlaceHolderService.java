package bbva.training2.external.JsonPlaceHolder.services;

import bbva.training2.external.JsonPlaceHolder.dto.Example;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class JsonPlaceHolderService {

    @Value("${jsonPlaceHolder.baseUrl}")
    private String url;

    public List<Example> getExample() throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<List<Example>> parameterizedTypeReference = new ParameterizedTypeReference<List<Example>>() {
        };

        //headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("X-COM-PERSIST", "NO");
        headers.set("X-COM-LOCATION", "BA");
        headers.setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        try {
            List<Example> response = restTemplate
                    .exchange(url, HttpMethod.GET, entity, parameterizedTypeReference).getBody();
            log.info("---- All: '{}'", response.toString());
            log.info("----- HEADERS: '{}'", entity.getHeaders());
            return response;
        } catch (NoSuchElementException e) {
            log.error("NoSuchElementException: ", e.getMessage());
            throw new NoSuchElementException("There is no element to display");
        } catch (NullPointerException e) {
            log.error("NullPointerException ", e.getMessage());
            throw new NullPointerException("Null");
        } catch (Exception e) {
            log.error("Exception", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }


}