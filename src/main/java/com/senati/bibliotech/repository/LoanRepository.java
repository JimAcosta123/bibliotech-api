package com.senati.bibliotech.repository;

import com.senati.bibliotech.model.Loan;
import com.senati.bibliotech.model.LoanStatus;
import com.senati.bibliotech.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUser(User user);
    List<Loan> findByUserAndStatus(User user, LoanStatus status);
}
