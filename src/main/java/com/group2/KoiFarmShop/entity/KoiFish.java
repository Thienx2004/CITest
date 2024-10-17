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
@Table(name = "KoiFish")
public class KoiFish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int koiID;

    private String origin;
    private boolean gender;
    private int age;
    private double size;
    private String personality;
//    private String situation;
    private double price;
    private String koiImage;
    private int status;
    private int purebred;
    private String health;
    private String temperature;
    private String water;
    private String pH;
    private String food;
    /*
    1_Còn Hàng
    2_Đã bán
    3_Ký gửi
    4_Chờ duyệt đơn ký gửi
    */


    @OneToOne(mappedBy = "koiFish",cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Certificate certificate;

    @OneToMany(mappedBy = "koiFish")
    private List<Consignment> consignment;

    @ManyToOne
    @JoinColumn(name = "categoryid")
    private Category category;

    @OneToMany(mappedBy = "koiFish")
    private List<OrderDetail> orderDetail;

}

