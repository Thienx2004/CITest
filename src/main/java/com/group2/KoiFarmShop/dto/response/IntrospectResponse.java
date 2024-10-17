package com.group2.KoiFarmShop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Builder
@AllArgsConstructor
@Data
public class IntrospectResponse {
    private boolean valid;




        // Constructor không tham số
        public IntrospectResponse() {
            this.valid = false; // Hoặc true, tùy thuộc vào logic của bạn
        }


}
