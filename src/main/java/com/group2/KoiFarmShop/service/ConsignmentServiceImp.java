package com.group2.KoiFarmShop.service;

import com.group2.KoiFarmShop.dto.request.ConsignmentKoiCare;
import com.group2.KoiFarmShop.dto.request.ConsignmentKoiRequest;
import com.group2.KoiFarmShop.dto.request.ConsignmentRequest;
import com.group2.KoiFarmShop.dto.request.KoiRequest;
import com.group2.KoiFarmShop.dto.response.ConsignmentDetailResponse;
import com.group2.KoiFarmShop.dto.response.ConsignmentResponse;
import com.group2.KoiFarmShop.dto.response.HealthcareResponse;
import com.group2.KoiFarmShop.dto.response.PaginReponse;
import com.group2.KoiFarmShop.entity.Consignment;
import com.group2.KoiFarmShop.entity.KoiFish;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ConsignmentServiceImp {

    public String createConsignment(int accountId, MultipartFile koiImg,
                                    String origin,
                                    boolean gender,
                                    int age,
                                    double size,
                                    String personality,
                                    double price,
                                    String food,
                                    String health,
                                    String ph,
                                    String temperature,
                                    String water,
                                    int pureBred,
                                    int categoryId,
                                    String name,
                                    MultipartFile certImg,
                                    String notes,
                                    String phoneNumber,
                                    boolean consignmentType,
                                    int duration,
                                    double serviceFee,
                                    boolean online);

    public String approveConsignment(int consignmentId) throws MessagingException;

    public String rejectConsignment(int consignmentId, String rejectionReason);

    public PaginReponse<ConsignmentResponse> getAllConsignmentForCustomer(int pageNo, int pageSize, int accountId);
    public PaginReponse<ConsignmentResponse> getAllConsignmentForStaff(int pageNo, int pageSize);

    public ConsignmentDetailResponse getConsignmentDetail(int consignmentId);

    public Consignment processPayment(int consignmentId, boolean isPay);

    public String deleteConsignment(int consignmentId);
    public ConsignmentDetailResponse updateConsignment(ConsignmentKoiRequest consignmentKoiRequest, int consignmentId, int koiId) throws IOException;
    public HealthcareResponse updateHealth(int consignmentId, ConsignmentKoiCare consignmentKoiCare) throws MessagingException;
    public HealthcareResponse addHealth(int consignmentId, ConsignmentKoiCare consignmentKoiCare) throws MessagingException;
}
