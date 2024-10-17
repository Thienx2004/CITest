package com.group2.KoiFarmShop.controller;


import com.group2.KoiFarmShop.dto.request.*;
import com.group2.KoiFarmShop.dto.response.*;
import com.group2.KoiFarmShop.entity.Consignment;
import com.group2.KoiFarmShop.entity.KoiFish;
import com.group2.KoiFarmShop.entity.Orders;
import com.group2.KoiFarmShop.entity.Payment;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.repository.PaymentRepository;
import com.group2.KoiFarmShop.service.ConsignmentServiceImp;
import com.group2.KoiFarmShop.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/consignment")
public class ConsignmentController {

    @Autowired
    private ConsignmentServiceImp consignmentService;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private EmailService emailService;

    @PostMapping("/createConsignment")
    public ApiReponse<String> createConsignment(@RequestParam int accountId, @RequestParam MultipartFile koiImg,
                                                @RequestParam String origin,
                                                @RequestParam boolean gender,
                                                @RequestParam int age,
                                                @RequestParam double size,
                                                @RequestParam String personality,
                                                @RequestParam double price,
                                                @RequestParam String food,
                                                @RequestParam String health,
                                                @RequestParam String ph,
                                                @RequestParam String temperature,
                                                @RequestParam String water,
                                                @RequestParam int pureBred,
                                                @RequestParam int categoryId,
                                                @RequestParam String name,
                                                @RequestParam MultipartFile certImg,
                                                @RequestParam String notes,
                                                @RequestParam String phoneNumber,
                                                @RequestParam boolean consignmentType,
                                                @RequestParam int duration,
                                                @RequestParam double serviceFee,
                                                @RequestParam boolean online
    ) {

        ApiReponse apiReponse = new ApiReponse();
        apiReponse.setData(consignmentService.createConsignment(accountId, koiImg, origin, gender, age, size, personality, price, food, health, ph, temperature, water,
                pureBred, categoryId, name, certImg, notes, phoneNumber, consignmentType, duration, serviceFee, online));

        return apiReponse;
    }

    @PutMapping("/approve/{consignmentID}")
    public ApiReponse<String> approveConsignment(@PathVariable int consignmentID) throws MessagingException {

        ApiReponse apiReponse = new ApiReponse();
        apiReponse.setData(consignmentService.approveConsignment(consignmentID));

        return apiReponse;
    }

    @PutMapping("/reject/{consignmentID}")
    public ApiReponse<String> rejectConsignment(@PathVariable int consignmentID, @RequestParam String rejectionReason) {
        ApiReponse apiReponse = new ApiReponse();
        apiReponse.setData(consignmentService.rejectConsignment(consignmentID, rejectionReason));

        return apiReponse;
    }

    @GetMapping("/getAllConsignment")
    public ApiReponse<PaginReponse<ConsignmentResponse>> getAllConsignmentForCustomer(@RequestParam int pageNo, @RequestParam int pageSize, @RequestParam int accountId) {
        ApiReponse apiReponse = new ApiReponse();
        PaginReponse<ConsignmentResponse> consignmentResponse = consignmentService.getAllConsignmentForCustomer(pageNo, pageSize, accountId);
        apiReponse.setData(consignmentResponse);
        return apiReponse;
    }

    @GetMapping("/getAllConsignmentManagement")
    public ApiReponse<PaginReponse<ConsignmentResponse>> getAllConsignmentForStaff(@RequestParam int pageNo, @RequestParam int pageSize) {
        ApiReponse apiReponse = new ApiReponse();
        PaginReponse<ConsignmentResponse> consignmentResponse = consignmentService.getAllConsignmentForStaff(pageNo, pageSize);
        apiReponse.setData(consignmentResponse);
        return apiReponse;
    }

    @GetMapping("/consignmentDetail/{consignmentId}")
    public ApiReponse<ConsignmentDetailResponse> getConsignmentDetail(@PathVariable int consignmentId) {
        ApiReponse<ConsignmentDetailResponse> response = new ApiReponse<>();
        response.setData(consignmentService.getConsignmentDetail(consignmentId));
        return response;
    }

    @PostMapping("/processPayment")
    public ApiReponse<String> processPayment(@RequestParam int consignmentId, @RequestParam String transactionCode) throws MessagingException {
        ApiReponse<String> resp = new ApiReponse<>();
        Payment payment = paymentRepository.findPaymentByTransactionCode(transactionCode).orElseThrow(() -> new AppException(ErrorCode.PAYMENT_FAILED));
        if (!payment.isStatus()) {
            Consignment success = consignmentService.processPayment(consignmentId, true);
            if (success == null) {
                throw new AppException(ErrorCode.PAYMENT_FAILED);
            }
            payment.setConsignment(success);
            payment.setStatus(true);
            paymentRepository.save(payment);
            // Gửi email xác nhận sau khi đơn hàng đã lưu thành công
            emailService.sendOrderConfirmationEmail(payment.getConsignment().getAccount().getEmail(), payment.getTransactionCode());
            resp.setData("Thanh toán thành công! Cá đã được đưa lên bán.");
            return resp;
        } else {
            throw new AppException(ErrorCode.TRANSACTION_INVALID);
        }
    }

    @DeleteMapping("/deleteConsignment")
    public ApiReponse<String> deleteConsignment(@RequestParam int consignmentId) {
        ApiReponse<String> apiReponse = new ApiReponse<>();
        apiReponse.setData(consignmentService.deleteConsignment(consignmentId));

        return apiReponse;
    }

    @PutMapping("/update/{id}/{koiId}")
    public ApiReponse<ConsignmentDetailResponse> updateConsignment(@PathVariable int id, @PathVariable int koiId, @ModelAttribute ConsignmentKoiRequest consignmentRequest) throws MessagingException, IOException {

        ConsignmentDetailResponse consignmentDetailResponse = consignmentService.updateConsignment(consignmentRequest, id, koiId);
        return ApiReponse.<ConsignmentDetailResponse>builder().data(consignmentDetailResponse).build();
    }

    @PutMapping("/update/{Id}")
    public ApiReponse<HealthcareResponse> updateHealth(@PathVariable int Id, @RequestBody ConsignmentKoiCare consignmentKoiCare) throws MessagingException, IOException {

        HealthcareResponse healthcareResponse = consignmentService.updateHealth(Id,consignmentKoiCare);
        return ApiReponse.<HealthcareResponse>builder().data(healthcareResponse).build();
    }
}
