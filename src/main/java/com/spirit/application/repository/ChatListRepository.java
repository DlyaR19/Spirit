package com.spirit.application.repository;

import com.spirit.application.entitiy.ChatList;
import com.spirit.application.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing ChatList entities.
 */
@Repository
public interface ChatListRepository extends JpaRepository<ChatList, Long> {

    /**
     * Finds a list of ChatList entities for a given User, ordered by the last message timestamp in descending order.
     *
     * @param user the User entity.
     * @return a list of ChatList entities.
     */
    List<ChatList> findByUserOrderByLastMessageTimestampDesc(User user);

    /**
     * Finds a ChatList entity by a User and another User.
     *
     * @param user the primary User.
     * @param otherUser the other User.
     * @return an Optional containing the ChatList entity if found.
     */
    Optional<ChatList> findByUserAndOtherUser(User user, User otherUser);
}
