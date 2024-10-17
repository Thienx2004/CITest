package com.group2.KoiFarmShop.repository;

import com.group2.KoiFarmShop.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {

    @Query("SELECT o FROM Orders o " +
            "JOIN o.payment p " +
            "JOIN FETCH o.orderDetails od " +
            "LEFT JOIN od.koiFish k " +
            "LEFT JOIN od.batch b " +
            "WHERE (:accountID IS NULL OR o.account.accountID = :accountID) " +
            //"AND (:type IS NULL OR od.type = :type) " +
            "ORDER BY o.order_date DESC")
    Page<Orders> findOrdersWithFilters(@Param("accountID") Integer accountID,
                                       //@Param("type") Boolean type,
                                       Pageable pageable);
}
