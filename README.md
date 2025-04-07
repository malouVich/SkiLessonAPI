# Ski Lesson Management System

This project is a system for managing ski lessons. It allows you to add and manage instructors, lessons, and locations for ski instruction. The system is built with Java and uses JPA (Jakarta Persistence API) to interact with the database.

## Features

- **Instructors**: Manage instructors and their details, such as name, email, phone number, and experience.
- **Ski Lessons**: Create and manage ski lessons, each with a specific start time, end time, price, and difficulty level (e.g., beginner, advanced).
- **Location**: Each lesson is associated with a location, stored with latitude and longitude.
- **Relationships**: An instructor can have multiple lessons. Lessons are linked to an instructor via a many-to-one relationship.

## Technologies Used

- **Java 17+**
- **Jakarta Persistence (JPA)** for database interaction
- **Lombok** 

## How to Run the Program

1. Create a database in your local PostgreSQL instance called `skilesson`.
2. Run the main method in the `Main` class to start the server on port 7070.
3. Use the `dev.http` file to test the available routes.
4. In the `dev.http` file, you can also register a user and log in.
5. First, run `GET {{url}}/skilessons/populate/` in the dev.http file to populate the database with sample data.
6. Use the `dev.http` file to test GET/POST/PUT/DELETE requests that are available.

## Additional Notes

- The `dev.http` file provides a set of pre-configured requests that can be used to interact with the system. And can be found under the package resources and package http.
- The server runs on port `7070` by default, but you can modify this in the application configuration if needed.

---

