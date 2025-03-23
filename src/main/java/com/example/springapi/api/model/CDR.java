package com.example.springapi.api.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "CDR")
public class CDR {
    @Id
    @GeneratedValue
    private Long id;

    private String callType;
    private String number;
    private String calledNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public CDR() {}

    public CDR(String callType, String number, String calledNumber, LocalDateTime startTime, LocalDateTime endTime) {
        this.callType = callType;
        this.number = number;
        this.calledNumber = calledNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
