package com.step.stupid.social.network.service.impl;

import com.step.stupid.social.network.exception.BadRequestException;
import com.step.stupid.social.network.exception.FileStorageException;
import com.step.stupid.social.network.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Path imageStorageLocation;

    @Value("${upload.path}")
    private String uploadPath;

    @PostConstruct
    public void createPathToImageStorageLocation() {
        this.imageStorageLocation = Paths.get(uploadPath)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.imageStorageLocation);
        } catch (IOException e) {
            throw new FileStorageException(String.format("Directory %s can not be created", uploadPath));
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        if (file == null) {
            throw new BadRequestException("");
        }
        final String[] extensions = {"jpeg", "jpg", "png"};
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String originalFileName = UUID.randomUUID().toString() + fileName;

        if (fileName.contains("..")) {
            throw new BadRequestException("");
        }
        boolean isMatches = Arrays.stream(extensions)
                .anyMatch(extension -> file.getOriginalFilename().endsWith(extension));

        if (!isMatches) {
            throw new FileStorageException("");
        }
        Path targetLocation = this.imageStorageLocation.resolve(originalFileName);

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("");
        }
        return originalFileName;
    }

    @PreDestroy
    public void deleteExistDirectory() {
        try {
            Files.deleteIfExists(this.imageStorageLocation);
        } catch (IOException e) {
            throw new FileStorageException(String.format("Directory %s can not be deleted", uploadPath));
        }
    }
}
