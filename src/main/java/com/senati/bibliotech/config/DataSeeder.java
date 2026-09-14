package com.senati.bibliotech.config;

import com.senati.bibliotech.model.Book;
import com.senati.bibliotech.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Override
    public void run(String... args) {
        if (bookRepository.count() > 0) return;

        bookRepository.save(Book.builder().title("Clean Code").author("Robert C. Martin")
                .isbn("9780132350884").genre("Ingenieria de software").totalCopies(3).availableCopies(3).build());
        bookRepository.save(Book.builder().title("Cien anios de soledad").author("Gabriel Garcia Marquez")
                .isbn("9780307474728").genre("Novela").totalCopies(2).availableCopies(2).build());
        bookRepository.save(Book.builder().title("Introduction to Algorithms").author("Cormen, Leiserson, Rivest, Stein")
                .isbn("9780262033848").genre("Algoritmos").totalCopies(2).availableCopies(2).build());
    }
}
