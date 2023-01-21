package cv.pn.sibs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class SIBSApplication {

	public static void main(String[] args) {
		SpringApplication.run(SIBSApplication.class, args);
	}

}
