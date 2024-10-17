package com.group2.KoiFarmShop.repository;

import com.group2.KoiFarmShop.entity.Certificate;
import com.group2.KoiFarmShop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Optional<Payment> findPaymentByTransactionCode(String transactionCode);
}
