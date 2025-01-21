package com.spirit.application.repository;

import com.spirit.application.entitiy.Notification;
import com.spirit.application.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing Notification entities.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Finds all unread notifications for a specific user.
     * @param user the user whose notifications are being queried.
     * @return a list of unread notifications.
     */
    List<Notification> findByUserAndIsReadFalse(User user);

    /**
     * Finds all notifications for a specific user.
     * @param user the user whose notifications are being queried.
     * @return a list of all notifications.
     */
    List<Notification> findByUser(User user);

    List<Notification> findByUser_userIDAndIsRead(Long userId, boolean isRead);
}

