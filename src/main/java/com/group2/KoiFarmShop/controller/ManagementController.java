package com.group2.KoiFarmShop.controller;

import com.google.protobuf.Api;
import com.group2.KoiFarmShop.dto.AccountDTO;
import com.group2.KoiFarmShop.dto.request.AccountCreateRequest;
import com.group2.KoiFarmShop.dto.request.AccountUpdateStatusRequest;
import com.group2.KoiFarmShop.dto.request.CreateCategoryRequest;
import com.group2.KoiFarmShop.dto.response.*;
import com.group2.KoiFarmShop.entity.Account;
import com.group2.KoiFarmShop.entity.Role;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.service.AccountService;
import com.group2.KoiFarmShop.service.CategoryService;
import com.group2.KoiFarmShop.service.CategoryServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("manage")
public class ManagementController {

    @Autowired
    private AccountService accountService;
    @Autowired
    CategoryServiceImp categoryServiceImp;
    @Autowired
    CategoryService categoryService;


    @GetMapping("/allAcount")
    @Operation(summary = "Lấy toàn bộ tài khoản", description = "")

    public ApiReponse<AccountPageRespone> getAllAccounts(@RequestParam int page, @RequestParam int pageSize) {
        if (page <= 0 || pageSize <= 0) {
            throw new AppException(ErrorCode.INVALIDNUMBER);
        }
        AccountPageRespone accountPageRespone = accountService.getAllAccounts(page, pageSize);
        return ApiReponse.<AccountPageRespone>builder().data(accountPageRespone).statusCode(200).build();
    }

    @PutMapping("/updateStatus/{id}")
    @Operation(summary = "Cập nhật trạng thái tài khoản", description = "")

    public ApiReponse<AccountDTO> changeStatus(@PathVariable int id) {
        AccountDTO accountDTO = accountService.updateAccountStatus(id);
        return ApiReponse.<AccountDTO>builder().data(accountDTO).statusCode(200).build();
    }

    @PostMapping("/createAccount")
    @Operation(summary = "Tạo tài khoản", description = "")

    public ApiReponse<AccountCreateRespone> createAccount (@RequestBody AccountCreateRequest request ) {
        AccountCreateRespone accountCreateRespone = accountService.createAccount(request);

        return ApiReponse.<AccountCreateRespone>builder().data(accountCreateRespone).statusCode(200).build();
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm tài khoản theo email",description = "")
    public ApiReponse<AccountDTO> getAccountByEmail(@RequestParam String email) {
        AccountDTO accountDTO = accountService.searchByEmail(email);
        return ApiReponse.<AccountDTO>builder().data(accountDTO).statusCode(200).build();
    }

    @GetMapping("/getAllCategory")
    public ApiReponse<CategoryPageResponse> getListAllCate(@RequestParam int pageNum, @RequestParam int pageSize) {

        CategoryPageResponse categoryReponseList = categoryServiceImp.getAllCategories(pageNum,pageSize);
        ApiReponse apiReponse = new ApiReponse();
        apiReponse.setData(categoryReponseList);
        return apiReponse;
    }

    @PostMapping("/addCategory")
    public ApiReponse<CreateCategoryRespone> createCategory(@RequestParam String cateName,@RequestParam  String description,@RequestParam  boolean status,@RequestParam  MultipartFile file) throws IOException {
        CreateCategoryRespone createCategoryRespone = categoryService.addCategory(cateName,description,status,file);
        return ApiReponse.<CreateCategoryRespone>builder().data(createCategoryRespone).statusCode(200).build();
    }

    @PutMapping("/changeStatus/{id}")
    @Operation(summary = "Update status cho category", description = "")
    public ApiReponse<CategoryReponse> updateStatus(@PathVariable int id) {
        CategoryReponse categoryReponse = categoryService.updateStatus(id);
        return ApiReponse.<CategoryReponse>builder().data(categoryReponse).statusCode(200).build();
    }
    @PutMapping("/updateCategory/{id}")
    public ApiReponse<CreateCategoryRespone> updateCategory(@ModelAttribute CreateCategoryRequest createCategoryRequest,@PathVariable int id,@RequestParam  MultipartFile file) throws IOException {
        CreateCategoryRespone respone = categoryService.updateCategory(id, createCategoryRequest,file);
        return ApiReponse.<CreateCategoryRespone>builder().data(respone).statusCode(200).build();
    }
}
