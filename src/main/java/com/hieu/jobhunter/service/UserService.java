package com.hieu.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hieu.jobhunter.domain.User;
import com.hieu.jobhunter.domain.dto.UserDto;
import com.hieu.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void handleSaveUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }

        userRepository.save(user);
    }

    public void handleDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public List<User> handleFetchAllUser() {
        return this.userRepository.findAll();
    }

    public Optional<User> handleFindUserById(Long id) {
        return this.userRepository.findById(id);
    }

    public Optional<User> handleFindUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User registerDTOtoUser(UserDto userDto, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setFullName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setUsername(userDto.getUsername()); // bắt buộc
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // encode password
        user.setRole(User.Role.valueOf(userDto.getRole())); // map từ string sang enum
        return user;
    }

    public Optional<User> handleFinduserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

}