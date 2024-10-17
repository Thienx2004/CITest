package com.group2.KoiFarmShop.service;


import com.group2.KoiFarmShop.dto.CertificateRequest;
import com.group2.KoiFarmShop.dto.request.ConsignmentKoiCare;
import com.group2.KoiFarmShop.dto.request.ConsignmentKoiRequest;
import com.group2.KoiFarmShop.dto.request.ConsignmentRequest;
import com.group2.KoiFarmShop.dto.request.KoiRequest;
import com.group2.KoiFarmShop.dto.response.*;
import com.group2.KoiFarmShop.entity.*;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.repository.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ConsignmentService implements ConsignmentServiceImp {

    @Autowired
    private ConsignmentRepository consignmentRepository;
    @Autowired
    private KoiFishRepository koiFishRepository;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private CertificateService certificateService;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private KoiFishService koiFishService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private HealthcareRepository healthcareRepository;

    @Override
    public String createConsignment(int accountId, MultipartFile koiImg, String origin, boolean gender, int age, double size, String personality, double price, String food,
                                    String health,
                                    String ph,
                                    String temperature,
                                    String water,
                                    int pureBred, int categoryId, String name, MultipartFile certImg, String notes,
                                    String phoneNumber,
                                    boolean consignmentType,
                                    int duration,
                                    double serviceFee,
                                    boolean online) {
        try {
            Consignment consignment = new Consignment();
            Account account = new Account();
            account.setAccountID(accountId);

            consignment.setAccount(account);

            Category category = new Category();
            category.setCategoryID(categoryId);

            KoiFish koiFish = new KoiFish();
            koiFish.setOrigin(origin);
            koiFish.setGender(gender);
            koiFish.setAge(age);
            koiFish.setSize(size);
            koiFish.setPersonality(personality);
            koiFish.setPrice(price);
            koiFish.setFood(food);
            koiFish.setHealth(health);
            koiFish.setPH(ph);
            koiFish.setTemperature(temperature);
            koiFish.setWater(water);
            koiFish.setPurebred(pureBred);
            koiFish.setKoiImage(firebaseService.uploadImage(koiImg));
            koiFish.setCategory(category);
            koiFish.setStatus(4);  // set status pending dang cho dc duyet

            Certificate certificate = new Certificate();
            certificate.setName(name);
            certificate.setImage(firebaseService.uploadImage(certImg));
            certificate.setCreatedDate(new Date());

            koiFish.setCertificate(certificate);
            certificate.setKoiFish(koiFish);

            koiFishRepository.save(koiFish);
            certificateRepository.save(certificate);

            consignment.setKoiFish(koiFish);
            consignment.setAgreedPrice(price);
            consignment.setConsignmentType(consignmentType);
            consignment.setOnline(online);
            consignment.setNotes(notes);
            consignment.setPhoneNumber(phoneNumber);
            consignment.setStatus(1);
            consignment.setConsignmentDate(new Date());
            consignment.setDuration(duration);
            consignment.setServiceFee(serviceFee);
            consignmentRepository.save(consignment);
        } catch (Exception e) {
            throw new AppException(ErrorCode.SAVE_FAILED);
        }

        return "Đơn ký gửi đã được tạo thành công, chờ phê duyệt!";
    }

    @Override
    public String approveConsignment(int consignmentId) throws MessagingException {

        Consignment consignment = consignmentRepository.findConsignmentByConsignmentID(consignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.CONSIGNMENT_NOT_FOUND));

        if (consignment.getStatus() == 1) { // Nếu đơn đang ở trạng thái Pending
            // Cập nhật trạng thái chờ thanh toán
            consignment.setStatus(4); // Pending Payment
            consignment.setStartDate(new Date());

            // Tính ngày hết hạn thanh toán (3 ngày sau khi phê duyệt)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, 3);
            consignment.setEndDate(calendar.getTime());

            consignmentRepository.save(consignment);

            // Gửi mail cho customer thông báo về đơn đã phê duyệt và hướng dẫn thanh toán
            emailService.sendEmailToCustomer(consignment.getAccount().getEmail(), consignment.getConsignmentID());

            return "Đơn ký gửi đã được phê duyệt và chờ thanh toán!";
        } else {
            throw new AppException(ErrorCode.CONSIGNMENT_NOT_FOUND);
        }


    }

    @Override
    public String rejectConsignment(int consignmentId, String rejectionReason) {
        Consignment consignment = consignmentRepository.findById(consignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.CONSIGNMENT_NOT_FOUND));

        if (consignment.getStatus() == 1) {

            KoiFish koiFish = koiFishRepository.findByKoiID(consignment.getKoiFish().getKoiID());

            consignment.setStatus(3); //status don bi tu choi
            consignment.setNotes(rejectionReason);
            consignment.setKoiFish(null);
            consignmentRepository.save(consignment);

            if (koiFish != null) {

                Certificate certificate = koiFish.getCertificate();
                if (certificate != null) {
                    certificateRepository.delete(certificate);
                }

                koiFishRepository.delete(koiFish);
            }

            return "Đã từ chốt đơn ký gửi!";
        } else {
            throw new AppException(ErrorCode.CONSIGNMENT_NOT_FOUND);
        }
    }

    @Override
    public PaginReponse<ConsignmentResponse> getAllConsignmentForCustomer(int pageNo, int pageSize, int accountId) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("consignmentDate").descending());
        Page<Consignment> consignmentPage = consignmentRepository.findConsignmentsByAccount_AccountID(accountId, pageable);
        List<ConsignmentResponse> consignmentResponses = new ArrayList<>();
        for (Consignment consignment : consignmentPage.getContent()) {
            ConsignmentResponse consignmentResponse = new ConsignmentResponse();
            consignmentResponse.setConsignmentID(consignment.getConsignmentID());
            consignmentResponse.setConsignmentDate(consignment.getConsignmentDate());
            consignmentResponse.setConsignmentType(consignment.isConsignmentType());
            consignmentResponse.setAgreedPrice(consignment.getAgreedPrice());
            consignmentResponse.setNotes(consignment.getNotes());
            consignmentResponse.setEmail(consignment.getAccount().getEmail());
            consignmentResponse.setFullname(consignment.getAccount().getFullName());
            consignmentResponse.setPhoneNumber(consignment.getPhoneNumber());
            consignmentResponse.setDuration(consignment.getDuration());
            consignmentResponse.setServiceFee(consignment.getServiceFee());
            consignmentResponse.setStartDate(consignment.getStartDate());
            consignmentResponse.setEndDate(consignment.getEndDate());
            consignmentResponse.setStatus(consignment.getStatus());
            consignmentResponse.setOnline(consignment.isOnline());
            consignmentResponses.add(consignmentResponse);
        }
        PaginReponse<ConsignmentResponse> consignmentResponsePaginReponse = new PaginReponse<>();
        consignmentResponsePaginReponse.setContent(consignmentResponses);
        consignmentResponsePaginReponse.setPageSize(pageSize);
        consignmentResponsePaginReponse.setPageNum(pageNo);
        consignmentResponsePaginReponse.setTotalElements(consignmentPage.getContent().size());
        consignmentResponsePaginReponse.setTotalPages(consignmentPage.getTotalPages());

        return consignmentResponsePaginReponse;
    }

    @Override
    public PaginReponse<ConsignmentResponse> getAllConsignmentForStaff(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("consignmentDate").descending());
        Page<Consignment> consignmentPage = consignmentRepository.findAll(pageable);
        List<ConsignmentResponse> consignmentResponses = new ArrayList<>();
        for (Consignment consignment : consignmentPage.getContent()) {
            ConsignmentResponse consignmentResponse = new ConsignmentResponse();
            consignmentResponse.setConsignmentID(consignment.getConsignmentID());
            consignmentResponse.setConsignmentDate(consignment.getConsignmentDate());
            consignmentResponse.setConsignmentType(consignment.isConsignmentType());
            consignmentResponse.setAgreedPrice(consignment.getAgreedPrice());
            consignmentResponse.setNotes(consignment.getNotes());
            consignmentResponse.setEmail(consignment.getAccount().getEmail());
            consignmentResponse.setFullname(consignment.getAccount().getFullName());
            consignmentResponse.setPhoneNumber(consignment.getPhoneNumber());
            consignmentResponse.setDuration(consignment.getDuration());
            consignmentResponse.setServiceFee(consignment.getServiceFee());
            consignmentResponse.setStartDate(consignment.getStartDate());
            consignmentResponse.setEndDate(consignment.getEndDate());
            consignmentResponse.setStatus(consignment.getStatus());
            consignmentResponse.setOnline(consignment.isOnline());
            consignmentResponses.add(consignmentResponse);
        }
        PaginReponse<ConsignmentResponse> consignmentResponsePaginReponse = new PaginReponse<>();
        consignmentResponsePaginReponse.setContent(consignmentResponses);
        consignmentResponsePaginReponse.setPageSize(pageSize);
        consignmentResponsePaginReponse.setPageNum(pageNo);
        consignmentResponsePaginReponse.setTotalElements(consignmentPage.getContent().size());
        consignmentResponsePaginReponse.setTotalPages(consignmentPage.getTotalPages());

        return consignmentResponsePaginReponse;
    }

    @Override
    public ConsignmentDetailResponse getConsignmentDetail(int consignmentId) {
        Consignment consignment = consignmentRepository.findConsignmentByConsignmentID(consignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.CONSIGNMENT_NOT_FOUND));

        ConsignmentDetailResponse detailResponse = new ConsignmentDetailResponse();
        detailResponse.setConsignmentID(consignment.getConsignmentID());
        detailResponse.setConsignmentDate(consignment.getConsignmentDate());
        detailResponse.setConsignmentType(consignment.isConsignmentType());
        detailResponse.setAgreedPrice(consignment.getAgreedPrice());
        detailResponse.setNotes(consignment.getNotes());
        detailResponse.setEmail(consignment.getAccount().getEmail());
        detailResponse.setFullname(consignment.getAccount().getFullName());
        detailResponse.setPhoneNumber(consignment.getPhoneNumber());
        detailResponse.setDuration(consignment.getDuration());
        detailResponse.setServiceFee(consignment.getServiceFee());
        detailResponse.setStartDate(consignment.getStartDate());
        detailResponse.setEndDate(consignment.getEndDate());
        detailResponse.setStatus(consignment.getStatus());
        detailResponse.setOnline(consignment.isOnline());

        KoiFish koiFish = consignment.getKoiFish();
        if (koiFish != null) {
            KoiFishDetailReponse koiFishResponse = new KoiFishDetailReponse();
            koiFishResponse.setId(koiFish.getKoiID());
            koiFishResponse.setCategoryId(koiFish.getCategory().getCategoryID());
            koiFishResponse.setCategory(koiFish.getCategory().getCategoryName());
            koiFishResponse.setOrigin(koiFish.getOrigin());
            koiFishResponse.setGender(koiFish.isGender());
            koiFishResponse.setAge(koiFish.getAge());
            koiFishResponse.setSize(koiFish.getSize());
            koiFishResponse.setPersonality(koiFish.getPersonality());
            koiFishResponse.setPrice(koiFish.getPrice());
            koiFishResponse.setFood(koiFish.getFood());
            koiFishResponse.setHealth(koiFish.getHealth());
            koiFishResponse.setPH(koiFish.getPH());
            koiFishResponse.setTemperature(koiFish.getTemperature());
            koiFishResponse.setWater(koiFish.getWater());
            koiFishResponse.setPurebred(koiFish.getPurebred());
            koiFishResponse.setKoiImage(koiFish.getKoiImage());
            koiFishResponse.setStatus(koiFish.getStatus());

            Certificate certificate = koiFish.getCertificate();
            if (certificate != null) {
                CertificateResponse certificateResponse = new CertificateResponse();
                certificateResponse.setName(certificate.getName());
                certificateResponse.setImage(certificate.getImage());
                certificateResponse.setCreatedDate(certificate.getCreatedDate());
                koiFishResponse.setCertificate(certificateResponse);
            }

            detailResponse.setKoiFish(koiFishResponse);
            if (!consignment.isConsignmentType()&&koiFish.getStatus()==5) {
                Optional<Healthcare> healthcare = healthcareRepository.findById(koiFish.getKoiID());
                if (healthcare.isPresent()) {
                    HealthcareResponse healthcareResponse = new HealthcareResponse();
                    healthcareResponse.setHealthStatus(healthcare.get().getHealthStatus());
                    healthcareResponse.setGrowthStatus(healthcare.get().getGrowthStatus());
                    healthcareResponse.setCareEnvironment(healthcare.get().getCareEnvironment());
                    healthcareResponse.setNote(healthcare.get().getNote());
                    LocalDate currentDate = LocalDate.now();
                    LocalDate futureDate = LocalDate.of(healthcare.get().getConsignmentDate().getYear(),healthcare.get().getConsignmentDate().getMonth(),healthcare.get().getConsignmentDate().getDay());
                    healthcareResponse.setDayRemain(ChronoUnit.DAYS.between(currentDate, futureDate));
                    detailResponse.setHealthcare(healthcareResponse);
                }
            }
        }

        return detailResponse;
    }

    @Override
    public Consignment processPayment(int consignmentId, boolean isPaid) {
        Consignment consignment = consignmentRepository.findConsignmentByConsignmentID(consignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.CONSIGNMENT_NOT_FOUND));

        if (consignment.getStatus() == 4) { // Nếu đơn đang ở trạng thái Pending Payment
            if (isPaid) {
                consignment.setStatus(2); // Đơn đã được thanh toán, chờ bán
                KoiFish koiFish = koiFishRepository.findByKoiID(consignment.getKoiFish().getKoiID());
                if (koiFish != null) {
                    if (consignment.isConsignmentType())
                        koiFish.setStatus(3);// Đặt trạng thái bán cho cá
                    else koiFish.setStatus(5);// Đặt trạng thái chăm sóc cho cá
                } else throw new AppException(ErrorCode.KOINOTFOUND);

                consignmentRepository.save(consignment);
                return consignment;
            } else if (new Date().after(consignment.getEndDate())) {
                consignment.setStatus(5); // Đơn quá hạn
                consignmentRepository.save(consignment);
                throw new AppException(ErrorCode.CONSIGNMENT_OUT_OF_DATE);
            }
        }

        return consignment;
    }

    @Override
    public String deleteConsignment(int consignmentId) {
        Consignment consignment = consignmentRepository.findById(consignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.CONSIGNMENT_NOT_FOUND));

        if (consignment != null) {

            KoiFish koiFish = koiFishRepository.findByKoiID(consignment.getKoiFish().getKoiID());
            consignment.setKoiFish(null);
            consignmentRepository.save(consignment);

            if (koiFish != null) {

                Certificate certificate = koiFish.getCertificate();
                if (certificate != null) {
                    certificateRepository.delete(certificate);
                }

                koiFishRepository.delete(koiFish);
            }

            Payment payment = consignment.getPayment();
            if (payment != null) {
                paymentRepository.delete(payment);
            }
            consignmentRepository.delete(consignment);

            return "Đã xoá thành công đơn ký gửi!";
        } else {
            throw new AppException(ErrorCode.CONSIGNMENT_NOT_FOUND);
        }
    }

    @Override
    public ConsignmentDetailResponse updateConsignment(ConsignmentKoiRequest consignmentKoiRequest, int consignmentId, int koiId) throws IOException {
        if (consignmentKoiRequest.getConsignmentRequest().getStatus() != 1) {
            throw new AppException(ErrorCode.CANNOTUPDATE);
        }
        //Consignment
        Consignment consignmentToUpdated = new Consignment();
        Optional<Account> account = accountRepository.findById(consignmentKoiRequest.getConsignmentRequest().getAccountid());
        consignmentToUpdated.setConsignmentID(consignmentId);
        consignmentToUpdated.setConsignmentDate(consignmentKoiRequest.getConsignmentRequest().getConsignmentDate());
        consignmentToUpdated.setConsignmentType(consignmentKoiRequest.getConsignmentRequest().isConsignmentType());
        consignmentToUpdated.setAccount(account.get());
        consignmentToUpdated.setStatus(consignmentKoiRequest.getConsignmentRequest().getStatus());
        consignmentToUpdated.setOnline(consignmentKoiRequest.getConsignmentRequest().isOnline());
        consignmentToUpdated.setNotes(consignmentKoiRequest.getConsignmentRequest().getNotes());
        consignmentToUpdated.setDuration(consignmentKoiRequest.getConsignmentRequest().getDuration());
        consignmentToUpdated.setKoiFish(koiFishRepository.findByKoiID(koiFishService.updateKoiFish(koiId, consignmentKoiRequest.getKoiRequest()).getId()));
        consignmentToUpdated.setAgreedPrice(consignmentKoiRequest.getConsignmentRequest().getAgreedPrice());
        consignmentToUpdated.setEndDate(consignmentKoiRequest.getConsignmentRequest().getEndDate());
        consignmentToUpdated.setStartDate(consignmentKoiRequest.getConsignmentRequest().getStartDate());
        consignmentToUpdated.setServiceFee(consignmentKoiRequest.getConsignmentRequest().getServiceFee());
        Consignment consignmentUpdated = consignmentRepository.save(consignmentToUpdated);
        return ConsignmentDetailResponse.builder()
                .consignmentID(consignmentUpdated.getConsignmentID())
                .consignmentDate(consignmentUpdated.getConsignmentDate())
                .consignmentType(consignmentUpdated.isConsignmentType())
                .email(consignmentUpdated.getAccount().getEmail())
                .agreedPrice(consignmentUpdated.getAgreedPrice())
                .notes(consignmentUpdated.getNotes())
                .duration(consignmentUpdated.getDuration())
                .endDate(consignmentUpdated.getEndDate())
                .startDate(consignmentUpdated.getStartDate())
                .serviceFee(consignmentUpdated.getServiceFee())
                .fullname(consignmentUpdated.getAccount().getFullName())
                .koiFish(koiFishService.getKoiFishById(consignmentUpdated.getKoiFish().getKoiID()))
                .online(consignmentUpdated.isOnline())
                .phoneNumber(consignmentUpdated.getPhoneNumber())
                .notes(consignmentUpdated.getNotes())
                .build();
    }

    @Override
    public HealthcareResponse updateHealth(int consignmentId, ConsignmentKoiCare consignmentKoiCare) throws MessagingException {
        Consignment consignment = consignmentRepository.findById(consignmentId).get();
        Healthcare healthcare = healthcareRepository.findById(consignment.getKoiFish().getKoiID()).get();
        healthcare.setId(consignment.getKoiFish().getKoiID());
        healthcare.setHealthStatus(consignmentKoiCare.getHealthStatus());
        healthcare.setGrowthStatus(consignmentKoiCare.getGrowthStatus());
        healthcare.setCareEnvironment(consignmentKoiCare.getCareEnvironment());
        healthcare.setNote(consignmentKoiCare.getNote());
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = LocalDate.of(healthcare.getConsignmentDate().getYear(),healthcare.getConsignmentDate().getMonth(),healthcare.getConsignmentDate().getDay());
        healthcareRepository.save(healthcare);
        emailService.sendEmailForCareFish(consignment.getAccount().getEmail(), consignmentId, consignmentKoiCare);
        return HealthcareResponse.builder()
                .healthStatus(consignmentKoiCare.getHealthStatus())
                .careEnvironment(consignmentKoiCare.getCareEnvironment())
                .growthStatus(consignmentKoiCare.getGrowthStatus())
                .note(consignmentKoiCare.getNote())
                .dayRemain(ChronoUnit.DAYS.between(currentDate, futureDate))
                .build();
    }
    @Override
    public HealthcareResponse addHealth(int consignmentId, ConsignmentKoiCare consignmentKoiCare) throws MessagingException {
        Consignment consignment = consignmentRepository.findById(consignmentId).get();
        Healthcare healthcare = new Healthcare();
        healthcare.setId(consignment.getKoiFish().getKoiID());
        healthcare.setHealthStatus(consignmentKoiCare.getHealthStatus());
        healthcare.setGrowthStatus(consignmentKoiCare.getGrowthStatus());
        healthcare.setCareEnvironment(consignmentKoiCare.getCareEnvironment());
        healthcare.setNote(consignmentKoiCare.getNote());
        Date currentDate = new Date(); // Ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate); // Cài đặt ngày cho Calendar
        calendar.add(Calendar.MONTH, consignment.getDuration());
        Healthcare healthcare1=healthcareRepository.save(healthcare);
        emailService.sendEmailForCareFish(consignment.getAccount().getEmail(), consignmentId, consignmentKoiCare);
        LocalDate date = LocalDate.now();
        LocalDate futureDate = LocalDate.of(healthcare1.getConsignmentDate().getYear(),healthcare1.getConsignmentDate().getMonth(),healthcare1.getConsignmentDate().getDay());
        return HealthcareResponse.builder()
                .healthStatus(consignmentKoiCare.getHealthStatus())
                .careEnvironment(consignmentKoiCare.getCareEnvironment())
                .growthStatus(consignmentKoiCare.getGrowthStatus())
                .note(consignmentKoiCare.getNote())
                .dayRemain(ChronoUnit.DAYS.between(date, futureDate))
                .build();
    }

}



