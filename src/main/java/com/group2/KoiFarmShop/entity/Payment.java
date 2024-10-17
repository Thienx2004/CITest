package com.group2.KoiFarmShop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentID;

    private String transactionCode;
    private double amount;

    @OneToOne
    @JoinColumn(name = "orderID")
    private Orders order;

    @OneToOne
    @JoinColumn(name = "consignmentID")
    private Consignment consignment;

    private boolean status;
    private Date paymentDate;

}
