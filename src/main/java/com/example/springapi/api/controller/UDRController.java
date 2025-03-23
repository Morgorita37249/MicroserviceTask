package com.example.springapi.api.controller;

import com.example.springapi.service.UDRService;
import com.example.springapi.api.model.UDR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Контроллер для обработки запросов, связанных с User Data Records.
 * Обеспечивает получение данных о продолжительности входящих и исходящих звонков
 * для абонента за заданный период или месяц.
 */
@RestController
@RequestMapping("/api/udr")
public class UDRController {

    @Autowired
    private UDRService udrService;

    /**
     * Обрабатывает GET-запрос для получения данных UDR (User Data Record) по абоненту
     * за определенный месяц.
     *
     * @param msisdn номер телефона абонента, для которого генерируется отчет.
     * @param year год отчетного периода.
     * @param month месяц отчетного периода.
     * @return объект UDR с данными о входящих и исходящих звонках для абонента.
     */
    @GetMapping("/byMonth/{msisdn}")
    public UDR getUDRForMonth(
            @PathVariable String msisdn,
            @RequestParam int year,
            @RequestParam int month) {
        return udrService.getUDRForSubscriberByMonth(msisdn, year, month);
    }

    /**
     * Обрабатывает GET-запрос для получения данных UDR по абоненту
     * за заданный период времени.
     *
     * @param msisdn номер телефона абонента, для которого генерируется отчет.
     * @param startDate строковое представление даты начала отчетного периода в формате 'yyyy-MM-ddTHH:mm:ss'.
     * @param endDate строковое представление даты окончания отчетного периода в формате 'yyyy-MM-ddTHH:mm:ss'.
     * @return объект UDR с данными о входящих и исходящих звонках для абонента.
     */
    @GetMapping("/byPeriod/{msisdn}")
    public UDR getUDRForPeriod(
            @PathVariable String msisdn,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        LocalDateTime start = LocalDateTime.parse(startDate.trim());
        LocalDateTime end = LocalDateTime.parse(endDate.trim());

        return udrService.getUDRForSubscriberForPeriod(msisdn, start, end);
    }
}


