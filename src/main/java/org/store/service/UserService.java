// UserService.java

package org.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.store.model.User;
import org.store.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(User userUpdateRequest) {
        User existingUser = userRepository.findById(userUpdateRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userUpdateRequest.getId()));

        // Update fields except the password
        existingUser.setEmail(userUpdateRequest.getEmail());
        existingUser.setUsername(userUpdateRequest.getUsername());

        // Store the password in plain text only for testing (Not recommended for production!)
        String newPassword = userUpdateRequest.getPassword();
        if (newPassword != null && !newPassword.isEmpty()) {
            existingUser.setPassword(newPassword);
        }

        // Do not update createdAt or other fields that should not change

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
