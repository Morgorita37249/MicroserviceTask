package com.example.springapi.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Setter
@Getter
public class UDR {

    private String msisdn;
    private CallDuration incomingCall;
    private CallDuration outcomingCall;

    public UDR(String msisdn) {
        this.msisdn = msisdn;
        this.incomingCall = new CallDuration();
        this.outcomingCall = new CallDuration();
    }

    public static class CallDuration {
        private Duration totalTime = Duration.ZERO;

        public void addDuration(Duration duration) {
            this.totalTime = this.totalTime.plus(duration);
        }

        public String getTotalTime() {
            return String.format("%02d:%02d:%02d", totalTime.toHours(), totalTime.toMinutes() % 60, totalTime.getSeconds() % 60);
        }
    }
}

