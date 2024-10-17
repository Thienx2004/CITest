package com.group2.KoiFarmShop.entity;

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
@Table(name = "Batch")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int batchID;

    @ManyToOne
    @JoinColumn(name = "categoryID")
    private Category category;


    private int quantity;
    private String origin;
    private String avgSize;

    private int age;

    private double price;
    private int purebred;
    private String health;
    private String temperature;
    private String water;
    private String pH;
    private String food;

    @OneToMany(mappedBy = "batch")
    private List<OrderDetail> orderDetails;

    private String batchImg;

    private int status = 1;

}
