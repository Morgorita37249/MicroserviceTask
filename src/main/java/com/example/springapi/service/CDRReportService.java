package com.example.springapi.service;

import com.example.springapi.api.model.CDR;
import com.example.springapi.api.repos.CDRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Сервис для генерации отчетов о звонках (CDR) для абонента в заданном диапазоне дат.
 * Этот сервис генерирует отчет в формате CSV, который содержит информацию о звонках для заданного номера телефона.
 */
@Service
public class CDRReportService {

    private static final Logger logger = LoggerFactory.getLogger(CDRReportService.class);

    @Autowired
    private CDRRepository cdrRepository;
    /**
     * Генерирует отчет о звонках (CDR) в формате CSV для заданного абонента и диапазона дат.
     * Отчет сохраняется в директории "reports" с уникальным именем файла, включающим номер телефона абонента и уникальный ID запроса.
     *
     * @param phoneNumber номер телефона абонента, для которого генерируется отчет.
     * @param startDate начальная дата и время диапазона, для которого генерируется отчет.
     * @param endDate конечная дата и время диапазона, для которого генерируется отчет.
     * @param requestId уникальный идентификатор запроса для создания уникального имени файла.
     * @return {@code true}, если отчет был успешно сгенерирован, {@code false} в случае отсутствия данных.
     * @throws IOException если произошла ошибка при записи в файл отчета.
     */
    public boolean generateReport(String phoneNumber, LocalDateTime startDate, LocalDateTime endDate, String requestId) throws IOException {

        try {
            List<CDR> cdrList = cdrRepository.findByNumberAndStartTimeBetween(phoneNumber, startDate, endDate);
            if (cdrList.isEmpty()) {
                logger.error("Нет данных для номера: {} в диапазоне дат: {} - {}", phoneNumber, startDate, endDate);
                return false;
            }

            String fileName = phoneNumber + "_" + requestId + ".csv";
            String filePath = "reports/" + fileName;

            File directory = new File("reports");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File reportFile = new File(filePath);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile))) {

                writer.write("Call Type,Number,Called Number,Start Time,End Time\n");
                for (CDR cdr : cdrList) {
                    writer.write(cdr.getCallType() + "," +
                            cdr.getNumber() + "," +
                            cdr.getCalledNumber() + "," +
                            cdr.getStartTime() + "," +
                            cdr.getEndTime() + "\n");
                }
            }

            return true;

        } catch (IOException e) {
            logger.error("Ошибка при записи в файл отчета: ", e);
            throw e;
        } catch (Exception e) {
            logger.error("Ошибка при генерации отчета: ", e);
            throw new RuntimeException("Ошибка при генерации отчета: " + e.getMessage(), e);
        }
    }
}
