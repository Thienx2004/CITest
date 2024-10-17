package com.group2.KoiFarmShop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
    @Table(name = "Account")
    public class Account {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int accountID;

        @Column(nullable = false, unique = true)
        private String email;

        @Column
        private String password;

        private String fullName;
        private int loyaltyPoints = 0;
        private boolean isVerified = false;
        private String address;
        private String phone;


        @ManyToOne
        @JoinColumn(name = "roleID")
        @JsonManagedReference

        private Role role;

        @OneToMany(mappedBy = "account")
        private List<Orders> orders;

        @OneToMany(mappedBy = "account")
        private List<Feedback> feedbacks;

        @OneToMany(mappedBy = "account")
        private List<VerificationToken> verificationTokens;

        @ManyToOne
        @JoinColumn(name = "promotionID")
        private Promotion promotion;

        private String avatar;
        private boolean status = true;

    public Account(String fullname, String email, String password) {
        this.fullName = fullname;
        this.email = email;
        this.password = password;
    }
}
