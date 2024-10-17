package com.group2.KoiFarmShop.repository;

import com.group2.KoiFarmShop.entity.Category;
import com.group2.KoiFarmShop.entity.KoiFish;

import com.group2.KoiFarmShop.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface KoiFishRepository extends JpaRepository<KoiFish, Integer> {
    public List<KoiFish> getAllByCategory(Category category);
    Page<KoiFish> findByCategory(Category category, Pageable pageable);
    public KoiFish findByKoiID(int koiId);
    Page<KoiFish> findAll(Specification<KoiFish> spec, Pageable pageable);

    @Query("SELECT k FROM Orders o " +
            "JOIN o.orderDetails od " +
            "JOIN od.koiFish k " +
            "WHERE (:accountID IS NULL OR o.account.accountID = :accountID) " +
            "AND od.type = true " +
            "ORDER BY o.order_date DESC")
    Page<KoiFish> findCustomerKoi(@Param("accountID") Integer accountID,
                                       Pageable pageable);
}

