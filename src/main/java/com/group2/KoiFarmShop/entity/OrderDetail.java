package com.group2.KoiFarmShop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OrderDetail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderDetailID;

    @ManyToOne
    @JoinColumn(name = "orderID")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "koiID")
    private KoiFish koiFish;

    @ManyToOne
    @JoinColumn(name = "batchID")
    private Batch batch;

//    @ManyToOne
//    @JoinColumn(name = "consignmentID")
//    private Consignment consignment;

    private boolean type; // true/false for Batch/Fish
    private int quantity;
    private double price;

    // Getters and Setters
}
