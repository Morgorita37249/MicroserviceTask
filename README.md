# CDR/UDR Микросервис на Spring Boot
## Описание проекта
Этот микросервис эмулирует работу коммутатора мобильного оператора, генерирует CDR-записи (Call Data Record), агрегирует их в UDR-отчеты (Usage Data Report) и предоставляет REST API для работы с ними.

## Основные возможности:
Генерация CDR-записей и их сохранение в H2 Database.
Получение UDR-отчетов по абонентам за период.
Генерация и экспорт CDR-отчета в CSV.
REST API для взаимодействия с данными.

## Запуск проекта:
mvn spring-boot:run
### Приложение будет доступно по адресу:
http://localhost:8080

## База данных:
Используется H2 Database (in-memory).
Консоль доступна по адресу: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb

## REST API Эндпоинты
### CDR Контроллер (Call Data Record)
1. Генерация CDR-отчета по абоненту за период
URL: POST /api/cdr/generateReport
Описание: Генерирует CDR-отчет по указанному абоненту за определенный временной интервал.
Параметры запроса:
phoneNumber (String) – номер телефона абонента
startDate (String) – начало периода (yyyy-MM-ddTHH:mm:ss)
endDate (String) – конец периода (yyyy-MM-ddTHH:mm:ss)

### Пример запроса:
POST "http://localhost:8080/api/cdr/generateReport?phoneNumber=79991112233&startDate=2024-03-01T00:00:00&endDate=2024-03-31T23:59:59"
Пример ответа (успешно):
{
    "message": "Отчет генерируется. ID запроса: 123e4567-e89b-12d3-a456-426614174000"
}
Пример ответа (ошибка формата даты):
{
    "error": "Неверный формат дат. Пожалуйста, используйте формат 'yyyy-MM-ddTHH:mm:ss'."
}

### UDR Контроллер (Usage Data Report)
2. Получение UDR-отчета за месяц
URL: GET /api/udr/byMonth/{msisdn}
Описание: Возвращает суммарную длительность входящих и исходящих звонков абонента за определенный месяц.
Параметры запроса:
msisdn (String) – номер телефона абонента
year (int) – год
month (int) – месяц
### Пример запроса:
GET "http://localhost:8080/api/udr/byMonth/79991112233?year=2024&month=3"
Пример ответа:
Редактировать
{
    "msisdn": "79991112233",
    "incomingCall": { 
        "totalTime": "01:20:45"
    },
    "outcomingCall": { 
        "totalTime": "00:45:30"
    }
}
3. Получение UDR-отчета за произвольный период
URL: GET /api/udr/byPeriod/{msisdn}
Описание: Возвращает суммарную длительность входящих и исходящих звонков абонента за заданный период.
Параметры запроса:
msisdn (String) – номер телефона абонента
startDate (String) – начало периода (yyyy-MM-ddTHH:mm:ss)
endDate (String) – конец периода (yyyy-MM-ddTHH:mm:ss)
### Пример запроса:
GET "http://localhost:8080/api/udr/byPeriod/79991112233?startDate=2024-03-01T00:00:00&endDate=2024-03-15T23:59:59"
Пример ответа:
{
    "msisdn": "79991112233",
    "incomingCall": {
        "totalTime": "00:35:20"
    },
    "outcomingCall": { 
        "totalTime": "01:12:15"
    }
}

## Архитектура проекта:
 com.example.springapi
├──  api.controller → REST-контроллеры
│   ├── CDRController.java → Обработка CDR-отчетов
│   ├── UDRController.java → Обработка UDR-отчетов
│
├──  api.model → Модели данных
│   ├── CDR.java → Модель CDR-записи
│   ├── UDR.java → Модель UDR-отчета
│   ├── DataLoader.java → Загрузчик данных в БД
│   ├── User.java → Модель абонента
├──  api.repos → Репозитории
│   ├── CDRRepository.java → Запросы к CDR в БД
|   |── SubscriberRepository.java → Запросы к User в БД
│
├──  service → Логика приложения
│   ├── CDRGeneratorService.java → Генерация CDR
│   ├── CDRReportService.java → Создание отчетов
│   ├── UDRService.java → Формирование UDR
│
└──  SpringApiApplication.java → Запуск приложения
Сгенерированные CDR-отчеты хранятся в папке /reports.
