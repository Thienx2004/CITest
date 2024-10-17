package com.group2.KoiFarmShop.repository;

import com.group2.KoiFarmShop.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    public List<OrderDetail> findAllByOrders_OrderID(int orderId);
}
