package com.example.springapi.api.model;

import com.example.springapi.api.repos.SubscriberRepository;
import com.example.springapi.service.CDRGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Компонент, который выполняет загрузку данных в базу при запуске приложения.
 * Создает 10 абонентов с уникальными номерами и генерирует для них CDR.
 * Используется для первоначальной инициализации базы данных.
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private CDRGeneratorService cdrGeneratorService;

    private final Random random = new Random();
    /**
     * Метод, который выполняется при старте приложения.
     * Создает 10 абонентов и сохраняет их в базу данных.
     * Генерирует данные CDR для всех абонентов, используя сервис CDRGeneratorService.
     *
     * @param args аргументы командной строки, передаваемые при запуске приложения.
     * @throws Exception если произошла ошибка при выполнении.
     */
    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 10; i++) {
            User subscriber = new User("7" + (100000000 + random.nextInt(900000000)), "Subscriber " + (i + 1));
            subscriberRepository.save(subscriber);
        }

        cdrGeneratorService.generateCDRData(24);
    }
}

