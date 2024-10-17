package com.group2.KoiFarmShop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int feedbackID;

    @ManyToOne
    @JoinColumn(name = "accountID")
    private Account account;

//    @ManyToOne
//    @JoinColumn(name = "batchID")
//    private Batch batch;

    private int rating;
    private String comment;
    private Date date=new Date();

}