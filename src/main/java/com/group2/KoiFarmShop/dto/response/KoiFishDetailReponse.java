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
public class KoiFishDetailReponse {
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
    private CertificateResponse certificate;
    private int purebred;
    private String health;
    private String temperature;
    private String water;
    private String pH;
    private String food;
    private int status;
    private List<KoiFishDetailReponse> list;
  


}
