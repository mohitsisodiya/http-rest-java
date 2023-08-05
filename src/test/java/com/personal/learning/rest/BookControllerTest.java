package com.personal.learning.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.json.JSONObject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BookStorage storage;
	
	@BeforeEach
	private void beforeEach() {
		storage.deleteAll();
	}

	private MockHttpServletResponse sendThePostRequest() throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("author", "Mohit Sisodiya");
		String jsonBody = jsonObj.toString();

		return mockMvc.perform(post("/api/v1/books").contentType(MediaType.APPLICATION_JSON).content(jsonBody))
				.andExpect(status().isCreated()).andReturn().getResponse();
	}

	@Test
	void getAll_noBooks_returnsEmptyList() throws Exception {
		mockMvc.perform(get("/api/v1/books")).andExpect(status().isOk()).andExpectAll(content().string("[]"));
	}

	@Test
	void addBook_returnsCreatedBook() throws Exception {
		MockHttpServletResponse response = sendThePostRequest();
		JSONObject result = new JSONObject(response.getContentAsString());
		assertEquals("Mohit Sisodiya", result.getString("author"));
		assertNotNull(result.getInt("id"));
		assertThat(response.getHeader("Location"), is("/api/v1/books/" + result.getInt("id")));
	}

	@Test
	void addBookAndGetSingle_returnBook() throws Exception {
		MockHttpServletResponse response = sendThePostRequest();
		this.mockMvc.perform(get(response.getHeader("Location"))).andExpect(status().isOk())
				.andExpect(content().string(response.getContentAsString()));
	}
	
	@Test
	void getSingle_noBooks_returnsNotFound() throws Exception {
		this.mockMvc.perform(get("/api/v1/books/1")).andExpect(status().isNotFound());
 	}
	
	@Test
	void addMultipleAndGetAll_returnsAddedBooks() throws Exception {
		sendThePostRequest();
		sendThePostRequest();
		this.mockMvc.perform(get("/api/v1/books")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
	}
	
	@Test
	void getSingle_idLessThanOne_returnsBadRequest() throws Exception{
		this.mockMvc.perform(get("/api/v1/books/0")).andExpect(status().isBadRequest());
	}
}
