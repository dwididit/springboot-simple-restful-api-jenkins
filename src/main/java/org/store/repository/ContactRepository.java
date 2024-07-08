package org.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.model.Contact;
import org.store.model.User;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    // Existing method to retrieve contacts by User entity
    List<Contact> findByUser(User user);

    // New method to retrieve contacts by user's ID
    List<Contact> findByUserId(Long userId);

    List<Contact> findAllByUserId(Long userId);

}
