package com.spirit.application.service;

import com.spirit.application.dto.ChatMessageDTO;
import com.spirit.application.entitiy.ChatMessage;
import com.spirit.application.entitiy.Notification;
import com.spirit.application.entitiy.User;
import com.spirit.application.repository.ChatMessageRepository;
import com.spirit.application.repository.NotificationRepository;
import com.spirit.application.repository.UserRepository;
import com.spirit.application.util.ChatHelper;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Service class for handling chat messages and user interactions.
 */
@Service
@Transactional
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final List<Consumer<ChatMessageDTO>> messageConsumers = new ArrayList<>();
    private final ChatHelper chatHelper;
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final NotificationRepository notificationRepository;


    @Autowired
    public ChatService(ChatMessageRepository chatMessageRepository, UserRepository userRepository, ChatHelper chatHelper, NotificationService notificationService, NotificationRepository notificationRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.chatHelper = chatHelper;
        this.notificationRepository = notificationRepository;
    }

    /**
     * Adds a message listener to be notified of new messages.
     * @param listener the listener to be added
     */
    public void addMessageListener(Consumer<ChatMessageDTO> listener) {
        messageConsumers.add(listener);
    }

    /**
     * Removes a message listener.
     * @param listener the listener to be removed
     */
    public void removeMessageListener(Consumer<ChatMessageDTO> listener) {
        messageConsumers.remove(listener);
    }

    /**
     * Sends a chat message from one user to another.
     * @param senderId   the ID of the sender
     * @param receiverId the ID of the receiver
     * @param content    the message content
     * @return the saved chat message
     */
    public ChatMessage sendMessage(long senderId, long receiverId, String content) {

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setRecipient(receiver);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now(ZoneId.systemDefault()));

        ChatMessage savedMessage = chatMessageRepository.save(message);
        chatHelper.updateChatList(savedMessage);
        ChatMessageDTO chatMessageDTO = ChatMessageDTO.fromMessage(savedMessage);

        VaadinSession currentSession = VaadinSession.getCurrent();
        CompletableFuture.runAsync(() -> {
            if (currentSession != null) {
                currentSession.access(() -> notifyListeners(chatMessageDTO));
            }
        });
        createNotificationForRecipient(receiver, sender);
        getUnreadNotifications(receiver.getUserID());
        return savedMessage;
    }

    /**
     * Notifies all message listeners of a new message.
     * @param message the message to notify listeners of
     */
    private void notifyListeners(ChatMessageDTO message) {
        for (Consumer<ChatMessageDTO> listener : messageConsumers) {
            try {
                listener.accept(message);
            } catch (Exception e) {
                logger.error("Error in notifying listener: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Retrieves the chat history between two users.
     * @param userId      the ID of the first user
     * @param otherUserId the ID of the second user
     * @return a list of chat messages between the two users
     */
    public List<ChatMessage> getChatHistory(long userId, long otherUserId) {
        User user1 = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(otherUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return chatMessageRepository.findChatMessagesBetweenUsers(user1.getUserID(), user2.getUserID());
    }


    /**
     * Creates a notification for the recipient of a chat message.
     * @param receiver the recipient of the message
     * @param sender   the sender of the message
     */
    private void createNotificationForRecipient(User receiver, User sender) {
        Notification notification = new Notification();
        notification.setUser(receiver); // Set the recipient of the notification
        notification.setMessage("Neue Nachricht von " + sender.getUsername()); // Message content
        notification.setRead(false); // Mark as unread
        notificationRepository.save(notification);// Save the notification
        getUnreadNotifications(receiver.getUserID());

    }

    /**
     * Retrieves unread notifications for a user.
     * @param userId The ID of the user to fetch notifications for.
     */
    private void getUnreadNotifications(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUser_userIDAndIsRead(userId, false);
        System.out.println("Unread notifications for user " + userId + ": " + unreadNotifications.size());

    }
}
