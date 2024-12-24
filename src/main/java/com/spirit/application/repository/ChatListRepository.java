package com.spirit.application.repository;

import com.spirit.application.entitiy.ChatList;
import com.spirit.application.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatListRepository extends JpaRepository<ChatList, Long> {
    List<ChatList> findByUserOrderByLastMessageTimestampDesc(User user);

    Optional<ChatList> findByUserAndOtherUser(User user, User otherUser);
}
