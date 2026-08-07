package com.example.translator.controller;

import com.example.translator.model.TranslationResponse;
import com.example.translator.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    @PostMapping("/translate")
    public ResponseEntity<TranslationResponse> translateDirectText(@RequestBody Map<String, String> request) {
        try {
            String text = request.get("text");
            String targetLang = request.get("targetLang");

            String result = translationService.translateTextDirectly(text, targetLang);
            return ResponseEntity.ok(new TranslationResponse(text, result, targetLang));
        } catch (Exception e) {
            TranslationResponse err = new TranslationResponse();
            err.setError("Translation failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }
}