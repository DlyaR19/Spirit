package com.spirit.application.entitiy;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Getter
@Setter
@Table(name = "chatlist", schema = "public")
public class ChatList implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatlistid", length = 64, nullable = false)
    private Long chatlistid;

    @ManyToOne
    @JoinColumn(name = "userid")
    @Getter
    private User user;

    @ManyToOne
    @JoinColumn(name = "other_userid")
    @Getter
    private User otherUser;

    @Basic
    @Column(name = "last_message")
    private String lastMessage;

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
