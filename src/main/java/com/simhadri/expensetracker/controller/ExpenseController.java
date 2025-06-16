package com.simhadri.expensetracker.controller;

import com.simhadri.expensetracker.model.Expense;
import com.simhadri.expensetracker.model.User;
import com.simhadri.expensetracker.service.ExpenseService;
//import com.simhadri.expensetracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
//    private final UserService userService;

    // ✅ Get all expenses for current logged-in user
    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<Expense> expenses = expenseService.getExpensesByUserEmail(email);
        return ResponseEntity.ok(expenses);
    }

    // ✅ Add an expense for logged-in user
    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Expense savedExpense = expenseService.addExpenseForUser(expense, email);
        return ResponseEntity.ok(savedExpense);
    }

    // ✅ Delete an expense by ID (only if it belongs to logged-in user)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        expenseService.deleteExpenseIfOwner(id, email);
        return ResponseEntity.noContent().build();
    }
}
