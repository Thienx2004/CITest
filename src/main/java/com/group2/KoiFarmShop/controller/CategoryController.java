package com.group2.KoiFarmShop.controller;


import com.group2.KoiFarmShop.dto.response.ApiReponse;
import com.group2.KoiFarmShop.dto.response.CategoryHomeReponse;
import com.group2.KoiFarmShop.dto.response.CategoryReponse;
import com.group2.KoiFarmShop.service.CategoryServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class CategoryController {

    @Autowired
    CategoryServiceImp categoryServiceImp;

    @GetMapping("/homepage")
    public ApiReponse<CategoryHomeReponse> getHomepage(@RequestParam int pageNum, @RequestParam int pageSize) {

        CategoryHomeReponse categoryHomeReponse = categoryServiceImp.getListHomeCategory(pageNum, pageSize);
        ApiReponse apiReponse = new ApiReponse();
        apiReponse.setData(categoryHomeReponse);
        return apiReponse;
    }

    @GetMapping("/getListCategory")
    public ApiReponse<List<CategoryReponse>> getListAllCate() {

        List<CategoryReponse> categoryReponseList = categoryServiceImp.getAllCategories();
        ApiReponse apiReponse = new ApiReponse();
        apiReponse.setData(categoryReponseList);
        return apiReponse;
    }
}
