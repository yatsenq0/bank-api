package com.example.bank.controller;

import com.example.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping(api)
public class BankController {
    @Autowired
    private UserService userService;

    @GetMapping(balance{userId})
    public ResponseEntity getBalance(@PathVariable Long userId) {
        try {
            BigDecimal balance = userService.getBalance(userId);
            return ResponseEntity.ok(Map.of(balance, balance));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(error, e.getMessage()));
        }
    }

    @PostMapping(deposit)
    public ResponseEntity deposit(
            @RequestParam Long userId,
            @RequestParam BigDecimal amount) {
        try {
            userService.deposit(userId, amount);
            return ResponseEntity.ok(Map.of(status, success));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(error, e.getMessage()));
        }
    }

    @PostMapping(withdraw)
    public ResponseEntity withdraw(
            @RequestParam Long userId,
            @RequestParam BigDecimal amount) {
        try {
            userService.withdraw(userId, amount);
            return ResponseEntity.ok(Map.of(status, success));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(error, e.getMessage()));
        }
    }
}