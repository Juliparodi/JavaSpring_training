package bbva.training2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.bbva.training2.models")
@EntityScan("com.bbva.training2.repository")
@SpringBootApplication
public class Training2Application {

	public static void main(String[] args) {
		SpringApplication.run(Training2Application.class, args);
	}

}
