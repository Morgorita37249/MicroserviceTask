package com.example.springapi.api.controller;

import com.example.springapi.service.UDRService;
import com.example.springapi.api.model.UDR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class UDRControllerTest {

    @Mock
    private UDRService udrService;

    @InjectMocks
    private UDRController udrController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(udrController).build();
    }

    @Test
    void testGetUDRForMonth_Success() throws Exception {
        String msisdn = "79991112233";
        int year = 2025;
        int month = 3;

        UDR udr = new UDR(msisdn);
        udr.getIncomingCall().addDuration(Duration.ofMinutes(30));
        udr.getOutcomingCall().addDuration(Duration.ofMinutes(20));

        when(udrService.getUDRForSubscriberByMonth(msisdn, year, month)).thenReturn(udr);

        mockMvc.perform(get("/api/udr/byMonth/{msisdn}", msisdn)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"msisdn\":\"79991112233\",\"incomingCall\":{\"totalTime\":\"00:30:00\"},\"outcomingCall\":{\"totalTime\":\"00:20:00\"}}"));

        verify(udrService, times(1)).getUDRForSubscriberByMonth(msisdn, year, month);
    }

    @Test
    void testGetUDRForPeriod_Success() throws Exception {
        String msisdn = "79991112233";
        String startDate = "2025-03-01T00:00:00";
        String endDate = "2025-03-01T23:59:59";

        UDR udr = new UDR(msisdn);
        udr.getIncomingCall().addDuration(Duration.ofMinutes(30));
        udr.getOutcomingCall().addDuration(Duration.ofMinutes(20));

        when(udrService.getUDRForSubscriberForPeriod(msisdn, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate))).thenReturn(udr);

        mockMvc.perform(get("/api/udr/byPeriod/{msisdn}", msisdn)
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"msisdn\":\"79991112233\",\"incomingCall\":{\"totalTime\":\"00:30:00\"},\"outcomingCall\":{\"totalTime\":\"00:20:00\"}}"));

        verify(udrService, times(1)).getUDRForSubscriberForPeriod(msisdn, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
    }
}
