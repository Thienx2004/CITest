package com.group2.KoiFarmShop.exception;


public enum ErrorCode {
    USER_EXISTED(1001, "Email đã tồn tại"),
    UNCATEGORIZED_EXCEPTION(9999, "Không xác định"),
    WRONGPASSWORD(5001,"Sai mật khẩu"),
    INVALIDACCOUNT(5002,"Không tìm thấy account"),
    INVALIDOTP(5003,"OTP không hợp lệ"),
    OTP_EXPIRED(5004, "OTP hết hạn"),
    ACCOUNT_ALREADY_VERIFIED(5010, "Email đã xác thực"),
    PASSWORDINVALID(5011,"Nhập lại pass word"),
    NOTVERIFYACCOUNT(5006,"Tài khoản chưa xác thực"),
    KOINOTFOUND(5005,"Không có kết quả"),
    BATCH_NOT_EXISTED(5007, "Không tìm thấy lô,"),
    POWERLESS(5008,"Không có quyền hạn"),
    PAYMENT_FAILED(5009,"Thanh toán thất bại"),
    TRANSACTION_INVALID(5010, "Mã thanh toán này không hợp lệ"),
    SAVE_FAILED(5011, "Lưu thất bại"),
    CONSIGNMENT_NOT_FOUND(5012, "Đơn ký gửi không tìm thấy"),
    PROMOTION_INVALID(5013, "Mã này không hợp lệ"),
    INVALIDNUMBER(5014,"số không hợp lệ"),
    PROMOTION_DATE_OVERLAP(5015, "Đã có mã khuyến mãi trong thời gian này"),
    CONSIGNMENT_OUT_OF_DATE(5016, "Đơn ký gửi đã quá hạn do không thanh toán."),
    CANNOTUPDATE(5017,"Không thể cập nhập"),
    CERTIFICATE_NOT_FOUND(5018, "Không tìm thấy chứng chỉ của Koi này");
    ;


    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}

