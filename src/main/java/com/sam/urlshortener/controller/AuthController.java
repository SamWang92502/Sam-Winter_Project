package com.sam.urlshortener.controller;

import com.sam.urlshortener.model.User;
import com.sam.urlshortener.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Use the ResponseEntity returned from the service method
            return userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        } catch (IllegalArgumentException e) {
            // Return an error response for invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    //"When a user sends an HTTP POST request to the URL /signup, run the method right below this annotation."
    //@PostMapping: This is a Spring annotation used in REST controllers. It maps a method to a POST request.
    @PostMapping("/signup")
    // ResponseEntity is the return type of the method
    // ResponseEntity is a Spring class that represents the full HTTP response
    // (including Status code (like 200 OK or 400 Bad Request), Headers, Body(the actual data returned, e.g. JSON))
    // <?> is a wildcard — it means the response body can be of any type (could be a JWT token, an error message, etc.).
    // signup: name of the method
    // (@RequestBody SignupRequest signupRequest):
    // @RequestBody: tells Spring “Take the JSON data from the HTTP request body, and convert (deserialize) it into a Java object.”
    // SignupRequest is a custom class (a DTO – Data Transfer Object) that likely has fields like username and password.
    // signupRequest is just the name of the variable that holds the deserialized object.
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        // You put code inside a try block that might throw an exception — for example, invalid input, a duplicate user, etc.
        // If that exception does happen, Java skips the rest of the try block and runs the catch block.
        try {
            //This line calls a service method to create a new user in the database.
            User user = userService.createUser(
                    signupRequest.getUsername(),
                    signupRequest.getEmail(),
                    signupRequest.getPassword()
            );
            //After successfully creating the user, a JWT token is generated for that user.
            String token = userService.generateTokenForUser(user.getUsername());
            //This token will be returned to the client to be used for future authenticated requests.
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }


    /*
    A DTO is a simple Java object used to transfer data between different layers of an application
    (like between a client and a server, or a controller and a service).
    It typically contains only fields and getters/setters, with no business logic.
     */
    // DTO for signup
    static class SignupRequest {
        private String username;
        private String password;
        private String email;

        // Getters and setters
        /*
        setUsername() → (object populated) → getUsername() used in your backend
        Think of SignupRequest like a form someone fills out.
        - setUsername("sam123") = user fills in the form
        Object populated: Filling the fields (like username and password) of an object with actual values.
        - getUsername() = backend reads what the user filled in
        ex. Registering a new user: getUsername() and getPassword() are used to extract user input and pass it to the service layer.
         */
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
    }



    // DTO for login request
    static class LoginRequest {
        private String username;
        private String password;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
