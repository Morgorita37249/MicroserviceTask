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

@Service
public class CDRReportService {

    private static final Logger logger = LoggerFactory.getLogger(CDRReportService.class);

    @Autowired
    private CDRRepository cdrRepository;

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
