package com.group2.KoiFarmShop.service;


import com.group2.KoiFarmShop.dto.AccountDTO;
import com.group2.KoiFarmShop.dto.response.*;

//import com.group2.KoiFarmShop.dto.response.AccountDTO;

import com.group2.KoiFarmShop.dto.Content;
import com.group2.KoiFarmShop.dto.request.*;
import com.group2.KoiFarmShop.entity.Account;
import com.group2.KoiFarmShop.entity.Role;
import com.group2.KoiFarmShop.entity.VerificationToken;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.repository.AccountRepository;
import com.group2.KoiFarmShop.repository.ForgotPasswordRepositoryI;
import com.group2.KoiFarmShop.repository.RoleRepository;
import com.group2.KoiFarmShop.repository.VerificationTokenRepository;
import com.group2.KoiFarmShop.ultils.JWTUltilsHelper;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import java.util.Optional;
import java.util.*;


@Service
public class AccountService implements AccountServiceImp{
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUltilsHelper jwtUltilsHelper;
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private ForgotPasswordRepositoryI forgotPasswordRepository;
    @Autowired
    private FirebaseService firebaseService;

    @Override
        public ApiReponse login(LoginRequest loginRequest) {
        // Tìm kiếm tài khoản dựa trên email
        Optional<Account> optionalAccount = accountRepository.findByEmail(loginRequest.getEmail());
        ApiReponse apiReponse = new ApiReponse();
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            // Kiểm tra mật khẩu
            if (passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
                // Kiểm tra trạng thái xác thực
                if(account.isVerified() && account.isStatus()){
                    String Token=jwtUltilsHelper.generateToken(account);
                    Content content = new Content();
                    content.setId(account.getAccountID());
                    content.setEmail(account.getEmail());
                    content.setPhone(account.getPhone());
                    content.setFullName(account.getFullName());
                    content.setRole(account.getRole().getRoleName());
                    content.setPhone(account.getPhone());
                    content.setAddress(account.getAddress());
                    content.setAvatar(account.getAvatar());
                    content.setAccessToken(Token);

                    apiReponse.setData(content);
                    apiReponse.setMessage("Đăng nhập thành công");
                    // Tài khoản đăng nhập thành công
                }else {
                    throw new AppException(ErrorCode.NOTVERIFYACCOUNT);
                }
            } else {
                throw new AppException(ErrorCode.WRONGPASSWORD);
            }
        }else{
            throw new AppException(ErrorCode.INVALIDACCOUNT);
        }

        return apiReponse;
    }

    @Override
    public ApiReponse logingg(LoginGoogleRequest loginGoogleRequest) {
        ApiReponse apiReponse = new ApiReponse();

        try {
            // Tìm kiếm tài khoản dựa trên email
            Optional<Account> optionalAccount = accountRepository.findByEmail(loginGoogleRequest.getEmail());
            Account account;

            if (optionalAccount.isPresent()) {
                account = optionalAccount.get();
            } else {
                Role role = new Role();
                role.setRoleID(3);
                account = new Account();
                account.setEmail(loginGoogleRequest.getEmail());
                account.setFullName(loginGoogleRequest.getName());
                account.setRole(role);
                accountRepository.save(account);
            }
            if (!account.isVerified()) {
                account.setVerified(true);
                accountRepository.updateVerify(loginGoogleRequest.getEmail(),true);
            }
            String newToken = jwtUltilsHelper.generateToken(account);

            Content content = new Content();
            content.setId(account.getAccountID());
            content.setEmail(account.getEmail());
            content.setPhone(account.getPhone());
            content.setFullName(account.getFullName());
            content.setRole(account.getRole().getRoleName());
            content.setAddress(account.getAddress());
            content.setAvatar(account.getAvatar());
            content.setAccessToken(newToken);
            apiReponse.setData(content);
            apiReponse.setMessage("Đăng nhập thành công");
        } catch (Exception e) {
            apiReponse.setMessage("Đã xảy ra lỗi: " + e.getMessage());
            apiReponse.setData(null);
        }
        return apiReponse;
    }

    @Override
    public Account createAccount(AccountCreationDTO accountCreationDTO) {

        if (accountRepository.existsByEmail(accountCreationDTO.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Role role = new Role();
        role.setRoleID(3);

        Account account = new Account();
        account.setEmail(accountCreationDTO.getEmail());
        account.setFullName(accountCreationDTO.getFullName());
        account.setRole(role);
        if(accountCreationDTO.getPassword()!=null) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            account.setPassword(passwordEncoder.encode(accountCreationDTO.getPassword()));
        }
        accountRepository.save(account);

        if(!account.isVerified()) {
        // Tạo mã OTP
        String otp = generateOTP();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(otp); // Lưu OTP trong trường token
        verificationToken.setAccount(account);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(2)); // OTP hết hạn sau 2 phút

        verificationTokenRepository.save(verificationToken);

        // Gửi OTP qua email
        emailService.sendVerificationEmail(accountCreationDTO.getEmail(), otp);
        }





        return account;
    }



