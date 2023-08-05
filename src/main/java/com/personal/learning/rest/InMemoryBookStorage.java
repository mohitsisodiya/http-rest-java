package com.personal.learning.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class InMemoryBookStorage implements BookStorage {
	
	private final Map<Long, Book> books = new HashMap<>();
 
	@Override
	public Book save(Book book) {
		boolean isBookIdEmptyOrNonExisting = book.getId() == null || !books.containsKey(book.getId());
		
		if(isBookIdEmptyOrNonExisting) {
			Long id = books.size() + 1L;
			book.setId(id);
		}
		
		books.put(book.getId(), book);
		return book;
	}

	@Override
	public Optional<Book> get(Long id) {
		return Optional.ofNullable(books.get(id));
	}

	@Override
	public List<Book> getAll() {
		ArrayList<Book> result = new ArrayList<>(books.values());
		return result;
	}

	@Override
	public void delete(Long id) {
		books.remove(id);
	}

	@Override
	public void deleteAll() {
		books.clear();
	}

}
