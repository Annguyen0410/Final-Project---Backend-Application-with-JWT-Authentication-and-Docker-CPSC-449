package com.example.traveljournal.repository;

import com.example.traveljournal.entity.Trip;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TripRepository extends MongoRepository<Trip, String> {
    // Returns only trips belonging to this user — enforces data isolation
    List<Trip> findByUserId(String userId);
}
