package com.group2.KoiFarmShop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.group2.KoiFarmShop.dto.CertificateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KoiFishReponse {
    private int id;
    private String origin;
    private boolean gender;
    private int age;
    private double size;
    private String personality;
    private double price;
    private String koiImage;
    private int categoryId;
    private String category;
    private CertificateRequest certificate;
    private int purebred;
    private int status;

}
