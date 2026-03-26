# Reward Points Application

```
A Spring Boot REST API that calculates reward points for customer based on their transactions over a given time period.
```

# Reward Points Logic

- 2 points for every dollar spent above $100
- 1 point for every dollar spent between $50 and $100
- No points for amounts below $50

# Project Structure
```
src/main/java/com/demo/reward 
 ├── controller        # REST Controllers 
 ├── service           # Business Logic 
 ├── repository        # JPA Repositories 
 ├── entity            # Database Entities 
 ├── dto               # Data Transfer Objects 
 └── exception         # Custom Exceptions 

src/test/java/com/demo/reward 
 ├── testcontroller    # Controller Test Cases 
 └── testservice       # Service Test Cases 
```

# Build With
```
- Java 17 
- Spring Boot 3.2.5 
- Spring Data JPA 
- H2 In-Memory Database 
- Maven 
- JUnit 5 
- Mockito 
```
# Project Setup
```
- Create Project
    Generated using Spring Initializr
    Dependencies selected:
      Spring Web
      Spring Data JPA
      H2 Database
      Spring Boot DevTools
- Import into IDE
    Open Eclipse / STS / IntelliJ
    Import as Maven Project
    Select the downloaded project folder
```
# Tesing the Application
##### Calculate Reward Points
```
Endpoint: GET /rewards/calculaterewardpoints/{customerId}
```
##### Query Parameters
| Parameter   | Type    | Description                           |
|-------------|---------|---------------------------------------|
| months      | Integer | Fetch rewards for last N months       |
| startDate   | Date    | Start date (yyyy-MM-dd)               |
| endDate     | Date    | End date (yyyy-MM-dd)                 |
##### Important Rules
```
Provide either months OR startDate and endDate
If no parameters are provided → defaults to last 3 months
```
##### Sample Requests
```
Using Months
GET http://localhost:8081/rewards/calculaterewardpoints/1?months=3

Using Date Range
GET http://localhost:8081/rewards/calculaterewardpoints/1?startDate=2026-01-01&endDate=2026-03-01
```
##### Sample Response:
```
     {
        "customerId": 1,
        "customerName": "John",
        "totalRewardPoints": 200.0,
        "monthlyRewardPoints": {
            "JANUARY": 90.0,
            "FEBRUARY": 110.0
        }
      }
```

# Key Features

* Layered architecture (Controller → Service → Repository)
* Custom exception handling using @RestControllerAdvice
* H2 database integration
* Unit and integration testing
* Logging using SLF4J

