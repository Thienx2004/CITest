package com.group2.KoiFarmShop.controller;

import com.group2.KoiFarmShop.dto.MailBody;
import com.group2.KoiFarmShop.dto.response.ApiReponse;
import com.group2.KoiFarmShop.entity.Account;
import com.group2.KoiFarmShop.entity.VerificationToken;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.repository.AccountRepository;
import com.group2.KoiFarmShop.repository.ForgotPasswordRepositoryI;
import com.group2.KoiFarmShop.repository.VerificationTokenRepository;
import com.group2.KoiFarmShop.service.EmailService;
import com.group2.KoiFarmShop.ultils.ChangePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/login")
public class ForgotPasswordController {

    private final EmailService emailService;
    private final AccountRepository accountRepository;
    private final ForgotPasswordRepositoryI forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public ForgotPasswordController(AccountRepository accountRepository, EmailService emailService, ForgotPasswordRepositoryI forgotPasswordRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //send mail for otp
    @PostMapping("/forgotPassword/{email}")
    public ApiReponse<String> verifyEmail(@PathVariable String email) {
        ApiReponse apiReponse = new ApiReponse();
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.INVALIDACCOUNT));

        int otp = otpGen();

        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("Mã OTP của bạn là: " + otp + ". OTP sẽ hết hạn trong vòng 2 phút.")
                .subject("Mã xác nhận OTP")
                .build();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(String.valueOf(otp));
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(2)); // Hết hạn sau 10 phút
        verificationToken.setAccount(account);
        emailService.sendSimpleMess(mailBody);
        verificationTokenRepository.save(verificationToken);
        apiReponse.setData("Đã gửi OTP");
        return apiReponse;
    }

    ;
//    @Autowired
//    private JWTUltilsHelper jwtUltilsHelper;
//    @PostMapping("/otp/{otp}/{email}")
//        public ApiReponse verifyOTP(@PathVariable Integer otp, @PathVariable String email) {
//        ApiReponse apiReponse = new ApiReponse();
//
//        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.INVALIDACCOUNT));
//
//        ForgotPassword fp = forgotPasswordRepository.findByOptAndAccount(otp, account).orElseThrow(() -> new AppException(ErrorCode.INVALIDOTP));
//
//        if(fp.getExpirationTime().before(Date.from(Instant.now()))){
//            forgotPasswordRepository.deleteById(fp.getFpid());
//            throw new AppException(ErrorCode.INVALIDOTP);
//        }
//
//        apiReponse.setMessage("OTP hợp lệ");
//
//        account.setOTPcheck("true");
//
//        String tokenOTP = jwtUltilsHelper.generateTokenForOTP(account);
//        apiReponse.setData(tokenOTP);
//        return apiReponse;
//    }

    @PostMapping("/changePassword/{email}")
    public ApiReponse<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                    @PathVariable String email) {
        ApiReponse apiReponse = new ApiReponse();

        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            throw new AppException(ErrorCode.PASSWORDINVALID);
        }

        String encodedPassword = passwordEncoder.encode(changePassword.password());
        accountRepository.updatePassword(email, encodedPassword);
        apiReponse.setData("Đổi mật khẩu thành công!");
        return apiReponse;
    }

    private Integer otpGen() {
        Random rand = new Random();
        return rand.nextInt(100_000, 999_999);
    }
}
