package com.group2.KoiFarmShop.service;


import java.time.LocalDateTime;

import com.group2.KoiFarmShop.dto.request.FeedbackRequest;
import com.group2.KoiFarmShop.dto.response.FeedbackPageResponse;
import com.group2.KoiFarmShop.dto.response.FeedbackResponse;
import com.group2.KoiFarmShop.entity.Account;
import com.group2.KoiFarmShop.entity.Feedback;
import com.group2.KoiFarmShop.entity.KoiFish;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.repository.AccountRepository;
import com.group2.KoiFarmShop.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService implements FeedbackServiceImp {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public FeedbackResponse addFeedback(FeedbackRequest feedback) {

        Feedback feedbackEntity = new Feedback();
        Optional<Account> account = accountRepository.findById(feedback.getAccountId());
        feedbackEntity.setRating(feedback.getRating());
        feedbackEntity.setComment(feedback.getComment());
        feedbackEntity.setAccount(account.get());

        Feedback feedbackSaved = feedbackRepository.save(feedbackEntity);

        return FeedbackResponse.builder()
                .feedbackId(feedbackSaved.getFeedbackID())
                .account(feedbackSaved.getAccount())
                .rating(feedbackSaved.getRating())
                .comment(feedbackSaved.getComment())
                .feedbackDate(feedbackSaved.getDate())
                .build();
    }

    @Override
    public FeedbackPageResponse getAllFeedback(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Feedback> feedbackList = feedbackRepository.findAll(pageable);
        List<FeedbackResponse> feedbackResponseList = new ArrayList<>();
        for (Feedback feedback : feedbackList) {
            FeedbackResponse feedbackResponse = new FeedbackResponse();
            feedbackResponse.setFeedbackId(feedback.getFeedbackID());
            feedbackResponse.setAccount(feedback.getAccount());
            feedbackResponse.setRating(feedback.getRating());
            feedbackResponse.setComment(feedback.getComment());
            feedbackResponse.setFeedbackDate(feedback.getDate());
            feedbackResponseList.add(feedbackResponse);
        }

        return FeedbackPageResponse.builder()
                .pageNum(feedbackList.getNumber()+1)
                .totalPages(feedbackList.getTotalPages())
                .totalElements(feedbackList.getTotalElements())
                .pageSize(feedbackList.getSize())
                .feedbackResponses(feedbackResponseList)
                .build();
    }

    @Override
    public FeedbackResponse getFeedbackById(int id) {
        Optional<Feedback> feedback = feedbackRepository.findById(id);
        if (!feedback.isPresent()) {
            throw new AppException(ErrorCode.KOINOTFOUND);
        }
        return FeedbackResponse.builder()
                .feedbackId(feedback.get().getFeedbackID())
                .comment(feedback.get().getComment())
                .rating(feedback.get().getRating())
                .account(feedback.get().getAccount())
                .feedbackDate(feedback.get().getDate())
                .build();
    }

    @Override
    public FeedbackResponse updateFeedback(FeedbackRequest feedback,int id) {
            Optional<Feedback> feedbackOptional = feedbackRepository.findById(id);
            Optional<Account> account = accountRepository.findById(feedback.getAccountId());
            Feedback feedbackToUpdate = (Feedback) feedbackOptional.get();
            feedbackToUpdate.setFeedbackID(id);
            feedbackToUpdate.setComment(feedback.getComment());
            feedbackToUpdate.setRating(feedback.getRating());
            feedbackToUpdate.setRating(feedback.getRating());
            feedbackToUpdate.setAccount(account.get());

            Feedback updatedFeedback = feedbackRepository.save(feedbackToUpdate);
        return FeedbackResponse.builder()
                .feedbackId(updatedFeedback.getFeedbackID())
                .comment(updatedFeedback.getComment())
                .rating(updatedFeedback.getRating())
                .account(updatedFeedback.getAccount())
                .feedbackDate(updatedFeedback.getDate())
                .build();
    }

    @Override
    public void deleteFeedback(int id) {
        Optional<Feedback> feedbackOptional = feedbackRepository.findById(id);
        Feedback feedbackToUpdate = (Feedback) feedbackOptional.get();
        feedbackToUpdate.setFeedbackID(id);
        feedbackRepository.delete(feedbackToUpdate);

    }

    @Override
    public FeedbackPageResponse filterFeedback(int page, int pageSize,String sortField, String sortDirection,String sortField2,String sortDirection2) {
        // Xử lý sortField1 và sortDirection1
        if (sortField == null || sortField.isEmpty()) {
            sortField = "date";
        }
        if (sortDirection == null || sortDirection.isEmpty()) {
            sortDirection = "desc";
        }
        if (sortDirection.equals("1")) {
            sortDirection = "asc";
        } else if (sortDirection.equals("2")) {
            sortDirection = "desc";
        } else {
            sortDirection = "asc";
        }

        // Tạo danh sách các điều kiện sắp xếp
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.fromString(sortDirection), sortField)); // Điều kiện sort chính

        // Xử lý sortField2 và sortDirection2 (nếu có)
        if (sortField2 != null && !sortField2.isEmpty()) {
            if (sortDirection2 == null || sortDirection2.isEmpty()) {
                sortDirection2 = "asc";
            }
            if (sortDirection2.equals("1")) {
                sortDirection2 = "asc";
            } else if (sortDirection2.equals("2")) {
                sortDirection2 = "desc";
            } else {
                sortDirection2 = "asc";
            }
            // Thêm điều kiện sort thứ hai
            orders.add(new Sort.Order(Sort.Direction.fromString(sortDirection2), sortField2));
        }

        // Áp dụng danh sách sắp xếp vào Sort
        Sort sort = Sort.by(orders);
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
        Page<Feedback> feedbackList= feedbackRepository.findAll(pageable);
        List<FeedbackResponse> feedbackResponseList = new ArrayList<>();
        for (Feedback feedback : feedbackList) {
            FeedbackResponse feedbackResponse = new FeedbackResponse();
            feedbackResponse.setFeedbackId(feedback.getFeedbackID());
            feedbackResponse.setAccount(feedback.getAccount());
            feedbackResponse.setRating(feedback.getRating());
            feedbackResponse.setComment(feedback.getComment());
            feedbackResponse.setFeedbackDate(feedback.getDate());
            feedbackResponseList.add(feedbackResponse);

        }
        return FeedbackPageResponse.builder()
                .pageSize(feedbackList.getTotalPages())
                .totalElements(feedbackList.getTotalElements())
                .pageNum(feedbackList.getNumber()+1)
                .feedbackResponses(feedbackResponseList)
                .build();
    }


    @Override
    public FeedbackPageResponse getFeedbackByRating(int rating, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Feedback> feedbackList=feedbackRepository.findAllByRating(rating,pageable);
        List<FeedbackResponse> feedbackResponseList = new ArrayList<>();
        for (Feedback feedback : feedbackList) {
            FeedbackResponse feedbackResponse = new FeedbackResponse();
            feedbackResponse.setFeedbackId(feedback.getFeedbackID());
            feedbackResponse.setAccount(feedback.getAccount());
            feedbackResponse.setRating(feedback.getRating());
            feedbackResponse.setComment(feedback.getComment());
            feedbackResponse.setFeedbackDate(feedback.getDate());
            feedbackResponseList.add(feedbackResponse);
        }
        return FeedbackPageResponse.builder()
                .pageSize(feedbackList.getTotalPages())
                .totalElements(feedbackList.getTotalElements())
                .pageNum(feedbackList.getNumber()+1)
                .feedbackResponses(feedbackResponseList)
                .build();
    }
}
