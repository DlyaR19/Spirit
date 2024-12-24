package com.spirit.application.service;

import com.spirit.application.dto.ChatListDTO;
import com.spirit.application.entitiy.User;
import com.spirit.application.repository.ChatListRepository;
import com.spirit.application.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatListService {
    private final ChatListRepository chatListRepository;
    private final UserRepository userRepository;
    private final List<Consumer<ChatListDTO>> updateListeners =
            Collections.synchronizedList(new ArrayList<>());

    public ChatListService(ChatListRepository chatListRepository,
                           UserRepository userRepository) {
        this.chatListRepository = chatListRepository;
        this.userRepository = userRepository;
    }

    public List<ChatListDTO> getChatsForUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return chatListRepository.findByUserOrderByLastMessageTimestampDesc(user)
                .stream()
                .map(ChatListDTO::fromChatList)
                .collect(Collectors.toList());
    }

    public void addUpdateListener(Consumer<ChatListDTO> listener) {
        updateListeners.add(listener);
    }

    public void removeUpdateListener(Consumer<ChatListDTO> listener) {
        updateListeners.remove(listener);
    }
}
