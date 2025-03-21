package com.example.bank.repository;

import com.example.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Поиск всех операций пользователя за определенный период времени.
     *
     * @param userId ID пользователя
     * @param startDate Начало периода
     * @param endDate Конец периода
     * @return Список операций
     */
    List<Transaction> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}