package com.example.traveljournal.dto;

import lombok.Data;

@Data
public class TripRequest {
    private String destination;
    private String startDate;   // e.g. "2025-06-01"
    private String endDate;
    private String notes;
    private int rating;         // 1-5
}
