package org.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.store.dto.ContactCreateDTO;
import org.store.model.Contact;
import org.store.model.User;
import org.store.repository.ContactRepository;
import org.store.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    public Contact createContact(ContactCreateDTO contactCreateDTO) {
        // Retrieve the associated User entity (if required)
        // Assuming the User entity is linked with the Contact
        User user = userRepository.findById(contactCreateDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + contactCreateDTO.getUserId()));

        // Create a new Contact entity and set its properties from the DTO
        Contact contact = new Contact();
        contact.setPhoneNumber(contactCreateDTO.getPhoneNumber());
        contact.setEmail(contactCreateDTO.getEmail());  // Setting the email from the DTO
        contact.setAddress(contactCreateDTO.getAddress());
        contact.setUser(user); // Associate the contact with the user

        return contactRepository.save(contact);
    }

    public Contact updateContact(Long id, ContactCreateDTO contactUpdateDTO) {
        // Find the existing contact
        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found with id: " + id));

        // Update the contact's fields
        existingContact.setPhoneNumber(contactUpdateDTO.getPhoneNumber());
        existingContact.setEmail(contactUpdateDTO.getEmail());
        existingContact.setAddress(contactUpdateDTO.getAddress());
        // Note: Do not change the user association here

        // Save the updated contact
        return contactRepository.save(existingContact);
    }

    public void deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new IllegalArgumentException("Contact not found with id: " + id);
        }
        contactRepository.deleteById(id);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    // Newly added method to get a contact by ID
    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public List<Contact> getContactsByUserId(Long userId) {
        return contactRepository.findAllByUserId(userId);
    }
}
