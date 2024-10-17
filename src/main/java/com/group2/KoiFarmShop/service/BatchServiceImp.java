package com.group2.KoiFarmShop.service;

import com.group2.KoiFarmShop.dto.response.BatchPageReponse;
import com.group2.KoiFarmShop.dto.response.BatchReponse;
import com.group2.KoiFarmShop.dto.request.BatchCreateDTO;
import org.springframework.web.multipart.MultipartFile;

public interface BatchServiceImp {

    public BatchPageReponse getBatchListFilter(int pageNo, int pageSize, String categoryID, String avgSize, String age, String minPrice, String maxPrice, String sortField, String sortDirection, String purebred);
    public BatchReponse getBatchById(int id);
    public String addBatch(BatchCreateDTO batch);
    public String updateBatch(int batchID, BatchCreateDTO batch);
    public String deleteBatch(String id);
    public BatchPageReponse getBatchByCategory(int categoryId, int pageNo, int pageSize);

    public BatchPageReponse getAllBatch(int pageNo, int pageSize);

}
