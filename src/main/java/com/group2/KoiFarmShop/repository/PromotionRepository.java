package com.group2.KoiFarmShop.repository;

import com.group2.KoiFarmShop.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    Optional<Promotion> findByPromoCode(String promoCode);
    Optional<Promotion> findByPromotionID(int promotionID);
}
