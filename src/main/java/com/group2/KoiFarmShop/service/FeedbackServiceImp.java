package com.group2.KoiFarmShop.service;


import com.group2.KoiFarmShop.dto.request.FeedbackRequest;
import com.group2.KoiFarmShop.dto.response.FeedbackPageResponse;
import com.group2.KoiFarmShop.dto.response.FeedbackResponse;
import com.group2.KoiFarmShop.entity.Feedback;

import java.util.List;

public interface FeedbackServiceImp {
    public FeedbackResponse addFeedback(FeedbackRequest feedback);
    public FeedbackPageResponse getAllFeedback(int page, int pageSize);
    public FeedbackResponse getFeedbackById(int id);
    public FeedbackResponse updateFeedback(FeedbackRequest feedback,int id);
    public void deleteFeedback(int id);
    public FeedbackPageResponse filterFeedback(int page, int pageSize,String sortField, String sortDirection,String sortField2,String sortDirection2);
    public FeedbackPageResponse getFeedbackByRating(int rating, int page, int pageSize);
}
