package bbva.training2.external.PublicApi.service;

import bbva.training2.adapters.PublicApiAdapter;
import bbva.training2.external.PublicApi.dto.PublicApiDTO;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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

    public PublicApiDTO publicApiInfo(String param) throws Exception {
        String url = urlWithoutIsbn.concat(param);

        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<PublicApiDTO> parameterizedTypeReference = new ParameterizedTypeReference<PublicApiDTO>() {
        };

        try {
            PublicApiDTO response = restTemplate.exchange(url, HttpMethod.GET, null,
                    parameterizedTypeReference).getBody();
            log.info("---- All: '{}'", response.toString());
            return response;
        } catch (NoSuchElementException e) {
            log.error("NoSuchElementException: ", e.getMessage());
            throw new NoSuchElementException("There is no element to display");
        } catch (NullPointerException e) {
            log.error("NullPointerException ", e.getMessage());
        } catch (Exception e) {
            log.error("Exception", e.getMessage());
            throw new Exception(e.getMessage());
        }
        return null;
    }
}
