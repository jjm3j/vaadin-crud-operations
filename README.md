# Vaadin CRUD operations

Демонстрация тестового задания. Реализовано:
* две таблицы PostgreSQL с связями one-to-many
* CRUD операции для таблиц
* регистрация и авторизация пользователей
* смена тёмной/светлой темы
* ~~личный кабинет~~

## Запуск приложения

Репозиторий является стандартным Maven проектом. Для запуска из командной строки, введите mvnw (Windows) или ./mvnw (Mac & Linux) в командной строке, затем откройте http://localhost:8080 в браузере.

Также вы можете запустить проект непосредственно из IDE/

## Структура проекта

- `MainLayout.java` в `src/main/java/com/jjmj/application/views` содержит настройки навигации (т. е.
  боковая/верхняя панель и главное меню). Эта установка использует
  [App Layout](https://vaadin.com/docs/components/app-layout).
- `views` модуль в `src/main/java/com/jjmj/application` содержит серверные Java-представления приложения.
- `data` модуль в `src/main/java/com/jjmj/application` содержит файлы описания сущностей, репозитории и сервисы для работы с базой данных.
- `security` модуль в `src/main/java/com/jjmj/application/` содержит файлы Spring Boot Security для реализации авторизации и разделения прав доступа.
- `themes` папка in `frontend/` содержит кастомные CSS стили.


