package com.spirit.application.repository;

import com.spirit.application.dto.UserDTO;
import com.spirit.application.entitiy.Bewertung;
import com.spirit.application.entitiy.Profil;
import com.spirit.application.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BewertungRepository extends JpaRepository<Bewertung, Long> {
    Bewertung findByUserAndRatedUser(User user, User ratedUser);
}
