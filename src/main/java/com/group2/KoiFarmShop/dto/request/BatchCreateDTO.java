package com.group2.KoiFarmShop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchCreateDTO {

    private String origin;
    private int quantity;
    private int age;
    private String avgSize;
    private double price;
    private int categoryID;
    private int purebred;
    private String health;
    private String temperature;
    private String water;
    private String pH;
    private String food;
    private MultipartFile batchImg;
    private int status = 1;

}
