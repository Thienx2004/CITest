package com.group2.KoiFarmShop.controller;

import com.group2.KoiFarmShop.dto.response.ApiReponse;
import com.group2.KoiFarmShop.repository.AccountRepository;
import com.group2.KoiFarmShop.repository.VerificationTokenRepository;
import com.group2.KoiFarmShop.service.AccountService;
import com.group2.KoiFarmShop.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VerificationController {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AuthenticationService authenticationService;


    @PostMapping("/verify-otp")
    public ApiReponse<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        ApiReponse apiReponse = accountService.verifyOTP(email, otp);


        return apiReponse;
    }

    @PostMapping("/resend-otp")
    public ApiReponse<String> resendOtp(@RequestParam String email) {
        ApiReponse apiReponse = accountService.resendOTP(email);


        return apiReponse;
    }

}
