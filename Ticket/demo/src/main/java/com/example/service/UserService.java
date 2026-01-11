package com.example.service;

import com.example.dao.UserDAO;
import com.example.dao.UserDAOImpl;
import com.example.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {

    private final UserDAO userDAO = new UserDAOImpl();

    public void createUser(String name, String email, String password, User.Role role) {
        if (userDAO.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User(name, email, password, role);
        userDAO.save(user);
    }

    public Optional<User> authenticate(String email, String password) {
        Optional<User> user = userDAO.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userDAO.findById(id);
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }

    public void deleteUser(User user) {
        userDAO.delete(user);
    }

    public void deleteUserById(Long id) {
        userDAO.findById(id).ifPresent(userDAO::delete);
    }

    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userDAO.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!user.getPassword().equals(oldPassword)) {
                return false;
            }
            if (newPassword.length() < 6) {
                throw new IllegalArgumentException("New password must be at least 6 characters");
            }
            user.setPassword(newPassword);
            userDAO.update(user);
            return true;
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public void updateProfile(Long userId, String name, String email) {
        Optional<User> userOpt = userDAO.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Check if email is taken by another user
            Optional<User> existingUser = userDAO.findByEmail(email);
            if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
                throw new IllegalArgumentException("Email already in use by another account");
            }
            user.setName(name);
            user.setEmail(email);
            userDAO.update(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public void changeUserRole(Long userId, User.Role newRole) {
        Optional<User> userOpt = userDAO.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(newRole);
            userDAO.update(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public List<User> getClientUsers() {
        return userDAO.findAll().stream()
                .filter(u -> u.getRole() == User.Role.CLIENT)
                .collect(Collectors.toList());
    }

    public List<User> getAdminUsers() {
        return userDAO.findAll().stream()
                .filter(u -> u.getRole() == User.Role.ADMIN)
                .collect(Collectors.toList());
    }

    public long countUsers() {
        return userDAO.findAll().size();
    }

    public long countClients() {
        return getClientUsers().size();
    }
}