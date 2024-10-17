package com.group2.KoiFarmShop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HealthcareResponse {
    private String healthStatus;
    private String growthStatus;
    private String careEnvironment;
    private String note;
    private long dayRemain;
}
