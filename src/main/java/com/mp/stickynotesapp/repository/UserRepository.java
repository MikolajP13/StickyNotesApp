package com.mp.stickynotesapp.repository;

import com.mp.stickynotesapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<List<User>> findAllByTeamName(String teamName);
}
