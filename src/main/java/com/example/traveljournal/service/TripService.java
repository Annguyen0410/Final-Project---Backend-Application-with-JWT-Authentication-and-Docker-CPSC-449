package com.example.traveljournal.service;

import com.example.traveljournal.dto.TripRequest;
import com.example.traveljournal.entity.Trip;
import com.example.traveljournal.exception.ForbiddenException;
import com.example.traveljournal.exception.ResourceNotFoundException;
import com.example.traveljournal.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    /** Create a new trip. userId MUST come from the JWT token, never from request body. */
    public Trip createTrip(TripRequest request, String userId) {
        Trip trip = new Trip();
        trip.setUserId(userId);
        trip.setDestination(request.getDestination());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setNotes(request.getNotes());
        trip.setRating(request.getRating());
        trip.setCreatedAt(new Date());
        return tripRepository.save(trip);
    }

    /** Returns only trips belonging to the authenticated user — data isolation enforced. */
    public List<Trip> getAllTrips(String userId) {
        return tripRepository.findByUserId(userId);
    }

    /** Returns a single trip by ID. 404 if not found, 403 if owned by another user. */
    public Trip getTripById(String id, String userId) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip with id " + id + " not found"));

        if (!trip.getUserId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to access this trip");
        }
        return trip;
    }

    /** Updates a trip. 404 if not found, 403 if owned by another user. */
    public Trip updateTrip(String id, TripRequest request, String userId) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip with id " + id + " not found"));

        if (!trip.getUserId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to update this trip");
        }

        trip.setDestination(request.getDestination());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate());
        trip.setNotes(request.getNotes());
        trip.setRating(request.getRating());
        return tripRepository.save(trip);
    }

    /** Deletes a trip. 404 if not found, 403 if owned by another user. */
    public void deleteTrip(String id, String userId) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip with id " + id + " not found"));

        if (!trip.getUserId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to delete this trip");
        }

        tripRepository.deleteById(id);
    }
}
