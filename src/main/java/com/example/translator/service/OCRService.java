package com.example.translator.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@Service
public class OCRService {

    public String extractTextFromImage(MultipartFile file) throws TesseractException {
        ITesseract tesseract = new Tesseract();
        // Sets datapath to root tessdata folder in project
        tesseract.setDatapath("tessdata"); 
        tesseract.setLanguage("eng+kan");

        File convFile = convertMultipartToFile(file);
        try {
            return tesseract.doOCR(convFile);
        } finally {
            if (convFile.exists()) {
                convFile.delete();
            }
        }
    }

    private File convertMultipartToFile(MultipartFile file) {
        try {
            File convFile = File.createTempFile("ocr_", "_" + file.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(convFile)) {
                fos.write(file.getBytes());
            }
            return convFile;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert image file for OCR: " + e.getMessage());
        }
    }
}