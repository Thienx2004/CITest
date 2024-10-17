package com.group2.KoiFarmShop.dto.response;

import com.group2.KoiFarmShop.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponse {
    private int feedbackId ;
    private Account account;
    private String comment ;
    private String feedback ;
    private int rating ;
    private Date feedbackDate ;
}
