package com.group2.KoiFarmShop.dto.response;

import com.group2.KoiFarmShop.dto.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountPageRespone {
    private int pageNum;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    List<AccountDTO> accounts;
}
