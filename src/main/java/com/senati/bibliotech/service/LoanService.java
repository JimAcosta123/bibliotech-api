package com.senati.bibliotech.service;

import com.senati.bibliotech.exception.BadRequestException;
import com.senati.bibliotech.exception.ResourceNotFoundException;
import com.senati.bibliotech.model.Book;
import com.senati.bibliotech.model.Loan;
import com.senati.bibliotech.model.LoanStatus;
import com.senati.bibliotech.model.User;
import com.senati.bibliotech.repository.LoanRepository;
import com.senati.bibliotech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private static final int LOAN_DAYS = 14;

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookService bookService;

    public List<Loan> findMyLoans(String email) {
        User user = getUser(email);
        return loanRepository.findByUser(user);
    }

    public Loan createLoan(String email, Long bookId) {
        User user = getUser(email);
        Book book = bookService.findById(bookId);

        bookService.decrementAvailability(book);

        Loan loan = Loan.builder()
                .book(book)
                .user(user)
                .loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(LOAN_DAYS))
                .status(LoanStatus.ACTIVO)
                .build();

        return loanRepository.save(loan);
    }

    public Loan returnLoan(String email, Long loanId) {
        User user = getUser(email);
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestamo no encontrado con id " + loanId));

        if (!loan.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Este prestamo no pertenece al usuario autenticado");
        }
        if (loan.getStatus() == LoanStatus.DEVUELTO) {
            throw new BadRequestException("Este prestamo ya fue devuelto");
        }

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.DEVUELTO);
        bookService.incrementAvailability(loan.getBook());

        return loanRepository.save(loan);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }
}
