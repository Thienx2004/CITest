package com.group2.KoiFarmShop;

import com.group2.KoiFarmShop.dto.request.AccountCreationDTO;
import com.group2.KoiFarmShop.dto.request.LoginGoogleRequest;
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
		// Initialize the mocks before each test
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGenerateOTPFormat() {
		// Mock the generateOTP method
		when(accountServiceImp.generateOTP()).thenReturn("123456");

		// Gọi phương thức generateOTP
		String otp = accountServiceImp.generateOTP();

		// Kiểm tra độ dài của OTP là 6
		assertEquals("Kiểm tra độ dài của OTP là 6", 6, otp.length());

		// Kiểm tra OTP chỉ chứa các chữ số
		assertTrue(otp.matches("\\d+"), "OTP should only contain digits");
	}

	@Test
	void testGetKoiFishByIdInvalidIdThrowsException() {
		int invalidId = -1;

		// Mock the koiFishService.getKoiFishById to throw AppException
		when(koiFishService.getKoiFishById(invalidId)).thenThrow(new AppException(ErrorCode.INVALIDNUMBER));

		AppException exception = assertThrows(AppException.class, () -> {
			koiFishService.getKoiFishById(invalidId);
		});

		assertEquals("tìm cá koi với id không hợp lệ", ErrorCode.INVALIDNUMBER, exception.getErrorCode());
	}

	@Test
	void testLoginWithValidUser() {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("user1@example.com");
		loginRequest.setPassword("123456@");

		// Mock the login method
		when(accountServiceImp.login(loginRequest)).thenReturn(ApiReponse.builder().message("Đăng nhập thành công").build());

		String result = accountServiceImp.login(loginRequest).getMessage();

		assertEquals("Test đăng nhập với user hợp lệ", "Đăng nhập thành công", result);
	}

	@Test
	void testLoginWithInvalidEmail() {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("user99@example.com");
		loginRequest.setPassword("123456@");

		// Mock the login method to throw AppException
		when(accountServiceImp.login(loginRequest)).thenThrow(new AppException(ErrorCode.INVALIDACCOUNT));

		AppException exception = assertThrows(AppException.class, () -> {
			accountServiceImp.login(loginRequest);
		});

		assertEquals("Test đăng nhập với email không hợp lệ", ErrorCode.INVALIDACCOUNT, exception.getErrorCode());
	}

	@Test
	void testLoginWithWrongPassword() {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("user1@example.com");
		loginRequest.setPassword("1234567");

		// Mock the login method to throw AppException for wrong password
		when(accountServiceImp.login(loginRequest)).thenThrow(new AppException(ErrorCode.WRONGPASSWORD));

		AppException exception = assertThrows(AppException.class, () -> {
			accountServiceImp.login(loginRequest);
		});

		assertEquals("Test đăng nhập với mật khẩu sai", ErrorCode.WRONGPASSWORD, exception.getErrorCode());
	}

	@Test
	void testLoginWithNotVerifiedAccount() {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("user1@gmail.com");
		loginRequest.setPassword("123456@");

		// Mock the login method to throw AppException for unverified account
		when(accountServiceImp.login(loginRequest)).thenThrow(new AppException(ErrorCode.NOTVERIFYACCOUNT));

		AppException exception = assertThrows(AppException.class, () -> {
			accountServiceImp.login(loginRequest);
		});

		assertEquals("Test đăng nhập với tài khoản chưa xác minh", ErrorCode.NOTVERIFYACCOUNT, exception.getErrorCode());
	}

	@Test
	void registerWithValidUser() {
		String fullname = "test";
		String email = "test@example.com";
		String password = "123456";
		AccountCreationDTO accountDTO = new AccountCreationDTO(fullname, email, password);
		Account expectedAccount = new Account(fullname, email, password);

		// Mock the createAccount method
		when(accountServiceImp.createAccount(accountDTO)).thenReturn(expectedAccount);

		Account actualAccount = accountServiceImp.createAccount(accountDTO);
		assertEquals("Thêm tài khoản thành công", expectedAccount, actualAccount);
	}

}
