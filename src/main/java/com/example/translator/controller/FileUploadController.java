package com.example.translator.controller;

import com.example.translator.model.TranslationResponse;
import com.example.translator.service.TextExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Autowired
    private TextExtractionService textExtractionService;

    @PostMapping("/file")
    public ResponseEntity<TranslationResponse> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("targetLang") String targetLang) {
        try {
            String translatedResult = textExtractionService.extractAndTranslate(file, targetLang);
            return ResponseEntity.ok(new TranslationResponse(translatedResult));
        } catch (Exception e) {
            TranslationResponse err = new TranslationResponse();
            err.setError("Error processing file: " + e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }
}