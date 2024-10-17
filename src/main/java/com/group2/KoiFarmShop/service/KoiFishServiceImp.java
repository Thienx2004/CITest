package com.group2.KoiFarmShop.service;

import com.group2.KoiFarmShop.dto.response.KoiFishDetailReponse;
import com.group2.KoiFarmShop.dto.response.KoiFishPageResponse;
import com.group2.KoiFarmShop.dto.response.KoiFishReponse;
import com.group2.KoiFarmShop.dto.request.KoiRequest;
import com.group2.KoiFarmShop.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface KoiFishServiceImp {
    public KoiFishPageResponse getAllKoiFish(int page, int pageSize);
    public KoiFishPageResponse getKoiByCategory(Category category, int page,int pageSize);
    public KoiFishDetailReponse getKoiFishById(int id);
    public KoiFishDetailReponse addKoiFish(KoiRequest koiFish) throws IOException;


    public KoiFishDetailReponse updateKoiFish(int id, KoiRequest koiRequest) throws IOException;
    public KoiFishPageResponse filterKoiFish(String categoryID,String maxSize,String minSize, String gender, String age, String minPrice, String maxPrice, String origin, int page, int pageSize,String sortField, String sortDirection,String sortField2,String sortDirection2,String purebred);
    public List<KoiFishDetailReponse> compareKoiFish(int koiFishId1, int koiFishId2);
    public KoiFishDetailReponse updateKoiImg(MultipartFile file, int id) throws IOException;
    public void changeKoiFishStatus(int id, int status);
    public KoiFishPageResponse getFishByStatus(int page, int pageSize,int status);

    }
