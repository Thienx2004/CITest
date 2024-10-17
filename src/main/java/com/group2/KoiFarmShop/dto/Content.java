package com.group2.KoiFarmShop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class Content {
    private int id;
    private String email;
    private String fullName;
    private String role;
    private String phone;
    private String address;
    private String avatar;
    private String accessToken;

}
