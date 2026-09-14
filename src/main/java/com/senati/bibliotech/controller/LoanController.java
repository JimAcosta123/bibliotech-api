package com.senati.bibliotech.controller;

import com.senati.bibliotech.model.Loan;
import com.senati.bibliotech.service.LoanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Tag(name = "Prestamos")
public class LoanController {

    private final LoanService loanService;

    @GetMapping("/mine")
    public List<Loan> myLoans(Authentication authentication) {
        return loanService.findMyLoans(authentication.getName());
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<Loan> createLoan(@PathVariable Long bookId, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanService.createLoan(authentication.getName(), bookId));
    }

    @PutMapping("/{loanId}/return")
    public Loan returnLoan(@PathVariable Long loanId, Authentication authentication) {
        return loanService.returnLoan(authentication.getName(), loanId);
    }
}
