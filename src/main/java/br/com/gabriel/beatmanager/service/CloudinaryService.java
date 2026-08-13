package br.com.gabriel.beatmanager.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile file) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("resource_type", "image"));
            return result.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao fazer upload da imagem: " + e.getMessage());
        }
    }

    public void deleteImage(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar imagem: " + e.getMessage());
        }
    }

    private String extractPublicId(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String filename = parts[parts.length - 1];
        return filename.split("\\.")[0];
    }
}
