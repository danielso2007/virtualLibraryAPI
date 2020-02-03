package br.com.virtuallibrary.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import br.com.virtuallibrary.commons.Constants;

public abstract class TestBaseController {
	
	protected static final String ADMIN_ROLE = "ADMIN";
	protected static final String USER_ROLE = "USER";
	protected static final String ADMIN = "admin";
	
	protected JacksonTester<Map<String, String>> jsonEntityFields;
	
	@Autowired
	private MockMvc mockMvc;
	
	protected MockHttpServletResponse getHttpServletResponse(String url, ResultMatcher status) throws Exception {
		return httpServletResponse(HttpMethod.GET, url, null, status);
	}

	protected MockHttpServletResponse putHttpServletResponse(String url, String json, ResultMatcher status)
			throws Exception {
		return httpServletResponse(HttpMethod.PUT, url, json, status);
	}

	protected MockHttpServletResponse deleteHttpServletResponse(String url, ResultMatcher status) throws Exception {
		return httpServletResponse(HttpMethod.DELETE, url, null, status);
	}

	protected MockHttpServletResponse postHttpServletResponse(String url, String json, ResultMatcher status)
			throws Exception {
		return httpServletResponse(HttpMethod.POST, url, json, status);
	}

	protected MockHttpServletResponse httpServletResponse(HttpMethod httpMethod, String url, String json, ResultMatcher status) throws Exception {
		MockHttpServletRequestBuilder requestBuilder = null;

		if (HttpMethod.POST.equals(httpMethod)) {
			requestBuilder = post(url).contentType(Constants.APPLICATION_JSON_UTF_8);
		} else if (HttpMethod.GET.equals(httpMethod)) {
			requestBuilder = get(url).contentType(Constants.APPLICATION_JSON_UTF_8);
		} else if (HttpMethod.PUT.equals(httpMethod)) {
			requestBuilder = put(url).contentType(Constants.APPLICATION_JSON_UTF_8);
		} else if (HttpMethod.DELETE.equals(httpMethod)) {
			requestBuilder = delete(url).contentType(Constants.APPLICATION_JSON_UTF_8);
		}

		if (json != null) {
			Objects.requireNonNull(requestBuilder).content(json);
		}

		return mockMvc.perform(Objects.requireNonNull(requestBuilder)).andDo(print()).andExpect(status).andReturn()
				.getResponse();
	}

}
