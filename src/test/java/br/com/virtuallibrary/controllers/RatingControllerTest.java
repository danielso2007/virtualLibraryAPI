package br.com.virtuallibrary.controllers;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.virtuallibrary.commons.Constants;
import br.com.virtuallibrary.entity.Rating;
import br.com.virtuallibrary.repositories.RatingRepository;
import br.com.virtuallibrary.services.RatingService;

public class RatingControllerTest {

	public static final String API_BOOKS = "/api/v1/ratings";
	private final String ID = "5dc4c9734e9b1214ed7a9e8a";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RatingRepository resource;

	@MockBean
	private RatingService service;

	private JacksonTester<Rating> json;

	@Before
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	public void contexLoads() {
		assertNotNull(resource);
		assertNotNull(service);
		assertNotNull(mockMvc);
	}

	private MockHttpServletResponse getHttpServletResponse(String url, ResultMatcher status) throws Exception {
		return httpServletResponse(HttpMethod.GET, url, null, status);
	}

	private MockHttpServletResponse putHttpServletResponse(String url, String json, ResultMatcher status)
			throws Exception {
		return httpServletResponse(HttpMethod.PUT, url, json, status);
	}

	private MockHttpServletResponse deleteHttpServletResponse(String url, ResultMatcher status) throws Exception {
		return httpServletResponse(HttpMethod.DELETE, url, null, status);
	}

	private MockHttpServletResponse postHttpServletResponse(String url, String json, ResultMatcher status)
			throws Exception {
		return httpServletResponse(HttpMethod.POST, url, json, status);
	}

	private MockHttpServletResponse httpServletResponse(HttpMethod httpMethod, String url, String json,
			ResultMatcher status) throws Exception {
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
