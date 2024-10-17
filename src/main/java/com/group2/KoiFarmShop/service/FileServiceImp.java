package com.group2.KoiFarmShop.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface FileServiceImp {

    public boolean saveFile(MultipartFile file);
    public Resource loadFile(String filename);

}
