package com.example.translator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
public class TextExtractionService {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private WordService wordService;

    @Autowired
    private OCRService ocrService;

    @Autowired
    private TranslationService translationService;

    public String extractAndTranslate(MultipartFile file, String targetLang) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }

        String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        String extractedRawText = "";

        if (fileName.endsWith(".pdf")) {
            extractedRawText = pdfService.extractTextFromPdf(file);
        } else if (fileName.endsWith(".docx")) {
            extractedRawText = wordService.extractTextFromDocx(file);
        } else if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            extractedRawText = ocrService.extractTextFromImage(file);
        } else {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
                extractedRawText = reader.lines().collect(Collectors.joining("\n"));
            }
        }

        if (extractedRawText == null || extractedRawText.trim().isEmpty()) {
            return "No readable text found in document.";
        }

        return translationService.translateTextDirectly(extractedRawText, targetLang);
    }
}