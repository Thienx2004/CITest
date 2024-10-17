package com.group2.KoiFarmShop.repository;

import com.group2.KoiFarmShop.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    public Page<Feedback> findAllByRating(int rating, Pageable pageable);
}
