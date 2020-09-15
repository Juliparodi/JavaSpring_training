package bbva.training2.controllers;

import bbva.training2.external.JsonPlaceHolder.dto.Example;
import bbva.training2.external.JsonPlaceHolder.services.JsonPlaceHolderService;
import bbva.training2.external.PublicApi.dto.PublicApiDTO;
import bbva.training2.external.PublicApi.service.PublicApisService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/publicApi/")
public class APIsController {

    @Autowired
    PublicApisService publicApisService;

    @Autowired
    JsonPlaceHolderService jsonPlaceHolderService;

    @GetMapping("{param}")
    public ResponseEntity<PublicApiDTO> getRandom(@PathVariable String param)
            throws Exception {
        if (param.isEmpty()) {
            throw new NoSuchElementException("No param");
        }
        return new ResponseEntity(publicApisService.publicApiInfo(param), HttpStatus.OK);
    }

    @GetMapping("JsonPlaceHolder")
    public ResponseEntity<List<Example>> getExample() {
        return new ResponseEntity(jsonPlaceHolderService.getExample(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Example> postExample(@RequestBody Example example) {
        Example example1 = new Example();
        example1.setUserId(example.getUserId());
        example1.setTitle(example.getTitle());
        example1.setBody(example.getBody());
        return new ResponseEntity<Example>(jsonPlaceHolderService.postExample(example1),
                HttpStatus.CREATED);
    }
}
