package com.group2.KoiFarmShop.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryReponse {

    private int orderId;
    private int accountId;
    private String fullName;
    private String transactionCode;
    private Date createdDate;
    private double totalPrice;
    private boolean status;
    private int paymentId;
    //private List<OrderDetailReponse> orderDetails;
}
