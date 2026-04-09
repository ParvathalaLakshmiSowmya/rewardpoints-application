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
- Spring Boot 3.3.4
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
| startDate   | LocalDate    | Start date (yyyy-MM-dd)               |
| endDate     | LocalDate    | End date (yyyy-MM-dd)                 |
##### Important Rules
```
Provide either months OR startDate and endDate
If no parameters are provided → defaults to last 3 months
```
##### Sample Requests
```
Using Months
GET http://localhost:8080/rewards/calculaterewardpoints/1?months=3

Using Date Range
GET http://localhost:8080/rewards/calculaterewardpoints/1?startDate=2026-01-01&endDate=2026-03-01
```
##### Sample Response:
```
http://localhost:8080/rewards/calculaterewardpoints/2?startDate=2025-01-01&endDate=2026-04-08
response:
{
    "customerId": 2,
    "customerName": "Joe",
    "yearlyRewardsDetails": [
        {
            "year": 2025,
            "monthlyRewardsDetails": [
                {
                    "month": "JANUARY",
                    "totalAmount": 100.53,
                    "rewardPoints": 51
                }
            ],
            "totalAmountPerYear": 100.53,
            "totalPointsPerYear": 51
        },
        {
            "year": 2026,
            "monthlyRewardsDetails": [
                {
                    "month": "JANUARY",
                    "totalAmount": 110.00,
                    "rewardPoints": 70
                }
            ],
            "totalAmountPerYear": 110.00,
            "totalPointsPerYear": 70
        }
    ],
    "totalAmount": 210.53,
    "totalPoints": 121
}
```

# Key Features

* Layered architecture (Controller → Service → Repository)
* Custom exception handling using @RestControllerAdvice
* H2 database integration
* Unit and integration testing
* Logging using SLF4J

