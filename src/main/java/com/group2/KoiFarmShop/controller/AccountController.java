package com.group2.KoiFarmShop.controller;

import com.group2.KoiFarmShop.dto.response.AccountReponse;
import com.group2.KoiFarmShop.dto.response.ApiReponse;
import com.group2.KoiFarmShop.dto.response.ProfileRespone;
import com.group2.KoiFarmShop.dto.request.AccountCreationDTO;
import com.group2.KoiFarmShop.dto.request.PasswordRequest;
import com.group2.KoiFarmShop.dto.request.ProfileRequest;
import com.group2.KoiFarmShop.entity.Account;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.repository.AccountRepository;
import com.group2.KoiFarmShop.service.AccountService;
import com.group2.KoiFarmShop.service.AccountServiceImp;
import com.group2.KoiFarmShop.service.AuthenticationService;
import com.group2.KoiFarmShop.service.FileServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountServiceImp accountServiceImp;
    @Autowired
    private FileServiceImp fileServiceImp;
    @Autowired
    private AccountService accountService;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    private AccountRepository accountRepository;


    @PostMapping("/register")
    public ApiReponse<AccountReponse> createAccount(@RequestBody AccountCreationDTO accountCreationDTO) {

        ApiReponse<AccountReponse> reponse = new ApiReponse<>();
        Account account = new Account();
        account = accountServiceImp.createAccount(accountCreationDTO);
        AccountReponse accountReponse = new AccountReponse();
        accountReponse.setFullName(account.getFullName());
        accountReponse.setEmail(account.getEmail());
        accountReponse.setPassword(account.getPassword());
        reponse.setMessage("Đăng kí thành công! Kiểm tra email của bạn để xác thực.");


        reponse.setData(accountReponse);
        return reponse;
//            accountServiceImp.createAccount(accountCreationDTO);
//            ApiReponse apiReponse = new ApiReponse();
        //return reponse;
    }

    @PostMapping("/savefile")
    public ApiReponse<Boolean> saveFile(@RequestParam MultipartFile file) {
        ApiReponse<Boolean> reponse = new ApiReponse<>();
        boolean success = fileServiceImp.saveFile(file);
        reponse.setData(success);
        return reponse;
    }

    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<?> getFile(@PathVariable String filename) {
        Resource resource = fileServiceImp.loadFile(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @Operation(summary = "Lấy profile theo email", description = "")
    @GetMapping("profile/{email}")
    ApiReponse<ProfileRespone> getProfile(@PathVariable String email) {
        ProfileRespone profileRespone = accountService.getProfile(email);

        if (profileRespone != null) {
            return ApiReponse.<ProfileRespone>builder().data(profileRespone).build();
        } else {
            throw new AppException(ErrorCode.INVALIDACCOUNT);
        }

    }

    @Operation(summary = "Cập nhật thông tin người dùng", description = "")
    @PutMapping("update/updateProfile/{id}")
    public ApiReponse<ProfileRespone> updateProfile(
            @RequestBody ProfileRequest profileRequest,
            @PathVariable int id,

            HttpServletRequest request) throws IOException {

//        // Lấy và kiểm tra JWT token
//        String token = authenticationService.extractTokenFromRequest(request);
//
//        // Kiểm tra tài khoản tồn tại
//        Account account=accountRepository.findByAccountID(id)
//                .orElseThrow(() -> new AppException(ErrorCode.INVALIDACCOUNT));
//
//       // Xác thực email từ token có khớp với email của tài khoản không
//        String emailFromToken = authenticationService.validateTokenByEmail(token);
//        if (!account.getEmail().equals(emailFromToken)) {
//            throw new AppException(ErrorCode.POWERLESS);
//        }

        ProfileRespone profileRespone = accountService.updateProfile(profileRequest, id);
        return ApiReponse.<ProfileRespone>builder()
                .data(profileRespone)
                .statusCode(200)
                .build();
    }

    @PutMapping("/updatePassword/{id}")
    public ApiReponse<ProfileRespone> updatePassword(@RequestBody PasswordRequest passwordRequest,
                                                     @PathVariable int id,
                                                     HttpServletRequest request) {

        // Lấy và kiểm tra JWT token
        String token = authenticationService.extractTokenFromRequest(request);

        // Kiểm tra tài khoản tồn tại
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALIDACCOUNT));

        // Xác thực email từ token có khớp với email của tài khoản không
        String emailFromToken = authenticationService.validateTokenByEmail(token);
        if (!account.getEmail().equals(emailFromToken)) {
            throw new AppException(ErrorCode.POWERLESS);
        }

        // Cập nhật mật khẩu và trả về kết quả
        ProfileRespone profileRespone = accountService.updatePassword(passwordRequest, id);
        return ApiReponse.<ProfileRespone>builder()
                .data(profileRespone)
                .statusCode(200)
                .build();
    }

    @PostMapping("/checkPassword/{id}")
    public ApiReponse<Boolean> checkPassword(@RequestBody PasswordRequest passwordRequest, @PathVariable int id) {
        Optional<Account> account = accountRepository.findById(id);
        if (!passwordEncoder.matches(passwordRequest.getPassword(), account.get().getPassword())) {
            throw new AppException(ErrorCode.PASSWORDINVALID);
        }
        return ApiReponse.<Boolean>builder()
                .data(true).message("Mật khẩu hợp lệ")
                .statusCode(200)
                .build();
    }

    @Operation(summary = "Cập nhật avatar", description = "")
    @PostMapping("profile/updateAvatar/{id}")
    public ApiReponse<ProfileRespone> updateAvatar(@RequestParam("file") MultipartFile file, @PathVariable int id) throws IOException {
        ProfileRespone profileRespone = accountService.updateAvatar(file, id);
        return ApiReponse.<ProfileRespone>builder()
                .data(profileRespone)
                .statusCode(200)
                .build();
    }

}
