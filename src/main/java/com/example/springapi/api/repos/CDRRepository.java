package com.example.springapi.api.repos;

import com.example.springapi.api.model.CDR;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CDRRepository extends JpaRepository<CDR,Integer> {

    List<CDR> findByNumberAndStartTimeBetween(String phoneNumber, LocalDateTime from, LocalDateTime to);

}
