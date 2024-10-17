package com.group2.KoiFarmShop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryRequest {

    String categoryName;
    String categoryDescription;
//    String categoryImage;
    boolean status;
}
