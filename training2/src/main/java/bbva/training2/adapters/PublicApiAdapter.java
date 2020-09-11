package bbva.training2.adapters;

import bbva.training2.external.dto.Entries;
import bbva.training2.external.dto.PublicApiDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class PublicApiAdapter {

    public PublicApiDTO createApiDTO(JsonNode request) {
        PublicApiDTO publicApiDTO = new PublicApiDTO();
        publicApiDTO.setCount(request.path("count").intValue());
        publicApiDTO.setEntries((Entries[]) request.findValues("entries").stream().toArray());
        return publicApiDTO;
    }


}

