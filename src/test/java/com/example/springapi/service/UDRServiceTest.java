
package com.example.springapi.service;

import com.example.springapi.api.model.CDR;
import com.example.springapi.api.model.UDR;
import com.example.springapi.api.repos.CDRRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UDRServiceTest {

    @Mock
    private CDRRepository cdrRepository;

    @InjectMocks
    private UDRService udrService;

    private final String TEST_MSISDN = "79991112233";

    private List<CDR> mockCDRData() {
        return Arrays.asList(
                new CDR("01", TEST_MSISDN, "79992223344",
                        LocalDateTime.of(2024, 3, 10, 12, 0),
                        LocalDateTime.of(2024, 3, 10, 12, 30)), // Исходящий - 30 мин
                new CDR("02", TEST_MSISDN, "79993334455",
                        LocalDateTime.of(2024, 3, 11, 14, 0),
                        LocalDateTime.of(2024, 3, 11, 14, 15))  // Входящий - 15 мин
        );
    }

    @Test
    void testGetUDRForSubscriberByMonth() {
        LocalDateTime start = LocalDateTime.of(2024, 3, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1).minusSeconds(1);

        when(cdrRepository.findByNumberAndStartTimeBetween(TEST_MSISDN, start, end)).thenReturn(mockCDRData());

        UDR udr = udrService.getUDRForSubscriberByMonth(TEST_MSISDN, 2024, 3);

        assertNotNull(udr);
        assertEquals(TEST_MSISDN, udr.getMsisdn());
        assertEquals("00:15:00", udr.getIncomingCall().getTotalTime());
        assertEquals("00:30:00", udr.getOutcomingCall().getTotalTime());

        verify(cdrRepository, times(1)).findByNumberAndStartTimeBetween(TEST_MSISDN, start, end);
    }

    @Test
    void testGetUDRForSubscriberForPeriod() {
        LocalDateTime start = LocalDateTime.of(2024, 3, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 3, 15, 23, 59);

        when(cdrRepository.findByNumberAndStartTimeBetween(TEST_MSISDN, start, end)).thenReturn(mockCDRData());

        UDR udr = udrService.getUDRForSubscriberForPeriod(TEST_MSISDN, start, end);

        assertNotNull(udr);
        assertEquals(TEST_MSISDN, udr.getMsisdn());
        assertEquals("00:15:00", udr.getIncomingCall().getTotalTime());
        assertEquals("00:30:00", udr.getOutcomingCall().getTotalTime());

        verify(cdrRepository, times(1)).findByNumberAndStartTimeBetween(TEST_MSISDN, start, end);
    }
}
