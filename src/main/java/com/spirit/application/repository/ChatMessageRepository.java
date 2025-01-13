package com.spirit.application.repository;

import com.spirit.application.entitiy.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository for managing ChatMessage entities.
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * Finds chat messages exchanged between two users, ordered by timestamp in ascending order.
     *
     * @param userId the ID of the first User.
     * @param otherUserId the ID of the second User.
     * @return a list of ChatMessage entities.
     */
    @Query("SELECT m FROM ChatMessage m WHERE " +
            "(m.sender.userID = :userId AND m.recipient.userID = :otherUserId) OR " +
            "(m.sender.userID = :otherUserId AND m.recipient.userID = :userId) " +
            "ORDER BY m.timestamp ASC")
    List<ChatMessage> findChatMessagesBetweenUsers(
            @Param("userId") long userId,
            @Param("otherUserId") long otherUserId
    );
}
