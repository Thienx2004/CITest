package com.group2.KoiFarmShop.dto.request;

import com.google.firebase.database.annotations.Nullable;
import com.group2.KoiFarmShop.dto.CertificateRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class KoiRequest {
    private String origin;
    private boolean gender;
    private int age;
    private double size;
    private String personality;
    private double price;
    private int categoryId;
    private int purebred;
    private String health;
    private String temperature;
    private String water;
    private String pH;
    private String food;
    private MultipartFile koiImage;
    private String name;
    @Nullable
    private MultipartFile image;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;
}
