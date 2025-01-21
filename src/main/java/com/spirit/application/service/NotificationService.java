package com.spirit.application.service;

import com.spirit.application.entitiy.User;
import com.spirit.application.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import com.spirit.application.entitiy.Notification;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Retrieves unread notifications for a user.
     * @param userId The ID of the user to fetch notifications for.
     * @return List of unread notifications.
     */
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUser_userIDAndIsRead(userId, false);
    }


    /**
     * Marks a notification as read.
     * @param notification The notification to mark as read.
     */
    public void markAsRead(Notification notification) {
        try {
            notification.setRead(true);
            notificationRepository.save(notification);  // Save the updated notification
        } catch (Exception e) {
            // Log the error or throw a custom exception
            throw new RuntimeException("Error marking notification as read", e);
        }
    }

    /**
     * Marks all unread notifications for a user as read.
     * @param userId The ID of the user whose notifications will be marked as read.
     */
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = getUnreadNotifications(userId);
        for (Notification notification : unreadNotifications) {
            markAsRead(notification);
        }
    }

    /**
     * Saves a notification (insert or update).
     * @param notification The notification to save.
     */
    public void saveNotification(Notification notification) {
        try {
            notificationRepository.save(notification);
        } catch (Exception e) {
            // Log the error or throw a custom exception
            throw new RuntimeException("Error saving notification", e);
        }
    }

    public long countUnreadNotifications(User currentUser) {
        return notificationRepository.findByUser_userIDAndIsRead(currentUser.getUserID(), false).size();
    }
}









