package com.group2.KoiFarmShop.service;


import com.group2.KoiFarmShop.dto.response.ApiReponse;
import com.group2.KoiFarmShop.dto.request.LoginGoogleRequest;
import com.group2.KoiFarmShop.dto.request.LoginRequest;
import com.group2.KoiFarmShop.dto.request.AccountCreationDTO;
import com.group2.KoiFarmShop.entity.Account;

public interface AccountServiceImp {


    public Account createAccount(AccountCreationDTO accountCreationDTO);
    public ApiReponse login(LoginRequest loginRequest);

    public ApiReponse logingg(LoginGoogleRequest loginGoogleRequest);

    public String generateOTP();
    public ApiReponse<String> resendOTP(String email);
    ApiReponse<String> verifyOTP(String email, String otp);

}
