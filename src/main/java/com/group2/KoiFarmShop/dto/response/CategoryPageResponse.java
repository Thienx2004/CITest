package com.group2.KoiFarmShop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryPageResponse {
    private int pageNum;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    List<CategoryReponse> categoryReponses;
}
