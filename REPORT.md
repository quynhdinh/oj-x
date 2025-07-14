# Course Project Report

**OJ-X Title:** Online Judge System

**Course:** Modern Programming Practices

**Block:** July 2025

**Instructor:** Dr. Bright Gee Varghese R

**Team Members:**

\[Vu Quynh Dinh]  \[619568]

**Date of Submission:** \[07/15/2025]

---

# Important Project Requirements

**Stream API:**

You must make use of the Java Stream API wherever it is applicable, especially for collection processing, filtering, mapping, aggregation, and other data operations. Clearly highlight these usages in your code and documentation.

**Unit Testing:**

Implement unit tests for your business logic using JUnit or a similar testing framework. Include instructions for running your test suite and ensure your tests cover major functionalities and edge cases.

**Singleton Pattern:**

Where a class should have only one instance (for example, for managing resources, database connections, configuration, etc.), apply the Singleton design pattern. 

---

## 1. Problem Description

This online judge system empowers users to enhance their programming skills through solving categorized problems and participating in contests. By providing a structured platform for practice and competition, it encourages continuous learning and skill development.

---

## 2. User Stories

Describe the system from the user's perspective using user stories:

* As a \[user role], I want to \[feature] so that \[benefit].

Admin User Stories

* As an admin, I want to manage user accounts so that I can control access and ensure a safe environment.

* As an admin, I want to review submitted problems so that I can ensure they meet quality standards before they are published.

* As an admin, I want to set contest using problems that problem setter submitted and set points for each problems.

User User Stories

* As a user, I want to view and solve categorized programming problems so that I can practice specific skills and track my progress.

* As a user, I want to participate in coding contests so that I can challenge myself and compete with others.

* As a user, I want to receive instant feedback on my submissions so that I can learn from my mistakes and improve my coding skills.

* As a user, I want to manage my personal information by having a personal profile UI so that others know who I am

Problem Setter User Stories

* As a problem setter, I want to create and submit new problems so that I can contribute to the platform and help other users learn.

* As a problem setter, I want to set difficulty levels for problems so that users can choose challenges that match their skills.

* As a problem setter, I want to review user submissions for my problems so that I can provide insights and feedback on their solutions.

* As a problem setter, I want to categorize my problems so that users can easily find relevant challenges based on their interests.

---

## 3. Functional Requirements
User Management
    User Registration: Users must be able to create an account by providing necessary information (e.g., username, email, password).
  
    User Login/Logout: Users must be able to log in and log out securely.

    Profile Management: Users must be able to view and edit their profiles, including personal information and preferences.

Problem Management

    Problem Creation: Problem setters must be able to create, edit, and delete programming problems.
    
    Problem Categorization: Problems must be categorized by difficulty, topic, and tags for easy navigation.
    
    Problem Review: Admins must be able to review and approve problems before they are published.

Submission and Evaluation

    Code Submission: Users must be able to submit their code solutions for various problems.
    
    Automated Grading: The system must automatically compile and run submitted code against predefined test cases to evaluate correctness.
    
    Feedback and Results: Users must receive immediate feedback on their submissions, including whether their solution is correct and performance metrics.

Contests

    Contest Creation: Admins must be able to create and manage coding contests, including setting time limits and problem sets.
    
    Contest Participation: Users must be able to register for and participate in contests.
    
    Leaderboard: The system must maintain a leaderboard displaying participant rankings based on their performance in contests.

---

## 4. Non-Functional Requirements

List requirements such as usability, security, reliability, performance, scalability, etc.
Performance

    Response Time: The system must respond to user actions (e.g., submissions, page loads) within 2 seconds under normal load.

Scalability

    User Capacity: The system must support at least 1,000 concurrent users without performance degradation.

Security

    Data Encryption: User data must be encrypted both in transit and at rest to ensure privacy and security.

Usability

    User Interface: The system must have an intuitive and user-friendly interface accessible to users with varying technical skills.

Availability

    Uptime: The system must have an uptime of 99.9% to ensure continuous access for users.

