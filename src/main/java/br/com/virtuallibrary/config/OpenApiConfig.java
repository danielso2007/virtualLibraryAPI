package br.com.virtuallibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

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
    	
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                		.title("Virtual Library REST API")
                		.description("API para cadastro e consulta de livros.")
                		.version("0.4.0")
                		.termsOfService("https://smartbear.com/terms-of-use/")
                		.contact(contact)
                		.license(license)
                );
    }
	
}
