package com.spirit.application.entitiy;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "chatmessage", schema = "public")
public class ChatMessage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatmessageid", length = 64, nullable = false)
    private long chatMessageID;
    @ManyToOne
    @JoinColumn(name = "senderid")
    @Getter
    private User sender;
    @ManyToOne
    @JoinColumn(name = "recipientid")
    @Getter
    private User recipient;
    @Basic
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    @Basic
    @Column(name = "timestamp", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp;

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
