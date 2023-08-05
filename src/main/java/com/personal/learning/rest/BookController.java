package com.personal.learning.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

	private BookStorage bookStorage;

	public BookController(BookStorage bookStorage) {
		this.bookStorage = bookStorage;
	}

	@GetMapping
	public List<Book> getAllBooks() {
		return bookStorage.getAll();
	}

	@PostMapping
	public ResponseEntity<Book> addBook(@RequestBody Book book) throws URISyntaxException {
		Book createdBook = bookStorage.save(book);
		UriComponents uriComponents = UriComponentsBuilder.fromPath("/api/v1/books/" + "{id}")
				.buildAndExpand(createdBook.getId());
		URI locationHeaderUri = new URI(uriComponents.getPath());
		return ResponseEntity.created(locationHeaderUri).body(createdBook);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Book>> getSingleBook(@PathVariable Long id){
		if(id < 1) {
			throw new IllegalArgumentException();
		}
		Optional<Book> book = bookStorage.get(id);
		if(book.isEmpty())
			throw new NotFoundException();
		return ResponseEntity.ok().body(book);
	}
}
