package com.group2.KoiFarmShop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private int accountID;
    private int[] koiFishs;
    private int[] batchs;
    private double[] price;
    private int[] quantity;
    private double totalPrice;
    private String promoCode;
    //private int consignmentID;

}
