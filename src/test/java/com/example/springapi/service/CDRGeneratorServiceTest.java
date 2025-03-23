package com.example.springapi.service;

import com.example.springapi.api.model.CDR;
import com.example.springapi.api.model.User;
import com.example.springapi.api.repos.CDRRepository;
import com.example.springapi.api.repos.SubscriberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CDRGeneratorServiceTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    @Mock
    private CDRRepository cdrRepository;

    @InjectMocks
    private CDRGeneratorService cdrGeneratorService;

    private User subscriber1;
    private User subscriber2;

    @BeforeEach
    void setUp() {
        subscriber1 = new User();
        subscriber1.setPhoneNumber("79991112233");
        subscriber1.setName("Subscriber 1");

        subscriber2 = new User();
        subscriber2.setPhoneNumber("79993334455");
        subscriber2.setName("Subscriber 2");
    }

    @Test
    void generateCDRData_Success() {
        List<User> subscribers = Arrays.asList(subscriber1, subscriber2);
        when(subscriberRepository.findAll()).thenReturn(subscribers);

        cdrGeneratorService.generateCDRData(24);

        verify(cdrRepository, atLeast(1)).save(any(CDR.class));
    }

    @Test
    void generateCDRData_NoSubscribers() {
        when(subscriberRepository.findAll()).thenReturn(Arrays.asList());

        assertThrows(RuntimeException.class, () -> {
            cdrGeneratorService.generateCDRData(24);
        });
    }

    @Test
    void generateCDRData_InsufficientSubscribers() {
        List<User> subscribers = Arrays.asList(subscriber1);
        when(subscriberRepository.findAll()).thenReturn(subscribers);

        assertThrows(RuntimeException.class, () -> {
            cdrGeneratorService.generateCDRData(24);
        });
    }
}


