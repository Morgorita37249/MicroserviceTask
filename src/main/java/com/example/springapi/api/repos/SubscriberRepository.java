package com.example.springapi.api.repos;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springapi.api.model.User;

public interface SubscriberRepository extends JpaRepository<User,Integer>{

}
