package com.example.traveljournal.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "trips")
@Data
public class Trip {

    @Id
    private String id;

    // Always set from JWT — never from request body
    private String userId;

    private String destination;
    private String startDate;   // ISO date string e.g. "2025-06-01"
    private String endDate;
    private String notes;
    private int rating;         // 1-5
    private Date createdAt;
}
