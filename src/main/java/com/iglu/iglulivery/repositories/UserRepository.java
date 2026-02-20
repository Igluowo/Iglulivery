package com.iglu.iglulivery.repositories;

import com.iglu.iglulivery.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
