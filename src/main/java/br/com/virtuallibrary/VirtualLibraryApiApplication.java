package br.com.virtuallibrary;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(value = "br.com.virtuallibrary")
@EnableAutoConfiguration
@SpringBootApplication
public class VirtualLibraryApiApplication {

	public static void main(String[] args) {
		final SpringApplication application = new SpringApplication(VirtualLibraryApiApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.setWebApplicationType(WebApplicationType.SERVLET);
		application.run(args);
	}

}
