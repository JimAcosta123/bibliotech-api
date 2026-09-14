package com.senati.bibliotech.service;

import com.senati.bibliotech.dto.BookRequest;
import com.senati.bibliotech.exception.BadRequestException;
import com.senati.bibliotech.exception.ResourceNotFoundException;
import com.senati.bibliotech.model.Book;
import com.senati.bibliotech.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> findAll(String search) {
        if (search == null || search.isBlank()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(search, search);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con id " + id));
    }

    public Book create(BookRequest request) {
        int copies = request.getTotalCopies() == null ? 1 : request.getTotalCopies();
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .genre(request.getGenre())
                .totalCopies(copies)
                .availableCopies(copies)
                .build();
        return bookRepository.save(book);
    }

    public Book update(Long id, BookRequest request) {
        Book book = findById(id);
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setGenre(request.getGenre());
        if (request.getTotalCopies() != null) {
            int diff = request.getTotalCopies() - book.getTotalCopies();
            book.setTotalCopies(request.getTotalCopies());
            book.setAvailableCopies(Math.max(0, book.getAvailableCopies() + diff));
        }
        return bookRepository.save(book);
    }

    public void delete(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
    }

    public void decrementAvailability(Book book) {
        if (book.getAvailableCopies() <= 0) {
            throw new BadRequestException("No hay copias disponibles de este libro");
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
    }

    public void incrementAvailability(Book book) {
        book.setAvailableCopies(Math.min(book.getTotalCopies(), book.getAvailableCopies() + 1));
        bookRepository.save(book);
    }
}
