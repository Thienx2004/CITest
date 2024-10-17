package com.group2.KoiFarmShop.dto;

import com.group2.KoiFarmShop.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {
    private int id;
    private String fullName;
    private String address;
    private String phone;
    private String email;
    private String password;
    private Role role;
    private String avatar;
    private boolean status;
}
