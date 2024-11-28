package com.example.spring_boot_project;

import com.example.spring_boot_project.entities.Book;
import com.example.spring_boot_project.repositories.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SpringBootProjectApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void testGetAllBooks() throws Exception {
		Book book = new Book("Test Book", "Author", 2023);
		bookRepository.save(book);

		mockMvc.perform(get("/books"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("Test Book"))
				.andExpect(jsonPath("$[0].author").value("Author"));

		bookRepository.deleteAll();
	}

	@Test
	void testCreateBook() throws Exception {
		Book book = new Book("New Book", "Author", 2022);
		String bookJson = objectMapper.writeValueAsString(book);

		mockMvc.perform(post("/books")
						.contentType("application/json")
						.content(bookJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("New Book"))
				.andExpect(jsonPath("$.author").value("Author"));

		bookRepository.deleteAll();
	}

	@Test
	void testGetBookById() throws Exception {
		Book book = new Book("Specific Book", "Author", 2023);
		Book savedBook = bookRepository.save(book);

		mockMvc.perform(get("/books/" + savedBook.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Specific Book"));

		bookRepository.deleteAll();
	}

	@Test
	void testUpdateBook() throws Exception {
		Book book = new Book("Test Book", "Author", 2023);
		Book savedBook = bookRepository.save(book);

		Book updatedBook = new Book("Test Book 2", "Pablo", 2022);
		updatedBook.setId(savedBook.getId());
		String bookJson = objectMapper.writeValueAsString(updatedBook);

		mockMvc.perform(put("/books/" + savedBook.getId())
				.contentType("application/json")
				.content(bookJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Test Book 2"))
				.andExpect(jsonPath("$.author").value("Pablo"));

		bookRepository.deleteAll();
	}

	@Test
	void testDeleteBook() throws Exception {
		Book book = new Book("To Delete", "Author", 2023);
		Book savedBook = bookRepository.save(book);

		mockMvc.perform(delete("/books/" + savedBook.getId()))
				.andExpect(status().isOk());

		Optional<Book> deletedBook = bookRepository.findById(savedBook.getId());
		assert (deletedBook.isEmpty());
	}
}

