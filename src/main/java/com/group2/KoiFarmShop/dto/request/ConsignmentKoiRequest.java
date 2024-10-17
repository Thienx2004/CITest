package com.group2.KoiFarmShop.dto.request;

import com.group2.KoiFarmShop.dto.CertificateRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsignmentKoiRequest {
    private KoiRequest koiRequest;
    private ConsignmentRequest consignmentRequest;
}
