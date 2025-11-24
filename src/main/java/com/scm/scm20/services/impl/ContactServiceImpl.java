package com.scm.scm20.services.impl;

import com.scm.scm20.controllers.ContactController;
import com.scm.scm20.entities.Contact;
import com.scm.scm20.entities.User;
import com.scm.scm20.helper.ResourceNotFoundException;
import com.scm.scm20.repositories.ContactRepo;
import com.scm.scm20.services.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private Logger logger= LoggerFactory.getLogger(ContactController.class);
    @Autowired
    private ContactRepo contactRepo;
    @Override
    public Contact save(Contact contact) {
     return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        var contactOld=contactRepo.findById(contact.getId()).orElseThrow(()->new ResourceNotFoundException("Cant found "));
//        contactOld.setName(contact.getName());
        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setLinkedInLink(contact.getLinkedInLink());
        contactOld.setFacebookLink(contact.getFacebookLink());
        contactOld.setPicture(contact.getPicture());
      //  contactOld.setcloudinaryImagePublicId(contact.getcloudinaryImagePublicId());
        contactOld.setLinks(contact.getLinks());
        return  contactRepo.save(contactOld);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
       return contactRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No Contacts Found.."));
    }

    @Override
    public void delete(String id) {
        var contact=contactRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No Contacts Found.."));
       contactRepo.delete(contact);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword,int page,int size,String sortField,String sortDirection) {
        Sort sort=sortDirection.equals("desc")?Sort.by(sortField).descending():Sort.by(sortField).ascending();
        var pageable= PageRequest.of(page,size,sort);
       // logger.info(contactRepo.findByNameContaining(nameKeyword,pageable));
        return contactRepo.findByNameContainingIgnoreCase(nameKeyword,pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword,int page,int size,String sortField,String sortDirection) {
        Sort sort=sortDirection.equals("desc")?Sort.by(sortField).descending():Sort.by(sortField).ascending();
        var pageable= PageRequest.of(page,size,sort);

        return contactRepo.findByEmailContainingIgnoreCase(emailKeyword,pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword,int page,int size,String sortField,String sortDirection) {
        Sort sort=sortDirection.equals("desc")?Sort.by(sortField).descending():Sort.by(sortField).ascending();
        var pageable= PageRequest.of(page,size,sort);
        return contactRepo.findByPhoneNumberContainingIgnoreCase(phoneNumberKeyword,pageable);
    }

//    @Override
//    public List<Contact> search(String name, String email, String PhoneNumber) {
//        return List.of();
//    }

    @Override
    public List<Contact> getByUserId(String userId) {
      return  contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user,int page,int size,String sortBy,String direction) {
        Sort sort=direction.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        var pageable= PageRequest.of(page,size,sort);
       return contactRepo.findByUser(user,pageable);
    }

    @Override
    public Contact toggleFavorite(String contactId) {
        Contact contact = contactRepo.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        contact.setFavorite(!contact.isFavorite());
        return contactRepo.save(contact);
    }
}
