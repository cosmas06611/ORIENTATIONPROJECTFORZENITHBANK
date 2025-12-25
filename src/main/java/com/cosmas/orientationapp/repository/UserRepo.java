package com.cosmas.orientationapp.repository;

import com.cosmas.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, String> {
    Users findByUsername(String username);
}
