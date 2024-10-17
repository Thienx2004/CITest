package com.group2.KoiFarmShop.service;

import com.group2.KoiFarmShop.dto.response.BatchPageReponse;
import com.group2.KoiFarmShop.dto.response.BatchReponse;
import com.group2.KoiFarmShop.dto.request.BatchCreateDTO;
import com.group2.KoiFarmShop.entity.Batch;
import com.group2.KoiFarmShop.entity.Category;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.repository.BatchRepository;

import jakarta.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class BatchService implements BatchServiceImp{

    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private FirebaseService firebaseService;

    @Override
    public BatchPageReponse getBatchListFilter(int pageNo, int pageSize, String categoryID, String avgSize, String age, String minPrice, String maxPrice, String sortField, String sortDirection, String purebred) {
        // Kiểm tra sortField và sortDirection, nếu không có thì mặc định theo "batchID" và "asc"/"desc"
        if (sortField == null || sortField.isEmpty()) {
            sortField = "batchID";
        }
        if (sortDirection == null || sortDirection.isEmpty()) {
            sortDirection = "asc";
        }

        if (sortDirection != null && !sortDirection.isEmpty()) {
            if (sortDirection.equals("1")) {
                sortDirection = "asc";
            } else if (sortDirection.equals("2")) {
                sortDirection = "desc";
            } else {
                sortDirection = "asc";
            }
        } else {
            sortDirection = "asc";
        }


        // Thiết lập phân trang và sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        // Sử dụng Specification để lọc dữ liệu
        Specification<Batch> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(categoryID != null && !categoryID.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("categoryID"), Integer.parseInt(categoryID)));
            }

            if (avgSize != null && !avgSize.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("avgSize"), avgSize));
            }

            if (age != null && !age.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("age"), Integer.parseInt(age)));
            }

            // Lọc theo minPrice và maxPrice
            if (minPrice != null && !minPrice.isEmpty()) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), Double.parseDouble(minPrice)));
            }
            if (maxPrice != null && !maxPrice.isEmpty()) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), Double.parseDouble(maxPrice)));
            }
            if(purebred != null && !purebred.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("purebred"),  Integer.parseInt(purebred)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Batch> batchPage = batchRepository.findAll(spec, pageable);

        List<BatchReponse> batchReponseList = new ArrayList<>();
        for (Batch batch : batchPage.getContent()) {
            BatchReponse batchReponse = new BatchReponse();
            batchReponse.setBatchID(batch.getBatchID());
            batchReponse.setOrigin(batch.getOrigin());
            batchReponse.setAge(batch.getAge());
            batchReponse.setAvgSize(batch.getAvgSize());
            batchReponse.setQuantity(batch.getQuantity());
            batchReponse.setPrice(batch.getPrice());
            batchReponse.setCategoryID(batch.getCategory().getCategoryID());
            batchReponse.setCategoryName(batch.getCategory().getCategoryName());
            batchReponse.setFood(batch.getFood());
            batchReponse.setHealth(batch.getHealth());
            batchReponse.setTemperature(batch.getTemperature());
            batchReponse.setWater(batch.getWater());
            batchReponse.setPH(batch.getPH());
            batchReponse.setPH(batch.getPH());
            batchReponse.setPurebred(batch.getPurebred());
            batchReponse.setBatchImg(batch.getBatchImg());
            batchReponse.setStatus(batch.getStatus());

            batchReponseList.add(batchReponse);
        }

        BatchPageReponse batchPageReponse = new BatchPageReponse();
        batchPageReponse.setBatchReponses(batchReponseList);
        batchPageReponse.setPageNum(pageNo);
        batchPageReponse.setPageSize(batchPage.getSize());
        batchPageReponse.setTotalElements(batchPage.getTotalElements());
        batchPageReponse.setTotalPages(batchPage.getTotalPages());
        return batchPageReponse;
    }


    @Override
    public BatchReponse getBatchById(int id) {
        Batch batch = batchRepository.findByBatchID(id)
                .orElseThrow(() -> new AppException(ErrorCode.BATCH_NOT_EXISTED));
        BatchReponse batchReponse = new BatchReponse();
        batchReponse.setBatchID(batch.getBatchID());
        batchReponse.setAge(batch.getAge());
        batchReponse.setOrigin(batch.getOrigin());
        batchReponse.setQuantity(batch.getQuantity());
        batchReponse.setPrice(batch.getPrice());
        batchReponse.setCategoryID(batch.getCategory().getCategoryID());
        batchReponse.setCategoryName(batch.getCategory().getCategoryName());
        batchReponse.setBatchImg(batch.getBatchImg());
        batchReponse.setStatus(batch.getStatus());

        return batchReponse;
    }

    @Override
    public String addBatch(BatchCreateDTO batch) {
        try {
            Category category = new Category();
            category.setCategoryID(batch.getCategoryID());
            Batch newBatch = new Batch();
            newBatch.setOrigin(batch.getOrigin());
            newBatch.setAge(batch.getAge());
            newBatch.setAvgSize(batch.getAvgSize());
            newBatch.setQuantity(batch.getQuantity());
            newBatch.setPrice(batch.getPrice());
            newBatch.setCategory(category);
            newBatch.setFood(batch.getFood());
            newBatch.setHealth(batch.getHealth());
            newBatch.setPH(batch.getPH());
            newBatch.setTemperature(batch.getTemperature());
            newBatch.setWater(batch.getWater());
            newBatch.setPurebred(batch.getPurebred());
            newBatch.setBatchImg(firebaseService.uploadImage(batch.getBatchImg()));
            newBatch.setStatus(batch.getStatus());
            batchRepository.save(newBatch);
            return "Lưu lô koi ID: " + newBatch.getBatchID() + " thành công";
        } catch (Exception e) {
            // Xử lý ngoại lệ, có thể log và trả về thông báo lỗi
            return "Không lưu được lô koi" + e.getMessage();
        }
    }

    @Override
    public String updateBatch(int batchId, BatchCreateDTO batchCreateDTO) {
        try {
            Category category = new Category();
            category.setCategoryID(batchCreateDTO.getCategoryID());
            Batch existedBatch = batchRepository.findByBatchID(batchId)
                        .orElseThrow(() -> new AppException(ErrorCode.BATCH_NOT_EXISTED));
            existedBatch.setOrigin(batchCreateDTO.getOrigin());
            existedBatch.setAge(batchCreateDTO.getAge());
            existedBatch.setAvgSize(batchCreateDTO.getAvgSize());
            existedBatch.setQuantity(batchCreateDTO.getQuantity());
            existedBatch.setPrice(batchCreateDTO.getPrice());
            existedBatch.setCategory(category);
            existedBatch.setFood(batchCreateDTO.getFood());
            existedBatch.setHealth(batchCreateDTO.getHealth());
            existedBatch.setPH(batchCreateDTO.getPH());
            existedBatch.setTemperature(batchCreateDTO.getTemperature());
            existedBatch.setWater(batchCreateDTO.getWater());
            existedBatch.setPurebred(batchCreateDTO.getPurebred());
            existedBatch.setBatchImg(firebaseService.uploadImage(batchCreateDTO.getBatchImg()));
            existedBatch.setStatus(batchCreateDTO.getStatus());
            batchRepository.save(existedBatch);
            return "Update lô koi ID: " + batchId + " thành công";
        } catch (Exception e) {
            return "Lỗi khi update lô koi " + e.getMessage();
        }
    }

    @Override
    public String deleteBatch(String batchId) {
        try {
            if(batchRepository.existsById(Integer.parseInt(batchId))) {
                batchRepository.deleteById(Integer.parseInt(batchId));
                return "Xoá lô koi ID: " + batchId + " thành công";
            } else throw new AppException(ErrorCode.BATCH_NOT_EXISTED);
        } catch (Exception e) {
            return "Lỗi khi xoá lô koi " + e.getMessage();
        }
    }

    @Override
    public BatchPageReponse getBatchByCategory(int categoryId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Category category = new Category();
        category.setCategoryID(categoryId);
        Page<Batch> batchPage = batchRepository.findByCategory(category, pageable);
        List<Batch> batchList = batchPage.getContent();
        List<BatchReponse> batchReponseList = new ArrayList<>();
        BatchPageReponse batchPageReponse = new BatchPageReponse();
        for(Batch batch : batchList) {
            BatchReponse batchReponse = new BatchReponse();
            batchReponse.setBatchID(batch.getBatchID());
            batchReponse.setAge(batch.getAge());
            batchReponse.setOrigin(batch.getOrigin());
            batchReponse.setQuantity(batch.getQuantity());
            batchReponse.setPrice(batch.getPrice());
            batchReponse.setCategoryID(batch.getCategory().getCategoryID());
            batchReponse.setCategoryName(batch.getCategory().getCategoryName());
            batchReponse.setBatchImg(batch.getBatchImg());

            batchReponse.setStatus(batch.getStatus());

            batchReponseList.add(batchReponse);
        }

        batchPageReponse.setBatchReponses(batchReponseList);
        batchPageReponse.setPageNum(pageNo);
        batchPageReponse.setPageSize(batchPage.getSize());
        batchPageReponse.setTotalElements(batchPage.getTotalElements());
        batchPageReponse.setTotalPages(batchPage.getTotalPages());


        return batchPageReponse;
    }

    @Override
    public BatchPageReponse getAllBatch(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("batchID").descending());
        Page<Batch> batchPage = batchRepository.findAll(pageable);
        List<Batch> batchList = batchPage.getContent();
        List<BatchReponse> batchReponseList = new ArrayList<>();
        BatchPageReponse batchPageReponse = new BatchPageReponse();
        for(Batch batch : batchList) {
            BatchReponse batchReponse = new BatchReponse();
            batchReponse.setBatchID(batch.getBatchID());
            batchReponse.setOrigin(batch.getOrigin());
            batchReponse.setAge(batch.getAge());
            batchReponse.setAvgSize(batch.getAvgSize());
            batchReponse.setQuantity(batch.getQuantity());
            batchReponse.setPrice(batch.getPrice());
            batchReponse.setCategoryID(batch.getCategory().getCategoryID());
            batchReponse.setCategoryName(batch.getCategory().getCategoryName());
            batchReponse.setFood(batch.getFood());
            batchReponse.setHealth(batch.getHealth());
            batchReponse.setTemperature(batch.getTemperature());
            batchReponse.setWater(batch.getWater());
            batchReponse.setPH(batch.getPH());
            batchReponse.setPH(batch.getPH());
            batchReponse.setPurebred(batch.getPurebred());
            batchReponse.setBatchImg(batch.getBatchImg());
            batchReponse.setStatus(batch.getStatus());

            batchReponse.setStatus(batch.getStatus());

            batchReponseList.add(batchReponse);
        }

        batchPageReponse.setBatchReponses(batchReponseList);
        batchPageReponse.setPageNum(pageNo);
        batchPageReponse.setPageSize(batchPage.getSize());
        batchPageReponse.setTotalElements(batchPage.getTotalElements());
        batchPageReponse.setTotalPages(batchPage.getTotalPages());


        return batchPageReponse;
    }

}
