package com.group2.KoiFarmShop.controller;

import com.google.cloud.storage.Blob;
import com.google.firebase.cloud.StorageClient;
import com.group2.KoiFarmShop.service.FirebaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

@RequestMapping

@RestController
public class FirebaseController {
    @Autowired
    private FirebaseService firebaseService;


    @GetMapping("/image/{fileName}")
    public String getImageUrl(@PathVariable String fileName) {
        return firebaseService.getImageUrl(fileName);
    }
//
//    @GetMapping("/image-gallery")
//    public String getImageGallery(Model model) {
//        List<String> imageUrls = firebaseService.getImageUrls("KoiAsagi/");
//        model.addAttribute("imageUrls", imageUrls);
//        return "images";
//    }

//    @PostMapping
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            String fileUrl = firebaseService.uploadFile(file);
//            return ResponseEntity.ok(fileUrl);
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
//        }
//    }


//    @PostMapping
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            String fileUrl = firebaseService.uploadFile(file);
//            return ResponseEntity.ok(fileUrl);
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
//        }
//    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = firebaseService.uploadImage(file);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload image");
        }

    }
//    @PostMapping("/upload")
//    public String uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
//        // Tạo tham chiếu tới Firebase Storage
//        if (!(request instanceof MultipartHttpServletRequest)) {
//            throw new IllegalArgumentException("Request is not a multipart request");
//        }
//        String bucketName = "koi-farm-shop-5212e.appspot.com";
//        String fileName = file.getOriginalFilename();
//
//        // Upload file lên Firebase Storage
//        Blob blob = StorageClient.getInstance().bucket(bucketName)
//                .create(fileName, file.getInputStream(), file.getContentType());
//
//        // Trả về URL của file đã upload
//        String fileUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
//
//        return "File uploaded successfully: " + fileUrl;
//    }
}



