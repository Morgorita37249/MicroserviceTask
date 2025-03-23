package com.example.springapi.service;

import com.example.springapi.api.model.CDR;
import com.example.springapi.api.repos.CDRRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CDRReportServiceTest {

    @Mock
    private CDRRepository cdrRepository;

    @InjectMocks
    private CDRReportService cdrReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateReport_Success() throws IOException {
        String phoneNumber = "79991112233";
        LocalDateTime startDate = LocalDateTime.parse("2024-03-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2024-03-31T23:59:59");
        String requestId = "123e4567-e89b-12d3-a456-426614174000";

        CDR cdr1 = new CDR("01", "79991112233", "79991112234", LocalDateTime.parse("2024-03-01T01:00:00"), LocalDateTime.parse("2024-03-01T01:05:00"));
        CDR cdr2 = new CDR("02", "79991112233", "79991112235", LocalDateTime.parse("2024-03-02T02:00:00"), LocalDateTime.parse("2024-03-02T02:10:00"));
        List<CDR> cdrList = Arrays.asList(cdr1, cdr2);

        when(cdrRepository.findByNumberAndStartTimeBetween(phoneNumber, startDate, endDate)).thenReturn(cdrList);
        boolean result = cdrReportService.generateReport(phoneNumber, startDate, endDate, requestId);

        assertTrue(result);
        File reportFile = new File("reports/" + phoneNumber + "_" + requestId + ".csv");
        assertTrue(reportFile.exists());

        reportFile.delete();
    }

    @Test
    void generateReport_NoData() throws IOException {
        String phoneNumber = "79991112233";
        LocalDateTime startDate = LocalDateTime.parse("2024-03-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2024-03-31T23:59:59");
        String requestId = "123e4567-e89b-12d3-a456-426614174000";

        when(cdrRepository.findByNumberAndStartTimeBetween(phoneNumber, startDate, endDate)).thenReturn(List.of());

        boolean result = cdrReportService.generateReport(phoneNumber, startDate, endDate, requestId);

        assertFalse(result);
    }

    @Test
    void generateReport_ExceptionHandling() throws IOException {
        String phoneNumber = "79991112233";
        LocalDateTime startDate = LocalDateTime.parse("2024-03-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2024-03-31T23:59:59");
        String requestId = "123e4567-e89b-12d3-a456-426614174000";

        when(cdrRepository.findByNumberAndStartTimeBetween(phoneNumber, startDate, endDate))
                .thenThrow(new RuntimeException("Ошибка при доступе к базе данных"));

        assertThrows(RuntimeException.class, () -> {
            cdrReportService.generateReport(phoneNumber, startDate, endDate, requestId);
        });
    }
}
