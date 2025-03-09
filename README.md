# Bank API
REST API для управления балансом пользователей

## Технологии
- Spring Boot 3.1.4
- PostgreSQL
- JPAHibernate

## Запуск
1. Установите PostgreSQL и создайте базу `bank_db`
2. Запустите `BankApplication.java`
3. Используйте Postman для тестирования API
   - `GET httplocalhost8080apibalance1`
   - `POST httplocalhost8080apideposituserId=1&amount=500`
   - `POST httplocalhost8080apiwithdrawuserId=1&amount=200`
