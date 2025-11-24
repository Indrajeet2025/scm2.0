package com.scm.scm20.controllers;

import com.scm.scm20.entities.Contact;
import com.scm.scm20.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController
{

    //get contact
    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts/{contactId}")
    public Contact getContact(@PathVariable String contactId)
    {
        return  contactService.getById(contactId);
    }

    @PatchMapping("/contacts/{contactId}/favorite")
    public Contact toggleFavorite(@PathVariable String contactId) {
        return contactService.toggleFavorite(contactId);
    }

}
