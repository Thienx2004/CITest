package com.group2.KoiFarmShop.controller;

import com.group2.KoiFarmShop.dto.response.ApiReponse;
import com.group2.KoiFarmShop.dto.response.BatchPageReponse;
import com.group2.KoiFarmShop.dto.response.BatchReponse;
import com.group2.KoiFarmShop.dto.request.BatchCreateDTO;
import com.group2.KoiFarmShop.service.BatchServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/BatchKoi")
public class BatchController {

    @Autowired
    BatchServiceImp batchService;

    @GetMapping("")

    public ApiReponse<BatchPageReponse> getAllBatchesByFilter(@RequestParam int pageNo, @RequestParam int pageSize,
                                                              @RequestParam(required = false) String categoryID,
                                                              @RequestParam(required = false) String avgSize,
                                                              @RequestParam(required = false) String age,
                                                              @RequestParam(required = false) String minPrice,
                                                              @RequestParam(required = false) String maxPrice,
                                                              @RequestParam(required = false) String sortField,
                                                              @RequestParam(required = false) String sortDirection,
                                                              @RequestParam(required = false) String purebred) {
        ApiReponse apiReponse = new ApiReponse();
        BatchPageReponse batchPageReponse = batchService.getBatchListFilter(pageNo, pageSize, categoryID, avgSize, age, minPrice, maxPrice, sortField, sortDirection, purebred);

        apiReponse.setData(batchPageReponse);
        return apiReponse;
    }

    @GetMapping("/getAllBatch")
    public ApiReponse<BatchPageReponse> getAllBatch(@RequestParam int pageNo, @RequestParam int pageSize) {
        ApiReponse apiReponse = new ApiReponse();
        apiReponse.setData(batchService.getAllBatch(pageNo, pageSize));

        return apiReponse;
    }

    @GetMapping("/{batchId}")
    public ApiReponse<BatchReponse> getBatch(@PathVariable int batchId) {
        ApiReponse apiReponse = new ApiReponse();
        BatchReponse batchReponse = batchService.getBatchById(batchId);
        apiReponse.setData(batchReponse);
        return apiReponse;
    }

    @GetMapping("/{categoryId}/{pageNo}/{pageSize}")
    public ApiReponse<BatchPageReponse> getBatchByCategory(@PathVariable int categoryId, @PathVariable int pageNo, @PathVariable int pageSize) {
        ApiReponse apiReponse = new ApiReponse();
        BatchPageReponse batchPageReponse = batchService.getBatchByCategory(categoryId, pageNo, pageSize);
        apiReponse.setData(batchPageReponse);
        return apiReponse;
    }

    @PostMapping("/createBatch")
    public ApiReponse<String> createBatch(@ModelAttribute BatchCreateDTO batch) {
        ApiReponse apiReponse = new ApiReponse();
        apiReponse.setData(batchService.addBatch(batch));

        return apiReponse;
    }

    @PutMapping("/updateBatch/{batchId}")
    public ApiReponse<String> updateBatch(@PathVariable int batchId, @ModelAttribute BatchCreateDTO batch) {
        ApiReponse apiReponse = new ApiReponse();
        apiReponse.setData(batchService.updateBatch(batchId, batch));

        return apiReponse;
    }

    @DeleteMapping("/deleteBatch")
    public ApiReponse<String> deleteBatch(@RequestParam String batchId) {
        ApiReponse apiReponse = new ApiReponse();
        apiReponse.setData(batchService.deleteBatch(batchId));

        return apiReponse;
    }
}
