package com.group2.KoiFarmShop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Healthcare")
public class Healthcare {

    @Id
    @Column(name = "koi_ID")
    private int id;

    private String healthStatus;
    private String growthStatus;
    private String careEnvironment;
    private String note;
    @Temporal(TemporalType.DATE)
    private Date consignmentDate;
    @OneToOne()
    @MapsId
    @JoinColumn(name = "koi_ID")
    private KoiFish koiFish;

}
