# Travel Journal API

A backend REST API for logging and managing personal travel journal entries.  
Built with **Spring Boot 3.4.5 · MongoDB · JWT Authentication · Docker**.

**CPSC 449 — Web Backend Engineering | Cal State Fullerton**  
**Team Members:**
- An Nguyen — CWID: 885598904
- Youtube link [https://youtu.be/gCk51avik10]

---

## 📖 Project Overview

The Travel Journal API allows authenticated users to create, read, update, and delete personal trip journal entries. Each user can only see and manage their own trips — full data isolation is enforced via JWT authentication.

### Domain: One User → Many Trips

| Entity | Fields |
|--------|--------|
| **User** | id, username, email, password (BCrypt) |
| **Trip** | id, userId (from JWT), destination, startDate, endDate, notes, rating, createdAt |

**Relationship:** One-to-Many — one User owns many Trips. The `userId` in each Trip is always taken from the JWT token, never from the request body.

---

## 🛠️ Tech Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.4.5 |
| Spring Security | 6.x |
| JJWT | 0.12.3 |
| MongoDB | 7.x (local) |
| Docker | 24+ |
| Build Tool | Maven (mvnw wrapper) |

---

## 📋 Phase 1: Prerequisites

1.  **Start MongoDB**: 
    *   Press `Win + R`, type `services.msc`, and hit Enter.
    *   Find **MongoDB Server**. Right-click it and select **Start**.
    *   Ensure it is running on the default port `27017`.
2.  **Docker Desktop** (Optional for local run, required for final project):
    *   Ensure it is open and shows "Engine Running".
3.  **No installation needed**: The project includes the Maven wrapper (`mvnw`), so you don't need to install Maven or Java manually.

---

## 🐳 Method 1: Running with Docker (Project Requirement)

**Use this for your demo video submission.**

### 1. Build the Docker Image
```powershell
docker build -t travel-journal:1.0 .
```

### 2. Run the Container
```powershell
docker run -d --name travel-journal -p 8080:8080 -e SPRING_DATA_MONGODB_URI=mongodb://host.docker.internal:27017/traveljournal travel-journal:1.0
```
> **Note:** `host.docker.internal` is required on Windows/Mac so the container can reach your local MongoDB.

### 3. Verify Startup
```powershell
docker logs travel-journal
```
Look for the Spring Boot banner and `Started TravelJournalApplication`.

---

## 💻 Method 2: Running Locally (Alternative - No Docker)

**Use this for development or if Docker Desktop is not available.**

### 1. Build the project
```powershell
.\mvnw.cmd clean package -DskipTests
```

### 2. Run the app
```powershell
.\mvnw.cmd spring-boot:run
```
The app will be available at `http://localhost:8080`.

---

## 🧪 Phase 2: Testing Everything (Postman Walkthrough)

Open Postman and perform these **9 mandatory tests** in order:

### 1. Register a New User (POST)
*   **URL**: `http://localhost:8080/api/auth/register`
*   **Method**: `POST`
*   **Body** -> `raw` -> `JSON`:
    ```json
    { "username": "globetrotter", "email": "world@tour.com", "password": "password123" }
    ```
*   **Result**: Copy the `"token"` from the response.

### 2. Login (POST)
*   **URL**: `http://localhost:8080/api/auth/login`
*   **Method**: `POST`
*   **Body** -> `JSON`:
    ```json
    { "email": "world@tour.com", "password": "password123" }
    ```
*   **Result**: You receive the same JWT token.

### 3. Create a Trip (POST - Protected)
*   **URL**: `http://localhost:8080/api/trips`
*   **Method**: `POST`
*   **Auth Tab**: Select `Bearer Token` and paste your token.
*   **Body** -> `JSON`:
    ```json
    { "destination": "Hawaii", "startDate": "2025-06-01", "endDate": "2025-06-10", "notes": "Surf trip!", "rating": 5 }
    ```
*   **Expect**: `201 Created`. Copy the `"id"` of the new trip.

### 4. Read All My Trips (GET - Protected)
*   **URL**: `http://localhost:8080/api/trips`
*   **Method**: `GET`
*   **Auth Tab**: `Bearer Token` (should still be active).
*   **Expect**: `200 OK`. You see a list containing your trip.

### 5. Get a Single Trip (GET - Protected)
*   **URL**: `http://localhost:8080/api/trips/<TRIP_ID>`
*   **Method**: `GET`
*   **Auth Tab**: `Bearer Token`.
*   **Expect**: `200 OK`. You see the full JSON for just that specific trip.

### 6. Update a Trip (PUT - Protected)
*   **URL**: `http://localhost:8080/api/trips/<TRIP_ID>`
*   **Method**: `PUT`
*   **Auth Tab**: `Bearer Token`.
*   **Body** -> `JSON`:
    ```json
    { "destination": "Tokyo, Japan", "startDate": "2025-10-01", "endDate": "2025-10-10", "notes": "Cherry blossoms and sushi!", "rating": 5 }
    ```
*   **Expect**: `200 OK`. The trip details are updated to Tokyo in MongoDB.

### 7. Delete a Trip (DELETE - Protected)
*   **URL**: `http://localhost:8080/api/trips/<TRIP_ID>`
*   **Method**: `DELETE`
*   **Auth Tab**: `Bearer Token`.
*   **Expect**: `200 OK` with a success message.
*   **Verification**: Try to `GET /api/trips` again; the list should be empty.

### 8. Data Isolation Check (The "403 Test")
*   Create a second user (Register as `tester2`).
*   Try to `DELETE` or `PUT` the first user's Trip ID using the second user's token.
*   **Expect**: `403 Forbidden`. This proves your data isolation logic is working.

### 9. Unauthorized Access (The "401 Test")
*   Try to `GET /api/trips` but remove the Bearer Token from the Auth tab.
*   **Expect**: `401 Unauthorized`. This proves your security filter is protecting the routes.


---

## 🛠️ Troubleshooting & Clean Up

*   **Error 401 Unauthorized**: JWT token is missing, malformed, or expired.
*   **Error 403 Forbidden**: You are trying to access/edit a trip ID that belongs to a different user.
*   **Error 409 Conflict**: Trying to register an email that is already in the database.
*   **Stop Docker Container**:
    ```powershell
    docker stop travel-journal && docker rm travel-journal
    ```

---

## 📂 Project Structure

```
src/main/java/com/example/traveljournal/
├── config/SecurityConfig.java         # Security setup & JWT filter chain
├── controller/AuthController.java     # /api/auth/register, login
├── controller/TripController.java     # CRUD /api/trips
├── dto/                               # Data Transfer Objects
├── entity/User.java + Trip.java       # MongoDB Documents
├── exception/GlobalExceptionHandler   # Clean JSON error responses
├── filter/JwtFilter.java              # Bearer token validation
├── repository/                        # MongoRepository interfaces
├── service/AuthService.java           # BCrypt & Auth logic
├── service/TripService.java           # CRUD & Ownership checks
└── util/JwtUtil.java                  # JWT generation & parsing
```
