package com.spirit.application.util;

import com.spirit.application.entitiy.ChatList;
import com.spirit.application.entitiy.ChatMessage;
import com.spirit.application.entitiy.User;
import com.spirit.application.repository.ChatListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatHelper {

    private final ChatListRepository chatListRepository;

    @Autowired
    public ChatHelper(ChatListRepository chatListRepository) {
        this.chatListRepository = chatListRepository;
    }

    public void updateChatList(ChatMessage message) {
        updateSingleUserChatList(message.getSender(), message.getRecipient(), message);
        updateSingleUserChatList(message.getRecipient(), message.getSender(), message);
    }

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
