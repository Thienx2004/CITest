package com.group2.KoiFarmShop.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
@Data
public class CertificateRequest {
    public String name;
    public MultipartFile image;
    public Date createdDate;
}
