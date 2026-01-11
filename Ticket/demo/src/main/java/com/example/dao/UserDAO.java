package com.example.dao;

import com.example.entity.User;

import java.util.Optional;

public interface UserDAO extends GenericDAO<User, Long> {
    Optional<User> findByEmail(String email);
}