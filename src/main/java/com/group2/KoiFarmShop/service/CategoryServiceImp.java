package com.group2.KoiFarmShop.service;

import com.group2.KoiFarmShop.dto.request.CreateCategoryRequest;
import com.group2.KoiFarmShop.dto.response.CategoryHomeReponse;
import com.group2.KoiFarmShop.dto.response.CategoryPageResponse;
import com.group2.KoiFarmShop.dto.response.CategoryReponse;
import com.group2.KoiFarmShop.dto.response.CreateCategoryRespone;
import com.group2.KoiFarmShop.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryServiceImp {

    public List<CategoryReponse> getAllCategories();
    public CategoryPageResponse getAllCategories(int pageNum, int pageSize);
    public CreateCategoryRespone addCategory( String cateName,String description,boolean status, MultipartFile file) throws IOException;
    public Category getCategoryById(int id);
    public String updateCategory(Category category);
    public String deleteCategory(int id);

    public CategoryHomeReponse getListHomeCategory(int pageNum, int pageSize);
}
