package com.group2.KoiFarmShop.dto.response;


import com.group2.KoiFarmShop.entity.Account;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDTO {

    private int promotionID;

    private String promoCode;
    private String desciption;
    private Date startDate;

    private Date endDate;

    private double discountRate;

    private List<Account> accounts;

    private boolean status = true;
}
