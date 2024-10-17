package com.group2.KoiFarmShop.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FirebaseService {

    public String getImageUrl(String fileName) {

        Bucket bucket = StorageClient.getInstance().bucket();


        Blob blob = bucket.get(fileName);


        if (blob == null) {
            throw new RuntimeException("File không tồn tại trong Firebase Storage");
        }


        return blob.signUrl(7, java.util.concurrent.TimeUnit.DAYS).toString(); // URL tồn tại trong ? ngày
    }

//    private final String bucketName = "koi-farm-shop-5212e.appspot.com";

//    public List<String> getImageUrls(String folderName) {
//        List<String> imageUrls = new ArrayList<>();
//
//        Storage storage = StorageOptions.getDefaultInstance().getService();
//        Bucket bucket = storage.get(bucketName);
//
//        for (Blob blob : bucket.list(Storage.BlobListOption.prefix(folderName)).iterateAll()) {
//            if (!blob.isDirectory()) {
//                // Tạo URL truy cập công khai
//                String imageUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, blob.getName());
//                imageUrls.add(imageUrl);
//            }
//        }
//
//        return imageUrls;
//    }


   public String uploadImage(MultipartFile file) throws IOException {
       Bucket bucket = StorageClient.getInstance().bucket();
       String timestamp = String.valueOf(System.currentTimeMillis());
       String blobName = timestamp + "_" + file.getOriginalFilename();
       Blob blob = bucket.create(blobName, file.getInputStream(),file.getContentType());
       return String.format("https://storage.googleapis.com/%s/%s", bucket.getName(), blob.getName());
   }



}
