package com.spirit.application.dto;

import com.spirit.application.entitiy.ChatMessage;
import com.spirit.application.entitiy.User;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for representing a Chat Message.
 */
@Getter
@Setter
public class ChatMessageDTO {

    private long chatMessageID;
    private User sender;
    private User recipient;
    private String content;
    private LocalDateTime timestamp;

    /**
     * Default constructor.
     */
    public ChatMessageDTO() {
    }

    /**
     * Converts a ChatMessage entity into a ChatMessageDTO.
     *
     * @param message the ChatMessage entity to convert.
     * @return a new ChatMessageDTO instance.
     */
    public static ChatMessageDTO fromMessage(ChatMessage message) {
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setChatMessageID(message.getChatMessageID());
        chatMessageDTO.setSender(message.getSender());
        chatMessageDTO.setRecipient(message.getRecipient());
        chatMessageDTO.setContent(message.getContent());
        chatMessageDTO.setTimestamp(message.getTimestamp());
        return chatMessageDTO;
    }

    @Override
    public String toString() {
        return "ChatMessageDTO{" +
                "chatMessageID=" + chatMessageID +
                ", sender=" + sender +
                ", recipient=" + recipient +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

}
