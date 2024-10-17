package com.group2.KoiFarmShop.repository;

import com.group2.KoiFarmShop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    public Category findByCategoryID(Integer categoryID);
}
