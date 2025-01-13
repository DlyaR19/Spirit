package com.spirit.application.util;

import com.spirit.application.entitiy.ChatList;
import com.spirit.application.entitiy.ChatMessage;
import com.spirit.application.entitiy.User;
import com.spirit.application.repository.ChatListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Helper class for managing chat-related operations, such as updating chat lists.
 */
@Component
public class ChatHelper {

    private final ChatListRepository chatListRepository;

    @Autowired
    public ChatHelper(ChatListRepository chatListRepository) {
        this.chatListRepository = chatListRepository;
    }

    /**
     * Updates the chat list for both the sender and the recipient of a message.
     * @param message the chat message to process
     */
    public void updateChatList(ChatMessage message) {
        updateSingleUserChatList(message.getSender(), message.getRecipient(), message);
        updateSingleUserChatList(message.getRecipient(), message.getSender(), message);
    }

    /**
     * Updates the chat list for a specific user and their interaction with another user.
     * @param user       the user whose chat list is being updated
     * @param otherUser  the other user involved in the chat
     * @param message    the latest message between the users
     */
    private void updateSingleUserChatList(User user, User otherUser, ChatMessage message) {
        ChatList chatList = chatListRepository
                .findByUserAndOtherUser(user, otherUser)
                .orElse(new ChatList());

        chatList.setUser(user);
        chatList.setOtherUser(otherUser);
        chatList.setLastMessage(message.getContent());
        chatList.setLastMessageTimestamp(message.getTimestamp());

        chatListRepository.save(chatList);
    }
}
