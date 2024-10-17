package com.group2.KoiFarmShop.controller;

import com.group2.KoiFarmShop.dto.response.ApiReponse;
import com.group2.KoiFarmShop.dto.response.KoiFishDetailReponse;
import com.group2.KoiFarmShop.dto.response.KoiFishPageResponse;
import com.group2.KoiFarmShop.dto.request.KoiRequest;
import com.group2.KoiFarmShop.dto.response.ProfileRespone;
import com.group2.KoiFarmShop.entity.Category;
import com.group2.KoiFarmShop.exception.AppException;
import com.group2.KoiFarmShop.exception.ErrorCode;
import com.group2.KoiFarmShop.repository.CategoryRepository;
import com.group2.KoiFarmShop.service.KoiFishServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/koifish")
public class KoiFishController {
    @Autowired
    KoiFishServiceImp koiFishService;
    @Autowired
    CategoryRepository categoryRepository;

    // Lấy toàn bộ danh sách cá Koi
    @GetMapping("/allkoi")
    @Operation(summary = "Lấy danh sách Koi", description = "-Nguyễn Hoàng Thiên")
    public ApiReponse<KoiFishPageResponse> getAllKoiFish(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        if (page <= 0 || pageSize <= 0) {
            throw new AppException(ErrorCode.INVALIDNUMBER);
        }
        KoiFishPageResponse koiFishList = koiFishService.getAllKoiFish(page, pageSize);
        return ApiReponse.<KoiFishPageResponse>builder().data(koiFishList).statusCode(200).build();
    }

    // Lấy cá Koi theo Category và Trang
    @GetMapping("/category")
    @Operation(summary = "Lấy danh sách Koi theo Category", description = "-Nguyễn Hoàng Thiên")
    public ApiReponse<KoiFishPageResponse> getKoiByCategory(@RequestParam("categoryId") int id, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        Category category = categoryRepository.findByCategoryID(id);
        KoiFishPageResponse koiFishList = koiFishService.getKoiByCategory(category, page, pageSize);
        return ApiReponse.<KoiFishPageResponse>builder().data(koiFishList).statusCode(200).build();
    }

    // Lấy cá Koi theo ID
    @GetMapping("/{id}")
    @Operation(summary = "Lấy Koi theo id", description = "-Nguyễn Hoàng Thiên")
    public ApiReponse<KoiFishDetailReponse> getKoiFishById(@PathVariable int id) {

        KoiFishDetailReponse koiFish = koiFishService.getKoiFishById(id);

        return ApiReponse.<KoiFishDetailReponse>builder().data(koiFish).message("Xử lý thành công").build();
    }

    // Thêm mới cá Koi
    @PostMapping("/add")
    @Operation(summary = "Thêm cá Koi", description = "-Nguyễn Hoàng Thiên")

    public ApiReponse<KoiFishDetailReponse> addKoiFish(@ModelAttribute KoiRequest koiRequest
                                                ) throws IOException {
//        if(koiFish.getCategoryId()==0){
//            throw new AppException(ErrorCode.KOINOTFOUND);
//        }
        KoiFishDetailReponse koiFishReponse=koiFishService.addKoiFish(koiRequest);
        return ApiReponse.<KoiFishDetailReponse>builder().data(koiFishReponse).message("Thêm Koi thành công").statusCode(200).build();
    }


    // Cập nhật cá Koi
    @PutMapping("/update/{id}")
    @Operation(summary = "Cập nhật Koi theo id", description = "-Nguyễn Hoàng Thiên")
    public ApiReponse<KoiFishDetailReponse> updateKoiFish(@PathVariable int id, @ModelAttribute KoiRequest koiFish) throws IOException {
        if(koiFish.getCategoryId()<=0||id<=0){
            throw new AppException(ErrorCode.INVALIDNUMBER);
        }
        KoiFishDetailReponse koiFishReponse = koiFishService.updateKoiFish(id, koiFish);
        return ApiReponse.<KoiFishDetailReponse>builder().data(koiFishReponse).message("Cập nhật thành công").statusCode(200).build();
    }

    @GetMapping("/filter")
    @Operation(summary = "Lọc cá Koi", description = "-Nguyễn Hoàng Thiên")
    public ApiReponse<KoiFishPageResponse> filterKoiFish(
            @RequestParam(required = false) String categoryID,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String minSize,
            @RequestParam(required = false) String maxSize,
            @RequestParam(required = false) String age,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String sortField1,
            @RequestParam(required = false) String sortDirection1,
            @RequestParam(required = false) String sortField2,
            @RequestParam(required = false) String sortDirection2,
            @RequestParam(required = false) String purebred,
            @RequestParam(defaultValue = "1") int page,      // Thêm tham số page với giá trị mặc định là 1
            @RequestParam(defaultValue = "6") int pageSize
            // Thêm tham số pageSize với giá trị mặc định là 6
    ) {

        KoiFishPageResponse koiFishReponses = koiFishService.filterKoiFish(categoryID, maxSize, minSize, gender, age, minPrice, maxPrice, origin, page, pageSize, sortField1, sortDirection1, sortField2, sortDirection2, purebred); // Truyền thêm page và pageSize vào filter
        return ApiReponse.<KoiFishPageResponse>builder().data(koiFishReponses).build();
    }

    @GetMapping("/compare")
    @Operation(summary = "So sánh Koi", description = "-Nguyễn Hoàng Thiên")
    public ApiReponse<List<KoiFishDetailReponse>> compareKoiFish(@RequestParam("koiFishId1") int id1, @RequestParam("koiFishId2") int id2) {
        List<KoiFishDetailReponse> koiFishReponses = koiFishService.compareKoiFish(id1, id2);
        return ApiReponse.<List<KoiFishDetailReponse>>builder().data(koiFishReponses).build();
    }

    @Operation(summary = "Cập nhật img của Koi", description = "Nguyễn Hoàng Thiên")
    @PostMapping("/updateImg/{id}")
    public ApiReponse<KoiFishDetailReponse> updateAvatar(@RequestParam("file") MultipartFile file, @PathVariable int id) throws IOException {
        KoiFishDetailReponse koiFishDetailReponse = koiFishService.updateKoiImg(file, id);
        return ApiReponse.<KoiFishDetailReponse>builder()
                .data(koiFishDetailReponse)
                .statusCode(200)
                .build();
    }
    @Operation(summary = "Cập nhật status của Koi", description = "Nguyễn Hoàng Thiên")
    @PostMapping("/changeStatus/{id}/{status}")
    public ApiReponse changeStatus(@PathVariable int id,@PathVariable int status) throws IOException {
        koiFishService.changeKoiFishStatus(id, status);
        return ApiReponse.<KoiFishDetailReponse>builder()
                .message("cập nhật thành công")
                .statusCode(200)
                .build();
    }

    @GetMapping("/status")
    @Operation(summary = "Lấy danh sách Koi theo status", description = "-Nguyễn Hoàng Thiên")
    public ApiReponse<KoiFishPageResponse> getKoiByStatus(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize,@RequestParam("status") int status) {
        if (page <= 0 || pageSize <= 0) {
            throw new AppException(ErrorCode.INVALIDNUMBER);
        }
        KoiFishPageResponse koiFishList = koiFishService.getFishByStatus(page, pageSize,status);
        return ApiReponse.<KoiFishPageResponse>builder().data(koiFishList).statusCode(200).build();
    }
}
