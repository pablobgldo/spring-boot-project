package com.example.spring_boot_project.services;

import com.example.spring_boot_project.entities.Book;
import com.example.spring_boot_project.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public Book updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setYearPublished(updatedBook.getYearPublished());
            return bookRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
