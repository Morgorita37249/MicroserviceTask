package com.example.springapi.service;

import com.example.springapi.api.model.CDR;
import com.example.springapi.api.model.UDR;
import com.example.springapi.api.repos.CDRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


/**
 * Сервис для вычисления UDR (Usage Data Records) для абонентов на основе данных о звонках (CDR).
 * Этот сервис предоставляет методы для получения данных о звонках для абонента в заданный месяц или период времени.
 */
@Service
public class UDRService {

    @Autowired
    private CDRRepository cdrRepository;
    /**
     * Получает UDR для абонента по номеру телефона для заданного месяца и года.
     * Этот метод находит все звонки абонента в пределах заданного месяца и генерирует отчет о его использовании.
     *
     * @param msisdn номер телефона абонента, для которого генерируется отчет.
     * @param year год для выбора отчетного месяца.
     * @param month месяц для выбора отчетного периода.
     * @return объект UDR с информацией о входящих и исходящих звонках абонента.
     */
    public UDR getUDRForSubscriberByMonth(String msisdn, int year, int month) {
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1);

        List<CDR> cdrList = cdrRepository.findByNumberAndStartTimeBetween(msisdn, startDate, endDate);
        return generateUDR(msisdn, cdrList);
    }
    /**
     * Получает UDR для абонента по номеру телефона за заданный период времени.
     * Этот метод генерирует отчет о звонках абонента в произвольном временном диапазоне.
     *
     * @param msisdn номер телефона абонента, для которого генерируется отчет.
     * @param startDate начало отчетного периода.
     * @param endDate конец отчетного периода.
     * @return объект UDR с информацией о входящих и исходящих звонках абонента.
     */
    public UDR getUDRForSubscriberForPeriod(String msisdn, LocalDateTime startDate, LocalDateTime endDate) {
        List<CDR> cdrList = cdrRepository.findByNumberAndStartTimeBetween(msisdn, startDate, endDate);

        return generateUDR(msisdn, cdrList);
    }
    /**
     * Генерирует объект UDR для абонента, анализируя список звонков (CDR).
     * Этот метод вычисляет общее время для входящих и исходящих звонков и возвращает результат в объекте UDR.
     *
     * @param msisdn номер телефона абонента, для которого генерируется отчет.
     * @param cdrList список звонков, полученных из базы данных.
     * @return объект UDR с рассчитанными длительностями входящих и исходящих звонков.
     */
    private UDR generateUDR(String msisdn, List<CDR> cdrList) {
        Duration incomingDuration = Duration.ZERO;
        Duration outgoingDuration = Duration.ZERO;

        for (CDR cdr : cdrList) {
            Duration callDuration = Duration.between(cdr.getStartTime(), cdr.getEndTime());

            if ("01".equals(cdr.getCallType())) {
                outgoingDuration = outgoingDuration.plus(callDuration);
            } else if ("02".equals(cdr.getCallType())) {
                incomingDuration = incomingDuration.plus(callDuration);
            }
        }

        UDR udr = new UDR(msisdn);
        udr.getIncomingCall().addDuration(incomingDuration);
        udr.getOutcomingCall().addDuration(outgoingDuration);

        return udr;
    }
}



