package com.group2.KoiFarmShop;

import com.group2.KoiFarmShop.dto.request.AccountCreationDTO;
import com.group2.KoiFarmShop.dto.request.LoginRequest;
import com.group2.KoiFarmShop.dto.response.ApiReponse;
import com.group2.KoiFarmShop.entity.Account;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.service.AccountServiceImp;
import com.group2.KoiFarmShop.service.KoiFishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class KoiFarmShopApplicationTests {

	@Mock
	private AccountServiceImp accountServiceImp;

	@Mock
	private KoiFishService koiFishService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGenerateOTPFormat() {
		when(accountServiceImp.generateOTP()).thenReturn("123456");

		String otp = accountServiceImp.generateOTP();

		assertEquals("Kiểm tra độ dài của OTP là 6", 6, otp.length());
		assertTrue(otp.matches("\\d+"), "OTP chỉ chứa các chữ số");
	}

	
}
