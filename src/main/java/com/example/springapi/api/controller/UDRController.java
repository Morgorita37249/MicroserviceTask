package com.example.springapi.api.controller;

import com.example.springapi.service.UDRService;
import com.example.springapi.api.model.UDR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/udr")
public class UDRController {

    @Autowired
    private UDRService udrService;

    @GetMapping("/byMonth/{msisdn}")
    public UDR getUDRForMonth(
            @PathVariable String msisdn,
            @RequestParam int year,
            @RequestParam int month) {
        return udrService.getUDRForSubscriberByMonth(msisdn, year, month);
    }

    @GetMapping("/byPeriod/{msisdn}")
    public UDR getUDRForPeriod(
            @PathVariable String msisdn,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        LocalDateTime start = LocalDateTime.parse(startDate.trim());
        LocalDateTime end = LocalDateTime.parse(endDate.trim());

        return udrService.getUDRForSubscriberForPeriod(msisdn, start, end);
    }
}


