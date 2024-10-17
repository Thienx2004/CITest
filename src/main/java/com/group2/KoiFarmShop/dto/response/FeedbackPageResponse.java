package com.group2.KoiFarmShop.dto.response;

import lombok.Builder;

import java.util.List;
@Builder
public class FeedbackPageResponse {
    private int pageNum;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    List<FeedbackResponse> feedbackResponses;
}
