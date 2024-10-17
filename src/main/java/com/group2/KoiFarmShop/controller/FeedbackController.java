package com.group2.KoiFarmShop.controller;

import com.group2.KoiFarmShop.dto.request.FeedbackRequest;
import com.group2.KoiFarmShop.dto.response.ApiReponse;
import com.group2.KoiFarmShop.dto.response.FeedbackPageResponse;
import com.group2.KoiFarmShop.dto.response.FeedbackResponse;
import com.group2.KoiFarmShop.entity.Feedback;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.service.FeedbackService;
import com.group2.KoiFarmShop.service.FeedbackServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackServiceImp feedbackServiceImp;

    // Lấy tất cả feedbacks
    @GetMapping("/all")
    public ApiReponse<FeedbackPageResponse> getAllFeedbacks(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        if (page <= 0 || pageSize <= 0) {
            throw new AppException(ErrorCode.INVALIDNUMBER);
        }
        FeedbackPageResponse feedbackPageResponse = feedbackServiceImp.getAllFeedback(page, pageSize);
        return ApiReponse.<FeedbackPageResponse>builder().data(feedbackPageResponse).statusCode(200).build();
    }

    @GetMapping("/feedbackByRating")
    public ApiReponse<FeedbackPageResponse> getFeedbackByRating(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("rating") int rating) {
        if (page <= 0 || pageSize <= 0) {
            throw new AppException(ErrorCode.INVALIDNUMBER);
        }
        FeedbackPageResponse feedbackPageResponse = feedbackServiceImp.getFeedbackByRating(page, pageSize, rating);
        return ApiReponse.<FeedbackPageResponse>builder().data(feedbackPageResponse).statusCode(200).build();
    }

    @GetMapping("/filterFeedback")
    public ApiReponse<FeedbackPageResponse> getFilterFeedback(
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam(required = false) String sortField1,
            @RequestParam(required = false) String sortDirection1,
            @RequestParam(required = false) String sortField2,
            @RequestParam(required = false) String sortDirection2) {
        if (page <= 0 || pageSize <= 0) {
            throw new AppException(ErrorCode.INVALIDNUMBER);
        }
        FeedbackPageResponse feedbackPageResponse = feedbackServiceImp.filterFeedback(page, pageSize, sortField1, sortDirection1, sortField2, sortDirection2);
        return ApiReponse.<FeedbackPageResponse>builder().data(feedbackPageResponse).statusCode(200).build();
    }

    // Lấy feedback theo ID
    @GetMapping("/{id}")
    public ApiReponse<FeedbackResponse> getFeedbackById(@PathVariable int id) {
        FeedbackResponse feedbackResponse = feedbackServiceImp.getFeedbackById(id);
        return ApiReponse.<FeedbackResponse>builder().data(feedbackResponse).statusCode(200).build();
    }

    // Tạo mới feedback
    @PostMapping("/add")
    public ApiReponse<FeedbackResponse> createFeedback(@RequestBody FeedbackRequest feedback) {
        FeedbackResponse feedbackResponse = feedbackServiceImp.addFeedback(feedback);
        return ApiReponse.<FeedbackResponse>builder().data(feedbackResponse).message("Thêm feedback thành công").statusCode(200).build();
    }

    // Cập nhật feedback
    @PutMapping("/update/{id}")
    public ApiReponse<FeedbackResponse> updateFeedback(@PathVariable int id, @RequestBody FeedbackRequest feedbackDetails) {
        FeedbackResponse feedbackResponse = feedbackServiceImp.updateFeedback(feedbackDetails, id);
        return ApiReponse.<FeedbackResponse>builder().data(feedbackResponse).message("Cập nhật thành công").statusCode(200).build();
    }

    // Xóa feedback
    @DeleteMapping("/delete/{id}")
    public ApiReponse<FeedbackResponse> deleteFeedback(@PathVariable int id) {
        feedbackServiceImp.deleteFeedback(id);
        return ApiReponse.<FeedbackResponse>builder().statusCode(200).message("Xóa thành công").build();
    }
}
