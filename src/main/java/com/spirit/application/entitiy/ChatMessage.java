package com.spirit.application.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 * Represents a message exchanged between two users in a chat.
 */
@Getter
@Setter
@Entity
@Table(name = "chatmessage", schema = "public")
public class ChatMessage implements Serializable {

    /**
     * Unique identifier for the chat message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatmessageid", length = 64, nullable = false)
    private long chatMessageID;

    /**
     * The sender of the message.
     */
    @ManyToOne
    @JoinColumn(name = "senderid")
    @Getter
    private User sender;

    /**
     * The recipient of the message.
     */
    @ManyToOne
    @JoinColumn(name = "recipientid")
    @Getter
    private User recipient;

    /**
     * The content of the message.
     */
    @Basic
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * The timestamp when the message was created.
     */
    @Basic
    @Column(name = "timestamp", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp;

    /**
     * Automatically sets the timestamp to the current time when the message is created.
     */
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now(ZoneId.systemDefault());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage chatMessage = (ChatMessage) o;
        return chatMessageID == chatMessage.chatMessageID &&
                sender == chatMessage.sender &&
                recipient == chatMessage.recipient &&
                content.equals(chatMessage.content) &&
                timestamp.equals(chatMessage.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatMessageID, sender, recipient, content, timestamp);
    }

}
