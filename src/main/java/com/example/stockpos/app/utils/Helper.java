package com.example.stockpos.app.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class Helper {

    public static String uploadImage(MultipartFile file, String uploadDir) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Create directory if it doesn't exist
        Path root = Paths.get(uploadDir);
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        // Generate a random unique filename to avoid collisions
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString() + extension;

        // Copy file to target path
        Path targetPath = root.resolve(filename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    public static String updateImage(MultipartFile file, String oldImageName, String uploadDir) throws IOException {
        String newImageName = uploadImage(file, uploadDir);
        
        // If a new image was successfully uploaded, delete the old one
        if (newImageName != null && oldImageName != null && !oldImageName.isEmpty()) {
            deleteFile(oldImageName, uploadDir);
        }
        
        return newImageName != null ? newImageName : oldImageName;
    }

    public static void deleteFile(String fileName, String uploadDir) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log the error or handle it as needed
            System.err.println("Could not delete file: " + fileName + ". Error: " + e.getMessage());
        }
    }

    public static String generateCode(String prefix) {
        return prefix + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
