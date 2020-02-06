package br.com.virtuallibrary.config;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuração do swagger. Para acesso, link abaixo:
 * http://localhost:8080/v2/api-docs
 * http://localhost:8080/swagger-ui.html#/
 */
@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Bean
    public Docket api() {
        List<ResponseMessage> list = new java.util.ArrayList<>();
        list.add(new ResponseMessageBuilder()
                .code(401)
                .message("Você não tem permissão para acessar esse recurso")
                .build());
        list.add(new ResponseMessageBuilder()
                .code(403)
                .message("É proibido acessar o recurso")
                .build());
        list.add(new ResponseMessageBuilder()
                .code(404)
                .message("O recurso que você estava tentando acessar não foi encontrado")
                .build());
        list.add(new ResponseMessageBuilder()
                .code(500)
                .message("Erro interno do servidor")
                .build());

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.virtuallibrary.controllers"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .directModelSubstitute(LocalDate.class, String.class)
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, list)
                .globalResponseMessage(RequestMethod.POST, list)
                .globalResponseMessage(RequestMethod.PUT, list)
                .globalResponseMessage(RequestMethod.DELETE, list)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Virtual Library REST API",
                "API para cadastro e consulta de livros.",
                "0.1.0",
                "https://smartbear.com/terms-of-use/",
                new Contact("Daniel Oliveira", "", "danielso2007@gmail.com"),
                "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0.html", Collections.emptyList());
    }

}
