package com.group2.KoiFarmShop.repository;

import com.group2.KoiFarmShop.entity.Account;
import com.group2.KoiFarmShop.entity.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgotPasswordRepositoryI extends JpaRepository<ForgotPassword, Integer> {
    @Query("select fp from ForgotPassword fp where fp.otp=?1 and fp.account=?2")
    Optional<ForgotPassword> findByOptAndAccount(Integer otp, Account account);

}
