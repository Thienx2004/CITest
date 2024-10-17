package com.group2.KoiFarmShop.controller;


import com.group2.KoiFarmShop.dto.response.ApiReponse;
import com.group2.KoiFarmShop.dto.response.PaginReponse;
import com.group2.KoiFarmShop.dto.response.PromotionDTO;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.service.PromotionService;
import com.group2.KoiFarmShop.service.PromotionServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/promotion")
public class PromotionController {

    @Autowired
    private PromotionServiceImp promotionService;

    @PostMapping("/create")
    public ApiReponse<String> createPromotion(
            @RequestParam String description,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam double discountRate) {

        String success = promotionService.createPromotion(description, startDate, endDate, discountRate);
        ApiReponse<String> apiReponse = new ApiReponse<>();
        apiReponse.setData(success);
        return apiReponse;
    }

    @PostMapping("/apply")
    public ApiReponse<PromotionDTO> applyPromotionToAccount(
            @RequestParam String promoCode,
            @RequestParam int accountId) {

        ApiReponse<PromotionDTO> apiReponse = new ApiReponse<>();
        PromotionDTO promotionDTO = promotionService.assignPromotionToAccount(promoCode, accountId);
        if (promotionDTO != null) {
            apiReponse.setData(promotionDTO);
            return apiReponse;
        }
        throw new AppException(ErrorCode.PROMOTION_INVALID);
    }

    @GetMapping("/getAllPromotion")
    public ApiReponse<PaginReponse<PromotionDTO>> getAllPromotion(@RequestParam int page, @RequestParam int pageSize) {

        ApiReponse<PaginReponse<PromotionDTO>> apiReponse = new ApiReponse<>();
        PaginReponse<PromotionDTO> paginReponse = promotionService.getAllPromotion(page, pageSize);
        apiReponse.setData(paginReponse);
        return apiReponse;
    }

    @PutMapping("/updatePromo")
    public ApiReponse<String> updatePromotion(@RequestBody PromotionDTO promotionDTO) {
        ApiReponse<String> apiReponse = new ApiReponse<>();
        apiReponse.setData(new String(promotionService.updatePromotion(promotionDTO)));
        return apiReponse;
    }

    @DeleteMapping("/deletePromo")
    public ApiReponse<String> deletePromotion(@RequestParam int promotionId) {
        ApiReponse<String> apiReponse = new ApiReponse<>();
        String success = promotionService.deletePromotion(promotionId);
        apiReponse.setData(success);
        return apiReponse;
    }
}
