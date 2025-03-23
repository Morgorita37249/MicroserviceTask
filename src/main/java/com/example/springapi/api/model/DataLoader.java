package com.example.springapi.api.model;

import com.example.springapi.api.repos.SubscriberRepository;
import com.example.springapi.service.CDRGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private CDRGeneratorService cdrGeneratorService;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 10; i++) {
            User subscriber = new User("7" + (100000000 + random.nextInt(900000000)), "Subscriber " + (i + 1));
            subscriberRepository.save(subscriber);
        }

        cdrGeneratorService.generateCDRData(24);
    }
}

