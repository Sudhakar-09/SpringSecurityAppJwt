package com.thissecurity.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thissecurity.Entities.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User,Integer>{

    Optional<User> findByEmail(String email);

}
