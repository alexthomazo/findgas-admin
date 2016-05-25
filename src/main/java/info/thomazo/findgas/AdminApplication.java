package info.thomazo.findgas;

import info.thomazo.findgas.service.ImportService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class AdminApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run(AdminApplication.class, args);

		ImportService importService = context.getBean(ImportService.class);
//		importService.updateMapping("patch");
//		importService.importStation("/home/alex/tmp/PrixCarburants_quotidien_20160514.xml");
	}
}
