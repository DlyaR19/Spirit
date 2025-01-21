package com.spirit.application.entitiy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a notification for a user.
 */
@Getter
@Setter
@Entity
@Table(name = "notifications", schema = "public")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificationid", nullable = false)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    private User user; // User who will receive the notification

    @Column(name = "message", nullable = false)
    private String message; // Message content, e.g., "Message from Username"

    @Column(name = "isread", nullable = false)
    private boolean isRead = false; // Indicates whether the notification has been read

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}

