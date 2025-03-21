package com.example.bank.service;

import com.example.bank.model.User;
import com.example.bank.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    /**
     * Тест: Успешный перевод денег между пользователями.
     */
    @Test
    public void testTransferMoney_Success() {
        // Arrange (подготовка данных)
        User sender = new User();
        sender.setUserId(1L);
        sender.setBalance(new BigDecimal("1000"));

        User receiver = new User();
        receiver.setUserId(2L);
        receiver.setBalance(new BigDecimal("500"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        // Act (выполнение действия)
        userService.transferMoney(1L, 2L, new BigDecimal("300"));

        // Assert (проверка результата)
        verify(userRepository, times(1)).save(sender); // Проверяем, что отправитель сохранен
        verify(userRepository, times(1)).save(receiver); // Проверяем, что получатель сохранен

        assertEquals(new BigDecimal("700"), sender.getBalance()); // Баланс отправителя уменьшился на 300
        assertEquals(new BigDecimal("800"), receiver.getBalance()); // Баланс получателя увеличился на 300
    }

    /**
     * Тест: Перевод денег, когда у отправителя недостаточно средств.
     */
    @Test
    public void testTransferMoney_InsufficientFunds() {
        // Arrange (подготовка данных)
        User sender = new User();
        sender.setUserId(1L);
        sender.setBalance(new BigDecimal("100"));

        User receiver = new User();
        receiver.setUserId(2L);
        receiver.setBalance(new BigDecimal("500"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        // Act & Assert (проверяем исключение)
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.transferMoney(1L, 2L, new BigDecimal("300"));
        });

        assertEquals("Недостаточно средств для перевода", exception.getMessage());
        verify(userRepository, never()).save(any(User.class)); // Данные не должны сохраняться
    }

    /**
     * Тест: Пополнение баланса пользователя.
     */
    @Test
    public void testDeposit() {
        // Arrange (подготовка данных)
        User user = new User();
        user.setUserId(1L);
        user.setBalance(new BigDecimal("500"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act (выполнение действия)
        userService.deposit(1L, new BigDecimal("300"));

        // Assert (проверка результата)
        verify(userRepository, times(1)).save(user); // Проверяем, что пользователь сохранен
        assertEquals(new BigDecimal("800"), user.getBalance()); // Баланс увеличился на 300
    }

    /**
     * Тест: Снятие средств с баланса пользователя.
     */
    @Test
    public void testWithdraw_Success() {
        // Arrange (подготовка данных)
        User user = new User();
        user.setUserId(1L);
        user.setBalance(new BigDecimal("1000"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act (выполнение действия)
        userService.withdraw(1L, new BigDecimal("300"));

        // Assert (проверка результата)
        verify(userRepository, times(1)).save(user); // Проверяем, что пользователь сохранен
        assertEquals(new BigDecimal("700"), user.getBalance()); // Баланс уменьшился на 300
    }

    /**
     * Тест: Снятие средств, когда недостаточно средств на счету.
     */
    @Test
    public void testWithdraw_InsufficientFunds() {
        // Arrange (подготовка данных)
        User user = new User();
        user.setUserId(1L);
        user.setBalance(new BigDecimal("100"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert (проверяем исключение)
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.withdraw(1L, new BigDecimal("300"));
        });

        assertEquals("Недостаточно средств", exception.getMessage());
        verify(userRepository, never()).save(any(User.class)); // Данные не должны сохраняться
    }
}