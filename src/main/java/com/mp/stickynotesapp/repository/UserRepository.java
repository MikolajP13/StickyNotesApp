package com.mp.stickynotesapp.repository;

import com.mp.stickynotesapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFirstName(String firstName);
    Optional<User> findByUsername(String username);
    Optional<User> findByFirstNameAndLastNameAndJobID(String firstName, String lastName, String jobID);
    Optional<List<User>> findAllByTeamNameAndCreatorId(String teamName, Long managerId);
    Optional<List<User>> findAllByWorkCountry(String workCountry);
}
