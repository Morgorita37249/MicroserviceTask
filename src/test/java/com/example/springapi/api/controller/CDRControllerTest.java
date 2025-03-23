package com.example.springapi.api.controller;

import com.example.springapi.service.CDRReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class CDRControllerTest {

    @Mock
    private CDRReportService cdrReportService;

    @InjectMocks
    private CDRController cdrController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cdrController).build();
    }

    @Test
    void testGenerateCdrReport_Failure() throws Exception {
        String phoneNumber = "79991112233";
        String requestId = UUID.randomUUID().toString();

        when(cdrReportService.generateReport(eq(phoneNumber), any(LocalDateTime.class), any(LocalDateTime.class), eq(requestId)))
                .thenReturn(false);

    }

    @Test
    void testGenerateCdrReport_ExceptionHandling() throws Exception {
        String phoneNumber = "79991112233";
        String requestId = UUID.randomUUID().toString();

        when(cdrReportService.generateReport(eq(phoneNumber), any(LocalDateTime.class), any(LocalDateTime.class), eq(requestId)))
                .thenThrow(new RuntimeException("Ошибка при генерации отчета"));
    }
}
