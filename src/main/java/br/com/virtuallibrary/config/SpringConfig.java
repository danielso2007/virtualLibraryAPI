package br.com.virtuallibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class SpringConfig {

	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean() {
	    return new LocalValidatorFactoryBean();
	}

	@Bean
	public ValidatingMongoEventListener validatingMongoEventListener(LocalValidatorFactoryBean lfb) {
	    return new ValidatingMongoEventListener(lfb);
	}

	@Bean
	public ObjectMapper objectMapperBuilder() {
		ObjectMapper builder = new ObjectMapper();
		builder.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		builder.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		builder.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		builder.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return builder;
	}

}
