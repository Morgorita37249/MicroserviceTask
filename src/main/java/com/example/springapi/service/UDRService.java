package com.example.springapi.service;

import com.example.springapi.api.model.CDR;
import com.example.springapi.api.model.UDR;
import com.example.springapi.api.repos.CDRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UDRService {

    @Autowired
    private CDRRepository cdrRepository;

    public UDR getUDRForSubscriberByMonth(String msisdn, int year, int month) {
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1);

        List<CDR> cdrList = cdrRepository.findByNumberAndStartTimeBetween(msisdn, startDate, endDate);
        return generateUDR(msisdn, cdrList);
    }

    public UDR getUDRForSubscriberForPeriod(String msisdn, LocalDateTime startDate, LocalDateTime endDate) {
        List<CDR> cdrList = cdrRepository.findByNumberAndStartTimeBetween(msisdn, startDate, endDate);

        return generateUDR(msisdn, cdrList);
    }

    private UDR generateUDR(String msisdn, List<CDR> cdrList) {
        Duration incomingDuration = Duration.ZERO;
        Duration outgoingDuration = Duration.ZERO;

        for (CDR cdr : cdrList) {
            Duration callDuration = Duration.between(cdr.getStartTime(), cdr.getEndTime());

            if ("01".equals(cdr.getCallType())) {
                outgoingDuration = outgoingDuration.plus(callDuration);
            } else if ("02".equals(cdr.getCallType())) {
                incomingDuration = incomingDuration.plus(callDuration);
            }
        }

        UDR udr = new UDR(msisdn);
        udr.getIncomingCall().addDuration(incomingDuration);
        udr.getOutcomingCall().addDuration(outgoingDuration);

        return udr;
    }
}



