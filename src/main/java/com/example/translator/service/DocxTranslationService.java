package com.example.translator.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class DocxTranslationService {

    @Autowired
    private TranslationService translationService;

    public String extractAndTranslateDocx(File docxFile, String targetLang) throws IOException {
        String normalizedLang = TranslationService.normalizeLanguageCode(targetLang);

        StringBuilder fullText = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(docxFile);
             XWPFDocument document = new XWPFDocument(fis)) {

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String pText = paragraph.getText();
                if (pText != null && !pText.trim().isEmpty()) {
                    String translated = translationService.translateTextDirectly(pText, normalizedLang);
                    fullText.append(translated).append("\n");
                }
            }
        }

        return fullText.toString();
    }
}