package br.com.virtuallibrary.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

	@Bean
    public OpenAPI customOpenAPI() {
		Contact contact = new Contact();
    	contact.setName("Daniel Oliveira");
    	contact.setUrl("https://github.com/danielso2007/virtualLibraryAPI");
    	contact.setEmail("danielso2007@gmail.com");
    	
    	License license = new License();
    	license.setName("Apache 2.0");
    	license.setUrl("http://www.apache.org/licenses/LICENSE-2.0.html");
    	
    	List<Server> servers = new ArrayList<>();
    	
    	Server dev = new Server();
    	dev.setUrl("http://localhost:8080");
    	dev.setDescription("DEV Server");
    	
    	Server prod = new Server();
    	prod.setUrl("http://localhost:8080");
    	prod.setDescription("PROD Server");
    	
    	servers.add(dev);
    	servers.add(prod);
    	
    	Components components = new Components();
    	
        return new OpenAPI()
                .components(components)
                .info(new Info()
                		.title("Virtual Library REST API")
                		.description("API para cadastro e consulta de livros.")
                		.version("0.4.0")
                		.termsOfService("https://smartbear.com/terms-of-use/")
                		.contact(contact)
                		.license(license)
                ).servers(servers);
    }
	
}
