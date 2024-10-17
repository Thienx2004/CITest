package com.group2.KoiFarmShop.repository;

import com.group2.KoiFarmShop.entity.Account;
import com.group2.KoiFarmShop.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    //Optional<VerificationToken> findByAccount_AccountID(int accountId);
    List<VerificationToken> findByAccount_AccountID(int accountID);
    void deleteById(int id);
}
