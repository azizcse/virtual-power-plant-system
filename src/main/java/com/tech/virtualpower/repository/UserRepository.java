package com.tech.virtualpower.repository;

import com.tech.virtualpower.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(@NotBlank String email);
    Boolean existsByEmail(@NotBlank String email);
}
