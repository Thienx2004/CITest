package com.group2.KoiFarmShop.controller;

import com.group2.KoiFarmShop.dto.PaymentDTO;
import com.group2.KoiFarmShop.dto.response.ApiReponse;
import com.group2.KoiFarmShop.entity.Payment;
import com.group2.KoiFarmShop.repository.PaymentRepository;
import com.group2.KoiFarmShop.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @Autowired
    private final PaymentRepository paymentRepository;
    @Value("${APP_DOMAIN}")
    private String domain;
    @GetMapping("/vn-pay")
    @Operation(summary = "Thanh toán", description = "-Nguyễn Hoàng Thiên")
    public ApiReponse<PaymentDTO.VNPayResponse> pay(HttpServletRequest request, @RequestParam Double amount, @RequestParam String bankCode, @RequestParam boolean type) {
        return ApiReponse.<PaymentDTO.VNPayResponse>builder().statusCode(200).message("Thanh cong").data(paymentService.createVnPayPayment(request,type)).build();

    }

    @Operation(summary = "Trả về kết quả Thanh toán", description = "-Nguyễn Hoàng Thiên")
    @GetMapping("/vn-pay-callback")
    public void vnPayCallback(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        String responseCode = params.get("vnp_ResponseCode");

        if ("00".equals(responseCode)) {
            // Giao dịch thành công
            String paymentCode = params.get("vnp_TransactionNo");
            Payment payment = new Payment();
            payment.setPaymentDate(new Date());
            payment.setAmount(Double.parseDouble(params.get("vnp_Amount")));
            payment.setStatus(false);
            payment.setTransactionCode(paymentCode);
            paymentRepository.save(payment);
            response.sendRedirect(domain+"/thank-you?paymentStatus=1&type=true&paymentCode=" + paymentCode); // Đường dẫn đến trang "Cảm ơn"
        } else {
            // Giao dịch không thành công
            response.sendRedirect(domain+"/payment-fail?paymentStatus=0"); // Đường dẫn đến trang "Thanh toán thất bại"
        }
    }
    @Operation(summary = "Trả về kết quả Thanh toán", description = "-Nguyễn Hoàng Thiên")
    @GetMapping("/vn-pay-callback-consignment")
    public void vnPayCallbackConsignment(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        String responseCode = params.get("vnp_ResponseCode");

        if ("00".equals(responseCode)) {
            // Giao dịch thành công
            String paymentCode = params.get("vnp_TransactionNo");
            Payment payment = new Payment();
            payment.setPaymentDate(new Date());
            payment.setAmount(Double.parseDouble(params.get("vnp_Amount")));
            payment.setStatus(false);
            payment.setTransactionCode(paymentCode);
            paymentRepository.save(payment);
            response.sendRedirect(domain+"/thank-you?paymentStatus=1&type=false&paymentCode=" + paymentCode); // Đường dẫn đến trang "Cảm ơn"
        } else {
            // Giao dịch không thành công
            response.sendRedirect(domain+"/payment-fail?paymentStatus=0"); // Đường dẫn đến trang "Thanh toán thất bại"
        }
    }
}