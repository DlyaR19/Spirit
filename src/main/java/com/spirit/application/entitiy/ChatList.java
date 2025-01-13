package com.spirit.application.entitiy;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a chat list entry between two users.
 */
@Data
@Entity
@Getter
@Setter
@Table(name = "chatlist", schema = "public")
public class ChatList implements Serializable {

    /**
     * Unique identifier for the chat list entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatlistid", length = 64, nullable = false)
    private Long chatlistid;

    /**
     * The user involved in the chat.
     */
    @ManyToOne
    @JoinColumn(name = "userid")
    @Getter
    private User user;

    /**
     * The other user involved in the chat.
     */
    @ManyToOne
    @JoinColumn(name = "other_userid")
    @Getter
    private User otherUser;

    /**
     * The last message exchanged in the chat.
     */
    @Basic
    @Column(name = "last_message")
    private String lastMessage;

    /**
     * The timestamp of the last message.
     */
    @Basic
    @Column(name = "last_message_timestamp", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime lastMessageTimestamp;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatList)) return false;
        ChatList chatList = (ChatList) o;
        return chatlistid.equals(chatList.chatlistid) &&
                user.equals(chatList.user) &&
                otherUser.equals(chatList.otherUser) &&
                lastMessage.equals(chatList.lastMessage) &&
                lastMessageTimestamp.equals(chatList.lastMessageTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatlistid, user, otherUser, lastMessage, lastMessageTimestamp);
    }
}
