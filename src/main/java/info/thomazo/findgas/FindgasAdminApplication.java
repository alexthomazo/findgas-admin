package info.thomazo.findgas;

import info.thomazo.findgas.service.ImportService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class FindgasAdminApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run(FindgasAdminApplication.class, args);

		ImportService importService = context.getBean(ImportService.class);
		importService.importStation(null);
	}
}
