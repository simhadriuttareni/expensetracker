package com.simhadri.expensetracker.service;
import com.simhadri.expensetracker.repository.ExpenseRepository;

import com.simhadri.expensetracker.model.Expense;
import com.simhadri.expensetracker.model.User;
import com.simhadri.expensetracker.repository.ExpenseRepository;
import com.simhadri.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    // ✅ Add expense and attach the user based on email
    public Expense addExpenseForUser(Expense expense, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        expense.setUser(user);
        return expenseRepository.save(expense);
    }

    // ✅ Get all expenses for the logged-in user
    public List<Expense> getExpensesByUserEmail(String email) {
        return expenseRepository.findByUserEmail(email);
    }

    // ✅ Delete only if it belongs to the logged-in user
    public void deleteExpenseIfOwner(Long expenseId, String email) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to delete this expense");
        }

        expenseRepository.delete(expense);
    }
}
