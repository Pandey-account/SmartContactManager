# Task Management System

## Backend (Spring Boot API)

### Project Setup
- Create a new Spring Boot project.
- Include dependencies for Spring Web, Spring Data JPA, MySQL Driver, Spring Security.

### Database Configuration
- Configure `application.properties` with database connection details.
spring.datasource.name="your database name"
spring.datasource.url="your database url"
spring.datasource.username="Your userName"
spring.datasource.password="Your Password"
spring.datasource.driver-class-name="Your driver name"
spring.jpa.properties.hibernate.dialect="dilect name"
spring.jpa.hibernate.ddl-auto=update

### Entity Class
- Create a `User` entity class with JPA annotations.
- Create a `Contact` entity class with JPA annotations.

### Repository
- Create a `UserRepository` interface extending `JpaRepository`.
- Create a `ContactRepository` interface extending `JpaRepository`.

### Controller
- Implement a `HomeController` with CRUD endpoints.

# Default
home Page: 'localhost:8080/'           -home page
Create User:'localhost:8080/signup'    -navbar click signup
Login User:'localhost:8080/sign in'    -navbar click login
About Page:'localhost:8080/about'      -navbar click about

# user authantication
User: user profile: 'localhost:8080/user/profile'         -sidebar click profile 
User: user update: 'localhost:8080/user/user-update/id'   -profile update button click
Deleate User:'localhost:8080/user/user-delete/id'         -profile delete button click


- Implement a `UserController` with CRUD endpoints.
# user authantication 
Contact: home Page:'localhost:8080/user/index'                                  -Get Start button click
Contact: add Contact:'localhost:8080/user/add-contact'                          -sidebar add-contact button click 
Contact: Show contacts:'localhost:8080/user/show-contacts/page'                 -sidebar view-contacts button click 
Contact: Indivisual Contact deatils:'localhost:8080/user/contact/cid'           -view contacts email link click 
Contact: delete Indivisual contact:'localhost:8080/user/delete/cid'             -view contacts delete button click
Contact: update Indivisual contact:'localhost:8080/user/update-contact/cid'     -view contacts update button click
settings: change password: 'localhost:8080/user/settings'                       -sidebar settings click

### Security Configuration
- Implement basic authentication or authorization using Spring Security.

### Component Structure
- Create components for User (display, add, update, delete).
- Create components for Contact (display, add, update, delete).

### API Integration
- Use `fetch` or a similar method to communicate with the backend API.

### User Interface Design
- Implement a clean and intuitive UI.

### Error Handling
- Display meaningful error messages to users.

### Authentication Integration
- If applicable, integrate authentication features with the backend.