package com.spirit.application.dto;

import com.spirit.application.entitiy.ChatList;
import com.spirit.application.entitiy.User;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ChatListDTO {
    private User user;
    private User otherUser;
    private String lastMessage;
    private LocalDateTime lastMessageTimestamp;

    public static ChatListDTO fromChatList(ChatList chatList) {
        ChatListDTO chatListDTO = new ChatListDTO();
        chatListDTO.setUser(chatList.getUser());
        chatListDTO.setOtherUser(chatList.getOtherUser());
        chatListDTO.setLastMessage(chatList.getLastMessage());
        chatListDTO.setLastMessageTimestamp(chatList.getLastMessageTimestamp());
        return chatListDTO;
    }

    public ChatListDTO() {
    }

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
