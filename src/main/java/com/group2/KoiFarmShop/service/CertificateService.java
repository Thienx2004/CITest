package com.group2.KoiFarmShop.service;

import com.group2.KoiFarmShop.dto.response.*;
import com.group2.KoiFarmShop.entity.Certificate;
import com.group2.KoiFarmShop.entity.KoiFish;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.repository.CertificateRepository;
import com.group2.KoiFarmShop.repository.KoiFishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService implements CertificateServiceImp {

    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private KoiFishRepository koiFishRepository;
    @Autowired
    private FirebaseService firebaseService;
    @Override
    public PaginReponse<KoiFishReponse> getAllKoiFishReponse(int pageNo, int pageSize, String accountId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("order_date").descending());
        Integer accountIdInt = (accountId != null && !accountId.isEmpty()) ? Integer.parseInt(accountId) : null;

        Page<KoiFish> customerKoi = koiFishRepository.findCustomerKoi(accountIdInt, pageable);
        List<KoiFishReponse> customerKoiReponses = new ArrayList<>();

        for (KoiFish koiFish : customerKoi) {
//            CertificationReponse certificationReponse = new CertificationReponse();
//            certificationReponse.setId(koiFish.getCertificate().getId());
//            certificationReponse.setName(koiFish.getCertificate().getName());
//            certificationReponse.setImage(koiFish.getCertificate().getImage());
//            certificationReponse.setCreatedDate(koiFish.getCertificate().getCreatedDate());

            KoiFishReponse koiFishReponse = new KoiFishReponse();
            koiFishReponse.setId(koiFish.getKoiID());
            koiFishReponse.setOrigin(koiFish.getOrigin());
            koiFishReponse.setCategoryId(koiFish.getCategory().getCategoryID());
            koiFishReponse.setCategory(koiFish.getCategory().getCategoryName());
            koiFishReponse.setGender(koiFish.isGender());
            koiFishReponse.setAge(koiFish.getAge());
            koiFishReponse.setSize(koiFish.getSize());
            koiFishReponse.setPersonality(koiFish.getPersonality());
            koiFishReponse.setPrice(koiFish.getPrice());
            koiFishReponse.setStatus(koiFish.getStatus());
            customerKoiReponses.add(koiFishReponse);

        }

        PaginReponse<KoiFishReponse> paginReponse = new PaginReponse<>();
        paginReponse.setContent(customerKoiReponses);
        paginReponse.setPageNum(pageNo);
        paginReponse.setPageSize(pageSize);
        paginReponse.setTotalElements(customerKoi.getNumberOfElements());
        paginReponse.setTotalPages(customerKoi.getTotalPages());

        return paginReponse;
    }

    @Override
    public CertificationReponse getCertificationReponse(int koiId) {
        KoiFish koiFish = koiFishRepository.findByKoiID(koiId);
        CertificationReponse certificationReponse = new CertificationReponse();
        if(koiFish != null) {
            certificationReponse.setId(koiFish.getCertificate().getId());
            certificationReponse.setName(koiFish.getCertificate().getName());
            certificationReponse.setImage(koiFish.getCertificate().getImage());
            certificationReponse.setCreatedDate(koiFish.getCertificate().getCreatedDate());
        } else {
            throw new AppException(ErrorCode.KOINOTFOUND);
        }
        return certificationReponse;
    }
    public CertificationReponse updateCertificateImg(MultipartFile file, int id) throws IOException {
        Optional<Certificate> certificate = certificateRepository.findById(id);
        Certificate certificate1 = certificate.get();

        certificate1.setImage(firebaseService.uploadImage(file));
        Certificate updatedCertificate = certificateRepository.save(certificate1);
        return CertificationReponse.builder()
                .createdDate(updatedCertificate.getCreatedDate())
                .id(updatedCertificate.getId())
                .image(updatedCertificate.getImage())
                .name(updatedCertificate.getName())
                .build();
    }

}
