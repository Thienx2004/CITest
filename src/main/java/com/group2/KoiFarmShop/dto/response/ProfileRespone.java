package com.group2.KoiFarmShop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRespone {
    private int id;
    private String fullName;
    private String password;
    private String email;
    private String phone;
    private String address;
    private boolean isVerified=true;
    private String avatar;
}
