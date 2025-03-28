package com.example.springapi.service;

import com.example.springapi.api.model.CDR;
import com.example.springapi.api.model.User;
import com.example.springapi.api.repos.CDRRepository;
import com.example.springapi.api.repos.SubscriberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;


/**
 * Сервис для генерации данных о CDR (Call Data Records) для абонентов.
 * Этот сервис генерирует случайные записи о звонках для абонентов в базе данных.
 */

@Service
public class CDRGeneratorService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private CDRRepository cdrRepository;

    private Random random = new Random();

    /**
     * Генерирует данные CDR для всех абонентов на основе заданного количества часов работы абонентов в день.
     * Генерация звонков будет случайной, и каждый абонент получит случайное количество звонков.
     *
     * @param hoursPerDay количество часов, в течение которых абоненты будут генерировать звонки.
     * @throws RuntimeException если в базе данных не найдено абонентов.
     */

    @Transactional
    public void generateCDRData(int hoursPerDay) {
        List<User> subscribers = subscriberRepository.findAll();
        if (subscribers.isEmpty()) {
            throw new RuntimeException("No subscribers found in the database.");
        }
        for (User subscriber : subscribers) {
            generateCallsForSubscriber(subscriber, hoursPerDay);
        }
    }

    /**
     * Генерирует звонки для конкретного абонента.
     * Каждый абонент будет иметь случайное количество звонков с случайными абонентами и временем звонков.
     *
     * @param subscriber абонент, для которого генерируются звонки.
     * @param hoursPerDay количество часов работы абонента в день.
     * @throws RuntimeException если в базе данных недостаточно абонентов для генерации звонков.
     */
    private void generateCallsForSubscriber(User subscriber, int hoursPerDay) {
        List<User> allSubscribers = subscriberRepository.findAll();
        if (allSubscribers.size() < 2) {
            throw new RuntimeException("Недостаточно абонентов для генерации звонков.");
        }

        int numberOfCalls = random.nextInt(10) + 5; // 5-15 звонков

        for (int i = 0; i < numberOfCalls; i++) {
            boolean isOutgoing = random.nextBoolean();
            String msisdn = isOutgoing ? subscriber.getPhoneNumber() : getRandomSubscriber(allSubscribers, subscriber).getPhoneNumber();
            String calledMsisdn = isOutgoing ? getRandomSubscriber(allSubscribers, subscriber).getPhoneNumber() : subscriber.getPhoneNumber();

            LocalDateTime startTime = LocalDateTime.now().minusDays(random.nextInt(365));
            Duration duration = Duration.ofMinutes(1 + random.nextInt(15));
            LocalDateTime endTime = startTime.plus(duration);

            CDR cdr = new CDR(isOutgoing ? "01" : "02", msisdn, calledMsisdn, startTime, endTime);
            cdrRepository.save(cdr);
        }
    }
    /**
     * Выбирает случайного абонента из списка всех абонентов, кроме текущего абонента.
     *
     * @param allSubscribers список всех абонентов.
     * @param current текущий абонент, для которого не должен быть выбран случайный абонент.
     * @return случайного абонента из списка.
     */
    private User getRandomSubscriber(List<User> allSubscribers, User current) {
        User randomSubscriber;
        do {
            randomSubscriber = allSubscribers.get(random.nextInt(allSubscribers.size()));
        } while (randomSubscriber.getPhoneNumber().equals(current.getPhoneNumber()));
        return randomSubscriber;
    }

}
