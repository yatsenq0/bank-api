package com.example.bank.service;

import com.example.bank.model.Transaction;
import com.example.bank.model.User;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Получение баланса пользователя.
     */
    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return user.getBalance();
    }

    /**
     * Пополнение баланса пользователя.
     */
    @Transactional
    public void deposit(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        // Сохраняем операцию в историю
        saveTransaction(userId, "DEPOSIT", amount);
    }

    /**
     * Снятие средств с баланса пользователя.
     */
    @Transactional
    public void withdraw(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (user.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Недостаточно средств");
        }

        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);

        // Сохраняем операцию в историю
        saveTransaction(userId, "WITHDRAW", amount);
    }

    /**
     * Перевод денег между пользователями.
     */
    @Transactional
    public void transferMoney(Long fromUserId, Long toUserId, BigDecimal amount) {
        // Проверяем отправителя
        User sender = userRepository.findById(fromUserId)
                .orElseThrow(() -> new RuntimeException("Отправитель не найден"));

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Недостаточно средств для перевода");
        }

        // Проверяем получателя
        User receiver = userRepository.findById(toUserId)
                .orElseThrow(() -> new RuntimeException("Получатель не найден"));

        // Обновляем балансы
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        userRepository.save(sender);
        userRepository.save(receiver);

        // Сохраняем операции в историю
        saveTransaction(fromUserId, "TRANSFER_OUT", amount);
        saveTransaction(toUserId, "TRANSFER_IN", amount);
    }

    /**
     * Сохранение операции в таблицу транзакций.
     */
    @Transactional
    private void saveTransaction(Long userId, String type, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);
    }
}