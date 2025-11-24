package com.scm.scm20.services;

import com.scm.scm20.entities.Contact;
import com.scm.scm20.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService
{
    //save contacts logic
    Contact save(Contact contact);
    //update contacts logic
    Contact update(Contact contact);
    //get all contacts
    List<Contact>getAll();
    //get Contact by id
    Contact getById(String id);
    //delete contact
    void delete(String id);
    //Search Contact
    Page<Contact> searchByName(String nameKeyword,int page,int size,String sortField,String sortDirection);
   Page<Contact> searchByEmail(String emailKeyword,int page,int size,String sortField,String sortDirection);
    Page<Contact> searchByPhoneNumber(String phoneNumberKeyword,int page,int size,String sortField,String sortDirection);
    //get Contacts by userId
    List<Contact> getByUserId(String userId);
    Page<Contact> getByUser(User user, int page,int size,String sortField,String sortDirection);

    Contact toggleFavorite(String contactId);

}
