package com.group2.KoiFarmShop.service;

import com.group2.KoiFarmShop.dto.response.PaginReponse;
import com.group2.KoiFarmShop.dto.response.PromotionDTO;
import com.group2.KoiFarmShop.entity.Account;
import com.group2.KoiFarmShop.entity.Promotion;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.repository.AccountRepository;
import com.group2.KoiFarmShop.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PromotionService implements PromotionServiceImp{

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public String createPromotion(String description, Date startDate, Date endDate, double discountRate) {

        List<Promotion> existingPromotions = promotionRepository.findAll();

        for (Promotion existingPromotion : existingPromotions) {
            if (datesOverlap(startDate, endDate, existingPromotion.getStartDate(), existingPromotion.getEndDate())) {
                throw new AppException(ErrorCode.PROMOTION_DATE_OVERLAP);
            }
        }

        Promotion promotion = new Promotion();

        promotion.setPromoCode(generatePromoCode());
        promotion.setDesciption(description);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setDiscountRate(discountRate / 100);
        promotion.setStatus(true);

        promotionRepository.save(promotion);
        return "Tạo mã giảm giá thành công!";
    }

    // Hàm kiểm tra xem 2 khoảng thời gian có giao nhau không
    private boolean datesOverlap(Date newStartDate, Date newEndDate, Date existingStartDate, Date existingEndDate) {
        return !(newEndDate.before(existingStartDate) || newStartDate.after(existingEndDate));
    }

    @Override
    public PromotionDTO assignPromotionToAccount(String promoCode, int accountId) {

        Account account = accountRepository.findByAccountID(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALIDACCOUNT));
        Promotion promotion = promotionRepository.findByPromoCode(promoCode)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_INVALID));

        if(isPromoCodeValidForAccount(promotion, account)) {
            PromotionDTO promotionDTO = new PromotionDTO();
            promotionDTO.setPromoCode(promoCode);
            promotionDTO.setPromotionID(promotion.getPromotionID());
            promotionDTO.setStartDate(promotion.getStartDate());
            promotionDTO.setEndDate(promotion.getEndDate());
            promotionDTO.setDiscountRate(promotion.getDiscountRate());
            promotionDTO.setDesciption(promotion.getDesciption());
            promotionDTO.setStatus(promotion.isStatus());

            return promotionDTO;
        }
        return null;
    }

    @Override
    public PaginReponse<PromotionDTO> getAllPromotion(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("promotionID").descending());
        Page<Promotion> promotions = promotionRepository.findAll(pageable);
        List<PromotionDTO> promotionDTOList = new ArrayList<>();

        for(Promotion promotion : promotions) {
            PromotionDTO promotionDTO = new PromotionDTO();
            promotionDTO.setPromoCode(promotion.getPromoCode());
            promotionDTO.setPromotionID(promotion.getPromotionID());
            promotionDTO.setStartDate(promotion.getStartDate());
            promotionDTO.setEndDate(promotion.getEndDate());
            promotionDTO.setDiscountRate(promotion.getDiscountRate());
            promotionDTO.setDesciption(promotion.getDesciption());
            promotionDTO.setStatus(promotion.isStatus());
            promotionDTOList.add(promotionDTO);
        }

        PaginReponse<PromotionDTO> paginReponse = new PaginReponse<>();
        paginReponse.setContent(promotionDTOList);
        paginReponse.setPageSize(pageSize);
        paginReponse.setPageNum(pageNo);
        paginReponse.setTotalElements(promotions.getNumberOfElements());
        paginReponse.setTotalPages(promotions.getTotalPages());
        return paginReponse;
    }

    @Override
    public String updatePromotion(PromotionDTO promotion) {
        Promotion newPromotion = promotionRepository.findByPromotionID(promotion.getPromotionID())
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_INVALID));
        newPromotion.setPromoCode(promotion.getPromoCode());
        newPromotion.setStartDate(promotion.getStartDate());
        newPromotion.setEndDate(promotion.getEndDate());
        newPromotion.setDiscountRate(promotion.getDiscountRate() / 100);
        newPromotion.setDesciption(promotion.getDesciption());
        newPromotion.setStatus(promotion.isStatus());
        promotionRepository.save(newPromotion);
        return "Update mã giảm giá #" + promotion.getPromotionID() + " thành công";
    }

    @Override
    public String deletePromotion(int promotionId) {

        Promotion promotion = promotionRepository.findByPromotionID(promotionId)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_INVALID));
        if(promotion.getAccounts() != null){
            for(Account account : promotion.getAccounts()) {
                account.setPromotion(null);
                accountRepository.save(account);
            }
        }
        promotionRepository.delete(promotion);

        return "Xoá mã giảm giá #" + promotion.getPromotionID() + " thành công";
    }

    private String generatePromoCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public boolean isPromoCodeValidForAccount(Promotion promotion, Account account) {

        if (promotion == null || !promotion.isStatus()) {
            return false;
        }

        Date now = new Date();
        if (now.before(promotion.getStartDate()) || now.after(promotion.getEndDate())) {
            return false;
        }

        if (account.getPromotion() != null && account.getPromotion().getPromoCode().equals(promotion.getPromoCode())) {
            return false;
        }

        return true;
    }


}
