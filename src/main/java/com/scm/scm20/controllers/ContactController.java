package com.scm.scm20.controllers;

import com.scm.scm20.entities.Contact;
import com.scm.scm20.entities.User;
import com.scm.scm20.forms.ContactForm;
import com.scm.scm20.helper.AppConstants;
import com.scm.scm20.helper.Helper;
import com.scm.scm20.helper.Message;
import com.scm.scm20.helper.MessageType;
import com.scm.scm20.services.ContactService;
import com.scm.scm20.services.ImageService;
import com.scm.scm20.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/user/contacts")
public class ContactController
{
    private Logger logger= LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    // add contact page:handler
    @RequestMapping("/add")
    public String addContactView(Model model)
    {
        ContactForm contactForm=new ContactForm();
        //contactForm.setName("Saurabh");
        model.addAttribute("contactForm",contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute  ContactForm contactForm, BindingResult bindingResult, Authentication authentication, HttpSession httpSession)
    {
        if(bindingResult.hasErrors())
        {
            httpSession.setAttribute("message", Message.builder().content("Please correct the errors...").type(MessageType.red).build());
            return "user/add_contact";
        }

        String username= Helper.getEmailOfLoggedInUser(authentication);

         User user=userService.getUserByEmail(username);
        // process  img
        logger.info("file information: {}",contactForm.getContactImage().getOriginalFilename());
        // img upload code
        String filename= UUID.randomUUID().toString();
        String fileUrl=imageService.uploadImage(contactForm.getContactImage(),filename);
        //process the form
           //form->contact
        Contact contact=new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setFacebookLink(contactForm.getFacebookLink());
        contact.setUser(user);
        contact.setPicture(fileUrl);
        contact.setCloudinaryImagePublicId(filename);
       contactService.save(contact);
        System.out.println(contactForm);
        httpSession.setAttribute("message", Message.builder().content("Contact Saved...").type(MessageType.green).build());
        return "redirect:/user/contacts/add";
    }

    // view Contacts
    @RequestMapping("/view")
    public String viewContacts(
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
            @RequestParam(value = "direction",defaultValue = "ascending") String direction,
            Model model,Authentication authentication)
    {
       String username=Helper.getEmailOfLoggedInUser(authentication);
      User user= userService.getUserByEmail(username);
        Page<Contact> pageContact=contactService.getByUser(user,page,size,sortBy,direction);
//        pageContact.getNumber()

        model.addAttribute("pageContact",pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        return "user/contacts";
    }

    @RequestMapping("/search")
    public String searchHandler(
            @RequestParam("field") String field,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ascending") String direction,
            Model model
    ) {
        logger.info("field {} keyword {}", field, keyword);

        Page<Contact> pageContact = Page.empty();

        if (field.equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(keyword, page, size, sortBy, direction);
        } else if (field.equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(keyword, page, size, sortBy, direction);
        } else if (field.equalsIgnoreCase("phone")) {
            pageContact = contactService.searchByPhoneNumber(keyword, page, size, sortBy, direction);
        }
        logger.info("search result size: {}", pageContact.getTotalElements());
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", size);
        model.addAttribute("field", field);
        model.addAttribute("keyword", keyword);

        return "user/search";
    }

    @RequestMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") String cid)
    {
        contactService.delete(cid);
        logger.info("cid {} deleted...",cid);
        return "redirect:/user/contacts/view";
    }

    // update contact
    @GetMapping("/view/{cid}")
    public String updateContactFormView(@PathVariable("cid") String cid,Model model)
    {
        var contact=contactService.getById(cid);

        ContactForm contactForm=new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setFacebookLink(contact.getFacebookLink());
        contactForm.setPicture(contact.getPicture());
       // contactForm.setFavorite(contact.isFavorite());

        model.addAttribute("contactForm",contactForm);
        model.addAttribute("cid",cid);
        return "user/update_contact_view";
    }

    @PostMapping("/update/{cid}")
     public String updateContact(@PathVariable("cid") String cid,@ModelAttribute ContactForm contactForm,Model model,HttpSession httpSession)
     {
         // update contact
         Contact contact=contactService.getById(cid);
         contact.setId(cid);
         contact.setName(contactForm.getName());
         contact.setEmail(contactForm.getEmail());
         contact.setPhoneNumber(contactForm.getPhoneNumber());
         contact.setAddress(contactForm.getAddress());
         contact.setDescription(contactForm.getDescription());
         contact.setLinkedInLink(contactForm.getLinkedInLink());
         contact.setFacebookLink(contactForm.getFacebookLink());

         // Process img
        // contact.setCloudinaryImagePublicId(contactForm.getCloudinaryImagePublicId());
         if(contactForm.getContactImage()!=null&&!contactForm.getContactImage().isEmpty())
         {
             String fileName=UUID.randomUUID().toString();
             String imageURL=imageService.uploadImage(contactForm.getContactImage(),fileName);
             contact.setCloudinaryImagePublicId(fileName);
             contact.setPicture(imageURL);
             contactForm.setPicture(imageURL);
         }
        var updatedContact= contactService.update(contact);
        logger.info("updated Contact {}",updatedContact);
         httpSession.setAttribute("message", Message.builder().content("Contact updated..").type(MessageType.green).build());

         return "redirect:/user/contacts/view/"+cid;
     }


}
