package com.spirit.application.dto;

import com.spirit.application.entitiy.ChatList;
import com.spirit.application.entitiy.User;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for representing a Chat List.
 */

@Getter
@Setter
public class ChatListDTO {
    private User user;
    private User otherUser;
    private String lastMessage;
    private LocalDateTime lastMessageTimestamp;

    /**
     * Converts a ChatList entity into a ChatListDTO.
     *
     * @param chatList the ChatList entity to convert.
     * @return a new ChatListDTO instance.
     */
    public static ChatListDTO fromChatList(ChatList chatList) {
        ChatListDTO chatListDTO = new ChatListDTO();
        chatListDTO.setUser(chatList.getUser());
        chatListDTO.setOtherUser(chatList.getOtherUser());
        chatListDTO.setLastMessage(chatList.getLastMessage());
        chatListDTO.setLastMessageTimestamp(chatList.getLastMessageTimestamp());
        return chatListDTO;
    }

    /**
     * Default constructor.
     */
    public ChatListDTO() {
    }

    /**
     * Constructor to initialize ChatListDTO with provided details.
     *
     * @param user the primary user.
     * @param otherUser the other user in the chat.
     * @param lastMessage the last message exchanged.
     * @param lastMessageTimestamp the timestamp of the last message.
     */
    public ChatListDTO(User user, User otherUser, String lastMessage, LocalDateTime lastMessageTimestamp) {
        this.user = user;
        this.otherUser = otherUser;
        this.lastMessage = lastMessage;
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    @Override
    public String toString() {
        return "ChatListDTO{" +
                "otherUser=" + otherUser +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastMessageTimestamp='" + lastMessageTimestamp + '\'' +
                '}';
    }
}
