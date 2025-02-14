## Orders and Number Generate Services

Этот репозиторий содержит два микросервиса:

Number Generate Service — генерирует уникальные номера заказов.

Orders Service — управляет заказами и взаимодействует с Number Generate Service для получения номеров заказов.


## Описание задания
#### Number Generate Service
[ссылка на репозиторий](https://github.com/Sirrin-wq/number-generate-service)
- Генерирует уникальный номер заказа (5 символов) и текущую дату в формате YYYYMMDD.
- Предоставляет REST API для получения номера заказа.
- Использует MongoDB для хранения сгенерированных номеров.

#### Orders Service
[ссылка на репозиторий](https://github.com/Sirrin-wq/orders-service)
- Управляет заказами и предоставляет REST API для:
    - Создания заказа.
    - Получения заказа по его идентификатору. 
    - Получения заказов за заданную дату и больше заданной суммы. 
    - Получения списка заказов, не содержащих заданный товар и поступивших в заданный временной период.
- Использует PostgreSQL для хранения данных о заказах.
- Взаимодействует с Number Generate Service для получения номеров заказов.

## Технологии
- Java 17
- Spring Boot
- PostgreSQL
- MongoDB
- Docker
- Swagger

## Как склонировать и запустить приложение
1. Склонируйте репозиторий
```bash
   git clone https://github.com/Sirrin-wq/test-task-app.git
```
2. Запустите приложение с помощью Docker Compose
```bash
   docker-compose up --build
```

## Как проверить запросы через Swagger
#### Number Generate Service
Swagger UI доступен по адресу:
http://localhost:80/swagger-ui.html

#### Orders Service
Swagger UI доступен по адресу:
http://localhost:8080/swagger-ui.html

Пример json тела запроса для теста создания заказа(/orders)
```
{
  "recipient": "John Doe",
  "deliveryAddress": "123 Real St",
  "paymentType": "CARD",
  "deliveryType": "DOOR_DELIVERY",
  "items": [
    {
      "articleId": 1,
      "productName": "Laptop",
      "quantity": 1,
      "unitPrice":800.00
    }
  ]
}
```

Формат даты для GET запросов: `YYYY-MM-DD`