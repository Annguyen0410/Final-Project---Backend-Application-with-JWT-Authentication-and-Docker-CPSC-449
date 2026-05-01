package com.example.traveljournal.controller;

import com.example.traveljournal.dto.TripRequest;
import com.example.traveljournal.entity.Trip;
import com.example.traveljournal.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    /**
     * POST /api/trips
     * Creates a new trip. userId is extracted from the JWT (Authentication.getName()),
     * never from the request body.
     * Returns: 201 Created
     */
    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody TripRequest request,
                                            Authentication auth) {
        String userId = auth.getName();  // principal = userId from JWT 'sub' claim
        Trip created = tripService.createTrip(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET /api/trips
     * Returns all trips belonging to the authenticated user only.
     * Other users' trips are NEVER returned.
     * Returns: 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips(Authentication auth) {
        String userId = auth.getName();
        return ResponseEntity.ok(tripService.getAllTrips(userId));
    }

    /**
     * GET /api/trips/{id}
     * Returns: 200 OK, 404 Not Found, or 403 Forbidden
     */
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable String id, Authentication auth) {
        String userId = auth.getName();
        return ResponseEntity.ok(tripService.getTripById(id, userId));
    }

    /**
     * PUT /api/trips/{id}
     * Updates a trip. Only the owner may update.
     * Returns: 200 OK, 404 Not Found, or 403 Forbidden
     */
    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable String id,
                                            @RequestBody TripRequest request,
                                            Authentication auth) {
        String userId = auth.getName();
        return ResponseEntity.ok(tripService.updateTrip(id, request, userId));
    }

    /**
     * DELETE /api/trips/{id}
     * Deletes a trip. Only the owner may delete.
     * Returns: 200 OK, 404 Not Found, or 403 Forbidden
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTrip(@PathVariable String id,
                                                           Authentication auth) {
        String userId = auth.getName();
        tripService.deleteTrip(id, userId);
        return ResponseEntity.ok(Map.of("message", "Trip deleted successfully"));
    }
}
