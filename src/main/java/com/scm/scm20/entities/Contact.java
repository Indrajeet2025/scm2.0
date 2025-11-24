package com.scm.scm20.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contacts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = "user")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Contact {

    @Id
    @EqualsAndHashCode.Include
    @UuidGenerator
    @Column(length = 36)
    private String id;

    private String name;
    private String email;
    private String phoneNumber;

//    public void setCloudinaryImagePublicId(String cloudinaryImagePublicId) {
//        this.cloudinaryImagePublicId = cloudinaryImagePublicId;
//    }

    private String address;
    private String picture;

    @Column(length = 2048)
    private String description;

    private boolean favorite = false;

    private String facebookLink;
    private String linkedInLink;
    private String cloudinaryImagePublicId;

//    public boolean isFavorite() {
//        return favorite;
//    }
//
//    public void setFavorite(boolean favorite) {
//        this.favorite = favorite;
//    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_contact_user")
    )
    @JsonIgnore
    private User user;


    @OneToMany(mappedBy = "contact",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<SocialLink> links=new ArrayList<>();
}
