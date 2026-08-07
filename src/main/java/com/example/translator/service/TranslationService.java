package com.example.translator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TranslationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String translateTextDirectly(String text, String targetLang) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        String normalizedLang = normalizeLanguageCode(targetLang);

        // Break text into smaller chunks (~500 chars) to prevent API URI limits
        List<String> chunks = splitTextIntoChunks(text, 500);
        StringBuilder translatedResult = new StringBuilder();

        for (String chunk : chunks) {
            if (chunk.trim().isEmpty()) continue;
            
            String chunkTranslation = translateChunkWithPost(chunk, normalizedLang);
            translatedResult.append(chunkTranslation).append(" ");
        }

        return translatedResult.toString().trim();
    }

    private String translateChunkWithPost(String text, String targetLang) {
        try {
            String url = "https://api.mymemory.translated.net/get";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("q", text);
            map.add("langpair", "autodetect|" + targetLang);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode responseData = root.path("responseData");
                String translatedText = responseData.path("translatedText").asText();

                if (translatedText != null && !translatedText.isEmpty() && !translatedText.equalsIgnoreCase("null")) {
                    return translatedText;
                }
            }
        } catch (Exception e) {
            System.err.println("Translation API failed for chunk: " + e.getMessage());
        }

        // Return original chunk if translation call fails
        return text; 
    }

    private List<String> splitTextIntoChunks(String text, int maxChunkSize) {
        List<String> chunks = new ArrayList<>();
        String[] sentences = text.split("(?<=[.?!\\n])"); // Split by sentence/newline
        
        StringBuilder currentChunk = new StringBuilder();

        for (String sentence : sentences) {
            if (currentChunk.length() + sentence.length() > maxChunkSize) {
                chunks.add(currentChunk.toString());
                currentChunk = new StringBuilder();
            }
            currentChunk.append(sentence);
        }

        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString());
        }

        return chunks;
    }

    public static String normalizeLanguageCode(String lang) {
        if (lang == null || lang.trim().isEmpty()) return "en";
        String cleanLang = lang.trim().toLowerCase();
        return switch (cleanLang) {
            case "kannada", "kn" -> "kn";
            case "english", "en" -> "en";
            case "hindi", "hi" -> "hi";
            case "malayalam", "ml" -> "ml";
            case "tamil", "ta" -> "ta";
            case "arabic", "ar" -> "ar";
            case "spanish", "es" -> "es";
            case "chinese", "zh" -> "zh-CN";
            case "german", "de" -> "de";
            case "french", "fr" -> "fr";
            case "japanese", "ja" -> "ja";
            default -> cleanLang;
        };
    }
}