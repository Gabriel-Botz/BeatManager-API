package br.com.gabriel.beatmanager.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.gabriel.beatmanager.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping
@Tag(name = "Upload", description = "Upload e exclusão de imagens")
public class UploadController {

    private final CloudinaryService cloudinaryService;

    public UploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upload de imagem", description = "Faz upload de uma imagem para o Cloudinary e retorna a URL")
    @ApiResponse(responseCode = "200", description = "Upload realizado com sucesso")
    public Map<String, String> upload(@RequestPart("file") MultipartFile file) {
        String url = cloudinaryService.uploadImage(file);
        return Map.of("url", url);
    }

    @DeleteMapping("/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar imagem", description = "Deleta uma imagem do Cloudinary pela URL")
    @ApiResponse(responseCode = "204", description = "Imagem deletada com sucesso")
    public void delete(@RequestParam("imageUrl") String imageUrl) {
        cloudinaryService.deleteImage(imageUrl);
    }
}
