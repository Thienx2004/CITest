package com.group2.KoiFarmShop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsignmentKoiCare {
    private int consignmentId;
    private String healthStatus;
    private String growthStatus;
    private String careEnvironment;
    private String note;
}
