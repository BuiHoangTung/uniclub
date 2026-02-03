package com.myproject.uniclub.service.imp;

import com.myproject.uniclub.exception.SaveFileException;
import com.myproject.uniclub.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImp implements FileService {

    @Value("${root.path}")
    private String root;

    @Override
    public void saveFile(MultipartFile file) {
        Path rootPath = Paths.get(root);
        try {
            /**
             * Sử dụng createDirectories lợi hơn createDirectory
             * là nó tự check folder có tồn tại hay không nếu chưa có thì nó sẽ tự động tạo
             */
            Files.createDirectories(rootPath);

            Files.copy(file.getInputStream(), rootPath.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Lỗi lưu file.");
            throw new SaveFileException(e.getMessage());
        }
    }
}
