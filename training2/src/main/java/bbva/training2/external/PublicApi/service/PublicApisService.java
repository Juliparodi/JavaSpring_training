package bbva.training2.external.PublicApi.service;

import bbva.training2.adapters.PublicApiAdapter;
import bbva.training2.exceptions.PublicApiServiceException;
import bbva.training2.external.PublicApi.dto.PublicApiDTO;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.NoSuchElementException;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Transactional
@Component
@Slf4j
public class PublicApisService {

    @Value("${public_apis_org}")
    private String urlWithoutIsbn;

    @Autowired
    private PublicApiAdapter publicApiAdapter;

    @Autowired
    public PublicApisService() {
    }

    public PublicApiDTO publicApiInfo(String param) {
        String url = urlWithoutIsbn.concat(param);

        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<PublicApiDTO> parameterizedTypeReference = new ParameterizedTypeReference<PublicApiDTO>() {
        };

        //headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-COM-LOCATION", "BA");
        headers.setAcceptCharset(
                Collections.singletonList(StandardCharsets.UTF_8)); //ask Lean charset=UTF-8

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        try {
            ResponseEntity<PublicApiDTO> response = restTemplate
                    .exchange(url, HttpMethod.GET, entity,
                            parameterizedTypeReference);

            log.info("---- All: '{}'", response.toString());
            log.info("----- HEADERS: '{}'", entity.getHeaders());
            log.info("---- STATUS CODE: {}", response.getStatusCode());
            return response.getBody();
        } catch (NoSuchElementException e) {
            log.error("NoSuchElementException: {}", e.getMessage());
            throw new NoSuchElementException("There is no element to display");
        } catch (NullPointerException e) {
            log.error("NullPointerException {}", e.getMessage());
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
            throw new PublicApiServiceException("Public Api service error integrating with URL", e);
        }
        return null;
    }
}

/*
try {
            ResponseEntity<PublicApiDTO> response = restTemplate
                    .exchange(url, HttpMethod.GET, entity,
                            parameterizedTypeReference).getBody();

            log.info("---- All: '{}'", response.toString());
            log.info("------- STATUS CODE: '{}'", response.getStatusCode());
            log.info("-------- HEADERS: '{}'", response.getHeaders());
            return response;
 */
