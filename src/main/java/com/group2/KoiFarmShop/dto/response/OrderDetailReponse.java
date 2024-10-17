package com.group2.KoiFarmShop.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailReponse {

    private int orderDetailId;
    private String categoryName;
    private int koiFishId;
    private int koiAge;
    private boolean gender;
    private double koiSize;
    private String koiImg;
    private int batchId;
    private String avgSize;
//    private double koiPrice;
//    private double batchPrice;
    private double price;
    private String batchImg;
    private int quantity;
    private boolean type;
}
