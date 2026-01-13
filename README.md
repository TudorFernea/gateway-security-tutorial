# Tutorial: Securing Microservices with Spring Cloud Gateway

This project demonstrates how to implement a custom API Key security filter using Spring Cloud Gateway.

## Architecture
- **Gateway Service (8080):** The entry point. Checks for `X-API-KEY`.
- **Dummy Service (8081):** A protected microservice that holds sensitive data.

## How to Run
1. Ensure you have Docker Installed.
2. Build the services:
   ```bash
   # Windows
   cd dummy-service && mvnw clean package -DskipTests && cd ..
   cd gateway-service && mvnw clean package -DskipTests && cd ..
   
   # Mac/Linux
   cd dummy-service && ./mvnw clean package -DskipTests && cd ..
   cd gateway-service && ./mvnw clean package -DskipTests && cd ..