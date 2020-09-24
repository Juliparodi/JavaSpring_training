package bbva.training2.controllers;

import bbva.training2.external.JsonPlaceHolder.dto.Example;
import bbva.training2.external.JsonPlaceHolder.dto.JsonPlaceHolderDTO;
import bbva.training2.external.JsonPlaceHolder.services.JsonPlaceHolderService;
import bbva.training2.external.PublicApi.dto.PublicApiDTO;
import bbva.training2.external.PublicApi.service.PublicApisService;
import bbva.training2.external.sumapuntos.dto.DataDTO;
import bbva.training2.external.sumapuntos.dto.PuntosDTO;
import bbva.training2.external.sumapuntos.repository.IPuntosRepository;
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
@RequestMapping("/api/public-api")
public class APIsController {

    @Autowired
    PublicApisService publicApisService;

    @Autowired
    JsonPlaceHolderService jsonPlaceHolderService;

    @Autowired
    IPuntosRepository iPuntosRepository;

    DataDTO dataDTO = new DataDTO();

    @GetMapping("/{param}")
    public ResponseEntity<PublicApiDTO> getRandom(@PathVariable String param) {
        if (param.isEmpty()) {
            throw new NoSuchElementException("No param");
        }
        return new ResponseEntity(publicApisService.publicApiInfo(param), HttpStatus.OK);
    }

    @GetMapping("/JsonPlaceHolder")
    public ResponseEntity<List<Example>> getExample() {
        return new ResponseEntity(jsonPlaceHolderService.getExample(), HttpStatus.OK);
    }

    @GetMapping("/JsonPlaceHolder/{id}")
    public ResponseEntity<List<JsonPlaceHolderDTO>> getComments(@PathVariable Integer id) {
        if (id == (null)) {
            throw new NullPointerException("ID is null, please enter a number");
        }
        return new ResponseEntity<>(jsonPlaceHolderService.getComments(id), HttpStatus.OK);
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

    @PostMapping("/sumapuntos")
    public ResponseEntity<PuntosDTO> postPuntos(@RequestBody PuntosDTO puntosDTO) {
        puntosDTO.addDataDTO(puntosDTO.getData()); //to make FK work

        /*
           DataDTO dataDTO1 = puntosDTO.getData();
        for (int i = 0; i < dataDTO1.getHistorial().size(); i++) {
            dataDTO.addHistorial(dataDTO1.getHistorial().get(i));
        }
         */

        return new ResponseEntity<>(iPuntosRepository.save(puntosDTO), HttpStatus.CREATED);
    }

    // DataDTO dataDTO = puntosDTO.getData();
    //   dataDTO.addDataDTO(puntosDTO);
    @GetMapping("/sumapuntos")
    public ResponseEntity<List<PuntosDTO>> getPuntos() {
        return new ResponseEntity<>(iPuntosRepository.findAll(), HttpStatus.OK);
    }
}
