package com.group2.KoiFarmShop.repository;

import com.group2.KoiFarmShop.entity.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    public boolean existsByEmail(String email);
    Optional<Account> findByEmail(String email);
    @Transactional
    @Modifying
    @Query("update Account acc set acc.password=?2 where acc.email=?1")
    void updatePassword(String email, String password);
    @Transactional
    @Modifying
    @Query("update Account acc set acc.isVerified=?2 where acc.email=?1")
    void updateVerify(String email, boolean verify);

    Optional<Account> findByAccountID(int accountID);

    Optional<Account> findByEmailContains(String email);
}
