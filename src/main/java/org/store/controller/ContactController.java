package org.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.store.dto.ContactCreateDTO;
import org.store.model.Contact;
import org.store.service.ContactService;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@Tag(name = "Contact", description = "The Contact API. Contains all the operations that can be performed on a contact.")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    @Operation(summary = "Get a list of all contacts")
    @SecurityRequirement(name = "ApiKeyAuth")
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all contacts for a specific user")
    @ApiResponse(responseCode = "404", description = "User not found")
    @SecurityRequirement(name = "ApiKeyAuth")
    public ResponseEntity<List<Contact>> getContactsByUserId(@PathVariable Long userId) {
        List<Contact> contacts = contactService.getContactsByUserId(userId);
        if (contacts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contacts);
    }


    @PostMapping("/user/{userId}")
    @Operation(summary = "Create a new contact for a user")
    @ApiResponse(responseCode = "201", description = "Contact created")
    @SecurityRequirement(name = "ApiKeyAuth")
    public ResponseEntity<Contact> createContact(@PathVariable Long userId, @RequestBody ContactCreateDTO contactCreateDTO) {
        contactCreateDTO.setUserId(userId); // Ensure the DTO has a setUserId method
        Contact newContact = contactService.createContact(contactCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newContact);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a contact")
    @ApiResponse(responseCode = "200", description = "Contact updated successfully")
    @ApiResponse(responseCode = "404", description = "Contact not found")
    @SecurityRequirement(name = "ApiKeyAuth")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody ContactCreateDTO contactUpdateDTO) {
        try {
            Contact updatedContact = contactService.updateContact(id, contactUpdateDTO);
            return ResponseEntity.ok(updatedContact);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a contact")
    @ApiResponse(responseCode = "204", description = "Contact deleted successfully")
    @ApiResponse(responseCode = "404", description = "Contact not found")
    @SecurityRequirement(name = "ApiKeyAuth")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        try {
            contactService.deleteContact(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
