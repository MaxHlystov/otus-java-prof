### Реактивное Веб-приложение на Spring Boot

#### Цель:
Нучиться создавать реактивное CRUD-приложения на Spring Boot

#### Описание/Пошаговая инструкция выполнения домашнего задания:

- Взять за основу ДЗ "Веб-приложение на Spring Boot".
- Приложение разделить на два сервиса:
  1. сервис для работы с базой данных (1)
  2. сервис для работы с web-клиентом (2)
- Для взаимодействия сервисов надо использовать webflux.
- Реактивный клиент на стороне сервиса (2) и реактивный контроллер на стороне сервиса (1).
- В сервисе (1) сделайте обращение к базе данных на отдельном пуле потоков.
