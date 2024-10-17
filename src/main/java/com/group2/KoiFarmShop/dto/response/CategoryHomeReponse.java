package com.group2.KoiFarmShop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryHomeReponse {
    List<CategoryReponse> categoryReponses;
    private int pageNum;
    private int pageSize;
    private int totalElements;
    private int totalPages;
}
