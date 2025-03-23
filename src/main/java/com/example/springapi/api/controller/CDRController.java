package com.example.springapi.api.controller;

import com.example.springapi.service.CDRReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Контроллер для обработки запросов, связанных с генерацией отчетов CDR.
 * Обеспечивает создание отчетов о звонках для заданных номеров телефонов в определенный период времени.
 */
@RestController
@RequestMapping("/api/cdr")
public class CDRController {

    @Autowired
    private CDRReportService cdrReportService;

    /**
     * Обрабатывает POST-запрос для генерации отчета CDR для указанного номера телефона
     * и временного интервала.
     *
     * @param phoneNumber номер телефона для которого генерируется отчет.
     * @param startDate строковое представление даты начала отчетного периода в формате 'yyyy-MM-ddTHH:mm:ss'.
     * @param endDate строковое представление даты окончания отчетного периода в формате 'yyyy-MM-ddTHH:mm:ss'.
     * @return ResponseEntity с информацией о статусе генерации отчета.
     */
    @PostMapping("/generateReport")
    public ResponseEntity<String> generateCdrReport(
            @RequestParam String phoneNumber,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        String requestId = UUID.randomUUID().toString();

        LocalDateTime start;
        LocalDateTime end;
        try {
            start = LocalDateTime.parse(startDate);
            end = LocalDateTime.parse(endDate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Неверный формат дат. Пожалуйста, используйте формат 'yyyy-MM-ddTHH:mm:ss'.");
        }

        try {
            boolean isReportGenerated = cdrReportService.generateReport(phoneNumber, start, end, requestId);

            if (isReportGenerated) {
                return ResponseEntity.ok("Отчет генерируется. ID запроса: " + requestId);
            } else {
                return ResponseEntity.status(500).body("Произошла ошибка при генерации отчета.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при создании файла отчета: " + e.getMessage());
        }
    }
}

