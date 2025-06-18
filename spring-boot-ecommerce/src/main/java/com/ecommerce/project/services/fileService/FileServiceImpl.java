package com.ecommerce.project.services.fileService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl  implements FileService{

    public String uploadImage(String path, MultipartFile image) throws IOException {

        String originalFilename = image.getOriginalFilename();

        String randomId = UUID.randomUUID().toString();

        String fileName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));

        String filePath = path + File.pathSeparator + fileName;

        File folder = new File(path);

        if(!folder.exists()) {
            folder.mkdirs();
        }

        Files.copy(image.getInputStream(), Paths.get(filePath));

        return fileName;
    }
}
