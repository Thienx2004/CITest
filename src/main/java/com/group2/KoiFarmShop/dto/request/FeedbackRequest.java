package com.group2.KoiFarmShop.dto.request;

import com.group2.KoiFarmShop.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {
    private int feedbackId ;
    private int accountId;
    private String comment ;
    private String feedback ;
    private int rating ;
}
