# Bank API

REST API для управления балансом пользователей и переводов.

---

## **Технологии**
- Spring Boot 3.1.4
- PostgreSQL
- JPA/Hibernate

---

## **Функционал**
- Просмотр баланса пользователя.
- Пополнение счета.
- Снятие средств.
- **Перевод денег между пользователями.**
- **История операций (сохранение всех действий в таблицу `transactions`).**

---

## **Запуск**
1. Установите PostgreSQL и создайте базу `bank_db`.
2. Запустите `BankApplication.java`.
3. Используйте Postman или curl для тестирования API:
   - **Проверка баланса:**
     ```
     GET http://localhost:8080/api/balance/1
     ```
   - **Пополнение счета:**
     ```
     POST http://localhost:8080/api/deposit?userId=1&amount=500
     ```
   - **Снятие средств:**
     ```
     POST http://localhost:8080/api/withdraw?userId=1&amount=200
     ```
   - **Перевод денег:**
     ```
     POST http://localhost:8080/api/transfer?fromUserId=1&toUserId=2&amount=300
     ```

---

## **Структура базы данных**
- **Таблица `users`:**
  - `user_id` (ID пользователя),
  - `balance` (баланс).
- **Таблица `transactions`:**
  - `transaction_id` (ID операции),
  - `user_id` (ID пользователя),
  - `type` (тип операции: DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT),
  - `amount` (сумма операции),
  - `created_at` (время создания операции).

---

## **Тестирование**
- Юнит-тесты для проверки бизнес-логики (`UserServiceTest`).
- Интеграционные тесты для проверки REST API (`BankControllerIntegrationTest`).

---

## **Дополнительно**
- Файл `data.sql` содержит начальные данные для БД.
- SQL-дамп базы данных доступен в файле `dump.sql`.

---

## **Пример данных**
- Начальные данные в таблице `users`:
  ```sql
  INSERT INTO users (user_id, balance) VALUES (1, 1000.00);
  INSERT INTO users (user_id, balance) VALUES (2, 500.00);
