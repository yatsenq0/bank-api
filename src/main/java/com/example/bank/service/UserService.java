package com.example.bank.service;

import com.example.bank.model.User;
import com.example.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public BigDecimal getBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() - new RuntimeException(User not found));
        return user.getBalance();
    }

    @Transactional
    public void deposit(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() - new RuntimeException(User not found));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }

    @Transactional
    public void withdraw(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() - new RuntimeException(User not found));
        if (user.getBalance().compareTo(amount)  0) {
            throw new RuntimeException(Недостаточно средств);
        }
        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);
    }
}