//    @Override
//    public Account getAccount(int id) {
//        return null;
//    }

    @Override
    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Tạo mã OTP 6 chữ số
        return String.valueOf(otp);
    }

    @Transactional
    @Override
    public ApiReponse<String> resendOTP(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.INVALIDACCOUNT));

        if (account.isVerified()) {
            throw new AppException(ErrorCode.ACCOUNT_ALREADY_VERIFIED);
        }

//        VerificationToken verificationToken1 = verificationTokenRepository.findByAccount_AccountID(account.getAccountID())
//                .orElseThrow(() -> new AppException(ErrorCode.INVALIDACCOUNT));
//        System.out.println(verificationToken1.getToken());
//
//        // Xóa mã OTP cũ (nếu có)
//        verificationTokenRepository.deleteById(verificationToken1.getId());

        // Tạo mã OTP mới
        String newOtp = generateOTP();

        // Tạo đối tượng VerificationToken mới
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(newOtp);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(10)); // Hết hạn sau 10 phút
        verificationToken.setAccount(account);

        // Lưu mã OTP mới vào cơ sở dữ liệu
        verificationTokenRepository.save(verificationToken);

        // Gửi OTP mới qua email
        emailService.sendVerificationEmail(account.getEmail(), newOtp);
        ApiReponse apiReponse = new ApiReponse();
        apiReponse.setData("OTP đã gửi thành công!");
        return apiReponse;
    }


    @Override
    public ApiReponse<String> verifyOTP(String email, String otp) {
        ApiReponse apiReponse = new ApiReponse();

        // Tìm tài khoản qua email
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.INVALIDACCOUNT));

        if(account.isVerified()) {
            List<VerificationToken> verificationTokenList = verificationTokenRepository.findByAccount_AccountID(account.getAccountID());
            if(!verificationTokenList.isEmpty()) {
                VerificationToken tmp = new VerificationToken();
                for(VerificationToken verificationToken : verificationTokenList) {
                    if(verificationToken.getToken().equals(otp)) {
                        tmp = verificationToken;
                        apiReponse.setMessage("OTP hợp lệ");
                        String tokenOTP = jwtUltilsHelper.generateTokenForOTP(account);
                        apiReponse.setData(tokenOTP);

                    }
                }
                if(tmp.getToken() != null) {
                    for (VerificationToken verificationToken : verificationTokenList) {
                        verificationTokenRepository.delete(verificationToken);
                    }
                } else throw new AppException(ErrorCode.INVALIDOTP);
            }

        }
        else {
            // Tìm mã OTP trong bảng VerificationToken
            VerificationToken verificationToken = verificationTokenRepository.findByToken(otp)
                    .orElseThrow(() -> new AppException(ErrorCode.INVALIDOTP));
//            out.println(verificationToken.getId());
//            out.println(verificationToken.getToken());

            if (!verificationToken.getToken().equals(otp))
                throw new AppException(ErrorCode.INVALIDOTP);


            // Kiểm tra xem OTP có hết hạn không
            if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new AppException(ErrorCode.OTP_EXPIRED);
            }


            // Xác thực tài khoản
            account.setVerified(true);

            accountRepository.save(account);
            apiReponse.setMessage("Xác thực tài khoản thành công!");
        }
        return apiReponse;
    }

   public ProfileRespone getProfile (String email) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isEmpty()) {
            throw new AppException(ErrorCode.INVALIDACCOUNT);
        }
        return ProfileRespone.builder()
                .id(account.get().getAccountID())
                .password((account.get().getPassword()))
                .fullName(account.get().getFullName())
                .email(account.get().getEmail())
                .phone(account.get().getPhone())
                .address(account.get().getAddress())
                .isVerified(account.get().isVerified())
                .avatar(account.get().getAvatar())
                .build();
   }


    public ProfileRespone updateProfile (ProfileRequest profileRequest, int id) throws IOException {
        Account account = new Account();
        Optional<Account>account1 = accountRepository.findById(id);
        account.setAccountID(id);
        account.setEmail(account1.get().getEmail());
        account.setFullName(profileRequest.getFullName());
        account.setPassword(account1.get().getPassword());
        account.setAddress(profileRequest.getAddress());
        account.setPhone(profileRequest.getPhone());
        account.setVerified(account1.get().isVerified());
        account.setAvatar(account1.get().getAvatar());
        account.setRole(account1.get().getRole());


        Account accSave = accountRepository.save(account);
        return ProfileRespone.builder()
                .id(accSave.getAccountID())
//                .password(passwordEncoder.encode(accSave.getPassword()))
                .fullName(accSave.getFullName())
                .email(accSave.getEmail())
                .phone(accSave.getPhone())
                .address(accSave.getAddress())
                .isVerified(accSave.isVerified())
                .avatar(accSave.getAvatar())
                .build();
    }


    public ProfileRespone updatePassword (PasswordRequest passwordRequest, int id) {
        Optional<Account>account1 = accountRepository.findById(id);
        Account account=(Account) account1.get();
        if(passwordEncoder.matches(passwordRequest.getPassword(), account1.get().getPassword())){
            throw new AppException(ErrorCode.PASSWORDINVALID);
        }
        account.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        Account accSave = accountRepository.save(account);

        return ProfileRespone.builder()

                .password(accSave.getPassword())
                .build();
    }


    public AccountPageRespone getAllAccounts(int page, int pageSize) {

        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("accountID").descending());
        Page<Account> accountList = accountRepository.findAll(pageable);
        List<AccountDTO> accountDTOList = new ArrayList<>();
        for (Account account : accountList) {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setId(account.getAccountID());
            accountDTO.setFullName(account.getFullName());
            accountDTO.setEmail(account.getEmail());
            accountDTO.setPassword(account.getPassword());
            accountDTO.setPhone(account.getPhone());
            accountDTO.setAddress(account.getAddress());
            accountDTO.setRole(account.getRole());
            accountDTO.setAvatar(account.getAvatar());
            accountDTO.setStatus(account.isStatus());
            accountDTOList.add(accountDTO);
        }

        return AccountPageRespone.builder()
                .pageNum(accountList.getNumber()+1)
                .totalPages(accountList.getTotalPages())
                .totalElements(accountList.getTotalElements())
                .pageSize(accountList.getSize())
                .accounts(accountDTOList)
                .build();
    }

     public ProfileRespone updateAvatar(MultipartFile file, int id) throws IOException {
         Optional<Account>account1 = accountRepository.findById(id);
         Account account=(Account) account1.get();
         account.setAvatar(firebaseService.uploadImage(file));
         Account accSave = accountRepository.save(account);
         return ProfileRespone.builder()
                 .avatar(accSave.getAvatar())
                 .build();
     }

    public AccountDTO updateAccountStatus(int accountId) {
        // Tìm theo ID
        Optional<Account> optionalAccount = accountRepository.findById(accountId);

        if (!optionalAccount.isPresent()) {
            throw new RuntimeException("Account not found with id: " + accountId);
        }

        Account account = optionalAccount.get();
        // Cập nhật status
        if(account.isStatus()){
            account.setStatus(false);
        }else{
            account.setStatus(true);
        }


        accountRepository.save(account);// Lưu thay đổi vào DB


        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getAccountID());
        accountDTO.setFullName(account.getFullName());
        accountDTO.setEmail(account.getEmail());
        accountDTO.setPassword(account.getPassword());
        accountDTO.setPhone(account.getPhone());
        accountDTO.setAddress(account.getAddress());
        accountDTO.setRole(account.getRole());
        accountDTO.setAvatar(account.getAvatar());
        accountDTO.setStatus(account.isStatus());

        return accountDTO;
    }

    public AccountCreateRespone createAccount(AccountCreateRequest accountRequest) {
        if (accountRepository.existsByEmail(accountRequest.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Role role = new Role();
        role.setRoleID(accountRequest.getRoleId());
        Account account = new Account();
        account.setFullName(accountRequest.getFullName());
        account.setAddress(accountRequest.getAddress());
        account.setPhone(accountRequest.getPhone());
        account.setEmail(accountRequest.getEmail());
        if(accountRequest.getPassword()!=null) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            account.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        }
        account.setStatus(true);
        account.setVerified(true);
        account.setRole(role);
//        account.setAvatar(accountRequest.getAvatar());
        accountRepository.save(account);
        return AccountCreateRespone.builder()
                .fullName(accountRequest.getFullName())
                .accountId(account.getAccountID())
                .address(accountRequest.getAddress())
                .phone(accountRequest.getPhone())
                .email(accountRequest.getEmail())
                .password(passwordEncoder.encode(accountRequest.getPassword()))
                .roleId(accountRequest.getRoleId())
                .status(accountRequest.isStatus())
                .isVerified(accountRequest.isVerified())
                .build();
    }

    public AccountDTO searchByEmail(String email) {
        Account optionalAccount = accountRepository.findByEmailContains(email).orElseThrow(()-> new AppException(ErrorCode.INVALIDACCOUNT));
        return AccountDTO.builder()
                .fullName(optionalAccount.getFullName())
                .email(optionalAccount.getEmail())
                .address(optionalAccount.getAddress())
                .id(optionalAccount.getAccountID())
                .role(optionalAccount.getRole())
                .status(optionalAccount.isStatus())
//                .password(optionalAccount.getPassword())
                .avatar(optionalAccount.getAvatar())
                .phone(optionalAccount.getPhone())
                .build();
    }

}
