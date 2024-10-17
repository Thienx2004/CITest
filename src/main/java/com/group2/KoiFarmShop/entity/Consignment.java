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
@Table(name = "Consignment")
public class Consignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int consignmentID;

    @ManyToOne
    @JoinColumn(name = "accountID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "koiID", nullable = true)
    private KoiFish koiFish;

    private boolean consignmentType; // true/false for Sale/Care
    private double agreedPrice;

    @Temporal(TemporalType.DATE)
    private Date consignmentDate;

    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;

    private String notes;
    private String phoneNumber;
    private int status;     //1 = Pending, 2 = Confirmed (đã duyệt), 3 = Reject, 4 = Pending Payment, 5 = Expired
    private boolean online;

    private int duration; // Thời gian ký gửi: 1, 3 hoặc 6 tháng
    private double serviceFee;

    @OneToOne(mappedBy = "consignment")
    private Payment payment;

//    @OneToMany(mappedBy = "consignment")
//    private List<OrderDetail> orderDetails;

}
