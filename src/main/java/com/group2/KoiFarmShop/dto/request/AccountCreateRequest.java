package com.group2.KoiFarmShop.dto.request;

import com.group2.KoiFarmShop.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateRequest {
    private String fullName;
    private String address;
    private String phone;
    private String email;
    private String password;
    private int roleId;
    private boolean status;
    private boolean isVerified;

}