Maintainability

    Modular Architecture: The system must be designed with modular components to facilitate updates and maintenance.
---

## 5. Architecture of Project

### 5.1 Overview

Provide an overview of your system's layered architecture. Identify each layer and briefly describe its purpose.

### 5.2 Architecture Diagram

*(Insert a diagram here)*

https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.oreilly.com%2Flibrary%2Fview%2Fsoftware-architecture-patterns%2F9781491971437%2Fch01.html&psig=AOvVaw1MvFBR-hwiX6nYyg9wbaq7&ust=1752555946799000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqGAoTCMjf0OfJu44DFQAAAAAdAAAAABCEAQ<img width="1127" height="843" alt="image" src="https://github.com/user-attachments/assets/e2aa8498-ae27-4ab7-bba0-03e5223de976" />

### 5.3 Technologies Used

List all major technologies (e.g., Java 24, MySQL).

### 5.4 Layer Descriptions

* **Presentation Layer:** Brief description.
* **Service Layer:** Brief description.
* **Data Access Layer:** Brief description.
* **Database:** Brief description.

---

## 6. Use Case Diagram(s)

Insert your use case diagram(s) here (as an image or diagram link).

---

## 7. Use Case Descriptions

Provide detailed descriptions for each use case:

* **Use Case Name:**
* **Primary Actor(s):**
* **Preconditions:**
* **Postconditions:**
* **Main Success Scenario:**

---

## 8. Class Diagram

Insert your UML class diagram image. Include key classes, their attributes, methods, relationships (associations, inheritance, interfaces, composition).

---

## 9. Sequence Diagrams

Provide sequence diagrams for important use cases.

---

## 10. Screenshots

Include relevant screenshots of your application's interface and features.

---

## 11. Installation & Deployment

Detailed, step-by-step instructions for:

* Cloning the repository
* Setting up dependencies and environment
* Database setup (with scripts if needed)
* Running the application (CLI/GUI/Web)
* (Optional) Docker setup instructions

---

## 12. How to Use

Provide a user guide or demo steps, including sample commands, test data, or login info (if relevant).

---

## 13. Design Justification & Principles

Explain your key design choices, such as:

* Use of interfaces, abstract classes, inheritance (Liskov Substitution Principle), composition.
* Application of Open-Closed Principle.
* Design patterns used (if any).

---

## 14. Team Members

\[There is only me in this project, so I will not include this section]

---

## 15. References

List all external resources, libraries, frameworks, and sources consulted.

---
## Grading Rubric (Total: 10 Points)

| Criteria                                     | Points | Description / Expectations                                                                                   |
| -------------------------------------------- | :----: | ------------------------------------------------------------------------------------------------------------ |
| **Problem Description & User Stories**       |    1   | Clearly states the problem and provides meaningful, relevant user stories.                                   |
| **Functional & Non-Functional Requirements** |    1   | Functional and non-functional requirements are complete, clear, and relevant.                                |
| **Architecture & Design**                    |    1   | Well-structured layered architecture, clear diagrams (class, sequence, use case), thoughtful design.         |
| **Use of Stream API**                        |    1   | Appropriately uses Java Stream API wherever possible; usage is clear and well-documented.                    |
| **Singleton Pattern (when applicable)**      |    1   | Applies the Singleton pattern where necessary; justification provided in documentation.                      |
| **Unit Testing**                             |    1   | Implements unit tests for key business logic; tests are meaningful and cover main cases.                     |
| **Implementation Quality**                   |    1   | Code quality: modularity, clean structure, meaningful naming, adherence to SOLID principles, error handling. |
| **Deployment, Installation & Usability**     |    1   | Clear setup instructions, successful deployment, working UI/CLI, and usability.                              |
| **Documentation & Reporting**                |    1   | Detailed README: all sections complete (screenshots, diagrams, instructions, principles, references, etc).   |
| **Presentation & Teamwork**                  |    1   | Professionalism in presentation (repo, submission, screenshots), teamwork (if applicable), and originality.  |
| **Total**                                    | **10** |                                                                                                              |

