package com.group2.KoiFarmShop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderID;

    @ManyToOne
    @JoinColumn(name = "accountID")
    private Account account;

    @Temporal(TemporalType.TIMESTAMP)
    private Date order_date;
    private double totalPrice;

    @OneToMany(mappedBy = "orders")
    private List<OrderDetail> orderDetails;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    //true: đang giao
    //false: hoàn tất đơn hàng
    private boolean status = true;

    // Getters and Setters
}
