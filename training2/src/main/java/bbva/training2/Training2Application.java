package bbva.training2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication()
@EnableCaching
@Slf4j
public class Training2Application {

    public static void main(String[] args) {
        log.info("starting app.... ");
        SpringApplication.run(Training2Application.class, args);
    }

    /*
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

     */

}
