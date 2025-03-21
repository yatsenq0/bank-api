package com.example.bank.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Тест: Успешный перевод денег между пользователями через API.
     */
    @Test
    public void testTransferMoney_Success() {
        // Act (выполнение запроса)
        String url = "/api/transfer?fromUserId=1&toUserId=2&amount=300";
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);

        // Assert (проверка результата)
        assertEquals(200, response.getStatusCodeValue()); // Проверяем статус ответа
        assertEquals("success", response.getBody().get("status")); // Проверяем тело ответа
    }

    /**
     * Тест: Перевод денег с недостаточным балансом через API.
     */
    @Test
    public void testTransferMoney_InsufficientFunds() {
        // Act (выполнение запроса)
        String url = "/api/transfer?fromUserId=1&toUserId=2&amount=99999";
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);

        // Assert (проверка результата)
        assertEquals(400, response.getStatusCodeValue()); // Проверяем статус ответа
        assertEquals("Недостаточно средств для перевода", response.getBody().get("error")); // Проверяем сообщение об ошибке
    }

    /**
     * Тест: Получение баланса пользователя через API.
     */
    @Test
    public void testGetBalance() {
        // Act (выполнение запроса)
        String url = "/api/balance/1";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        // Assert (проверка результата)
        assertEquals(200, response.getStatusCodeValue()); // Проверяем статус ответа
        assertNotNull(response.getBody().get("balance")); // Проверяем, что баланс существует
    }

    /**
     * Тест: Пополнение баланса пользователя через API.
     */
    @Test
    public void testDeposit() {
        // Act (выполнение запроса)
        String url = "/api/deposit?userId=1&amount=500";
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);

        // Assert (проверка результата)
        assertEquals(200, response.getStatusCodeValue()); // Проверяем статус ответа
        assertEquals("success", response.getBody().get("status")); // Проверяем тело ответа
    }

    /**
     * Тест: Снятие средств с баланса пользователя через API.
     */
    @Test
    public void testWithdraw_Success() {
        // Act (выполнение запроса)
        String url = "/api/withdraw?userId=1&amount=300";
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);

        // Assert (проверка результата)
        assertEquals(200, response.getStatusCodeValue()); // Проверяем статус ответа
        assertEquals("success", response.getBody().get("status")); // Проверяем тело ответа
    }

    /**
     * Тест: Снятие средств с недостаточным балансом через API.
     */
    @Test
    public void testWithdraw_InsufficientFunds() {
        // Act (выполнение запроса)
        String url = "/api/withdraw?userId=1&amount=99999";
        ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);

        // Assert (проверка результата)
        assertEquals(400, response.getStatusCodeValue()); // Проверяем статус ответа
        assertEquals("Недостаточно средств", response.getBody().get("error")); // Проверяем сообщение об ошибке
    }
}