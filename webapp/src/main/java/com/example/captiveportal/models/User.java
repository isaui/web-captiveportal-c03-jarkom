package com.example.captiveportal.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_logged_in")
    private boolean isLoggedIn;

    // Constructor untuk kemudahan pembuatan object
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isLoggedIn = false;
    }
}
