package com.group2.KoiFarmShop.service;

import com.group2.KoiFarmShop.dto.response.PaginReponse;
import com.group2.KoiFarmShop.dto.response.PromotionDTO;

import java.util.Date;

public interface PromotionServiceImp {

    String createPromotion(String description, Date startDate, Date endDate, double discountRate);
    PromotionDTO assignPromotionToAccount(String promoCode, int accountId);
    PaginReponse<PromotionDTO> getAllPromotion(int pageNo, int pageSize);
    String updatePromotion(PromotionDTO promotion);
    String deletePromotion(int promotionId);
}
