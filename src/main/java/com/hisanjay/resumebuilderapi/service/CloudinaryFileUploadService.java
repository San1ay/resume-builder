package com.hisanjay.resumebuilderapi.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Qualifier("cloudinaryUploader")
@RequiredArgsConstructor
public class CloudinaryFileUploadService implements FileUploadService {

    private final Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile file) throws IOException {

        Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("resource_type", "image"));

        return result.get("secure_url").toString();
    }
}