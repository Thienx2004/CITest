package com.group2.KoiFarmShop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginReponse<T> {

    private List<T> content;
    private int pageNum;
    private int pageSize;
    private int totalElements;
    private int totalPages;
}
