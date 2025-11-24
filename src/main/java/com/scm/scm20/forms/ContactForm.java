package com.scm.scm20.forms;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {

    // ----------- BASIC INFORMATION --------------

    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 50, message = "Name must be between 2 to 50 characters.")
    private String name;


    @NotBlank(message = "Email is required..")
    @Email(message = "Please enter a valid email address.")
    @Size(max = 100, message = "Email cannot exceed 100 characters.")
    private String email;


    // Indian phone validation + optional
    @Pattern(
            regexp = "^(?:\\+?91)?[6-9][0-9]{9}$",
            message = "Phone number must be valid (10 digits, starts with 6–9)."
    )
    private String phoneNumber;


    @Size(max = 255, message = "Address cannot exceed 255 characters.")
    private String address;


    @Size(max = 2048, message = "Description cannot exceed 2048 characters.")
    private String description;


    // ----------- SOCIAL LINKS --------------

    @URL(message = "Facebook link must be a valid URL.")
    @Size(max = 255, message = "Facebook URL cannot exceed 255 characters.")
    private String facebookLink;


    @URL(message = "LinkedIn link must be a valid URL.")
    @Size(max = 255, message = "LinkedIn URL cannot exceed 255 characters.")
    private String linkedInLink;


    // ----------- PROFILE PICTURE --------------

    // Will validate manually (size/type/resolution)

    private MultipartFile contactImage;

    private  String picture;

}
