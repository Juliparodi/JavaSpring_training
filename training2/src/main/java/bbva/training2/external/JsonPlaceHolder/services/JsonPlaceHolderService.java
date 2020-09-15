package bbva.training2.external.JsonPlaceHolder.services;

import bbva.training2.exceptions.JsonPlaceHolderException;
import bbva.training2.external.JsonPlaceHolder.dto.Example;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class JsonPlaceHolderService {

    @Value("${json_place_holder_baseUrl}")
    private String url;

    //GET METHOD
    public List<Example> getExample() {

        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<List<Example>> parameterizedTypeReference = new ParameterizedTypeReference<List<Example>>() {
        };

        //headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-COM-LOCATION", "BA");
        headers.setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        try {
            ResponseEntity<List<Example>> response = restTemplate
                    .exchange(url, HttpMethod.GET, entity, parameterizedTypeReference);
            log.info("---- All: '{}'", response.toString());
            log.info("----- HEADERS: '{}'", entity.getHeaders());
            log.info("---- STATUS CODE: {} ", response.getStatusCode());
            return response.getBody();
        } catch (NoSuchElementException e) {
            log.error("NoSuchElementException: {}", e.getMessage());
            throw new NoSuchElementException("There is no element to display");
        } catch (NullPointerException e) {
            log.error("NullPointerException {}", e.getMessage());
            throw new NullPointerException("Null");
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
            throw new JsonPlaceHolderException(
                    "there was an error with JsonPlaceHolder integration", e);
        }
    }

    //POST METHOD
    public Example postExample(Example example) {
        RestTemplate restTemplate = new RestTemplate();

        //headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Example> request = new HttpEntity<>(example, headers);

        try {
            ResponseEntity<Example> response = restTemplate
                    .exchange(url, HttpMethod.POST, request, Example.class);

            log.info("------- {} ", request);
            log.info("STATUS CODE: {}", response.getStatusCode());
            log.info("HEADERS: {} ", response.getHeaders());
            return response.getBody();
        } catch (Exception ex) {
            throw new JsonPlaceHolderException("POST failed", ex);
        }


    }


}