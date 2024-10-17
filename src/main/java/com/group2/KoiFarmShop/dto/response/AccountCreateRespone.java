package com.group2.KoiFarmShop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountCreateRespone {
    private int accountId;
    private String fullName;
    private String address;
    private String phone;
    private String email;
    private String password;
    private int roleId;
    private boolean status;
    private boolean isVerified;

}
