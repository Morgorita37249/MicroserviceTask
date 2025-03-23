package com.example.springapi.api.model;

import com.example.springapi.api.repos.SubscriberRepository;
import com.example.springapi.service.CDRGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@SpringBootTest
class DataLoaderTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    @Mock
    private CDRGeneratorService cdrGeneratorService;

    @InjectMocks
    private DataLoader dataLoader;

    private User subscriber;

    @BeforeEach
    void setUp() {
        subscriber = new User("79991112233", "Subscriber 1");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun() throws Exception {
        when(subscriberRepository.save(any(User.class))).thenReturn(subscriber);
        dataLoader.run();
        verify(subscriberRepository, times(10)).save(any(User.class));

        verify(cdrGeneratorService, times(1)).generateCDRData(24);
    }
}

