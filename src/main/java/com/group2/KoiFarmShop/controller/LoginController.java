package com.group2.KoiFarmShop.controller;

import com.group2.KoiFarmShop.dto.Content;
import com.group2.KoiFarmShop.dto.response.ApiReponse;
import com.group2.KoiFarmShop.dto.request.LoginGoogleRequest;
import com.group2.KoiFarmShop.dto.request.LoginRequest;
import com.group2.KoiFarmShop.service.AccountServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AccountServiceImp accountServiceImp;

    @PostMapping("/signin")
    @Operation(summary = "Đăng nhập", description = "-Nguyễn Hoàng Thiên")
    public ApiReponse<Content> login(@RequestBody LoginRequest loginRequest) {
        ApiReponse<Content> reponse = accountServiceImp.login(loginRequest);
        return reponse;
    }

    @PostMapping("/signingoogle")
    @Operation(summary = "Đăng nhập google", description = "-Nguyễn Hoàng Thiên")
    public ApiReponse<Content> logingoogle(@RequestBody LoginGoogleRequest loginGoogleRequest) {
        ApiReponse<Content> reponse = accountServiceImp.logingg(loginGoogleRequest);
        return reponse;
    }
}

