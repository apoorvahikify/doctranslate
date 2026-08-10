package com.example.translator.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class OCRService {

    public String extractTextFromImage(MultipartFile file) throws TesseractException {

        ITesseract tesseract = new Tesseract();

        tesseract.setDatapath("tessdata");

        // Image contains Kannada text
        tesseract.setLanguage("kan");

        // Better for text arranged in several lines
        tesseract.setPageSegMode(6);

        File imageFile = prepareImage(file);

        try {
            return tesseract.doOCR(imageFile);
        } finally {
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }
    }

    private File prepareImage(MultipartFile file) {

        try {

            BufferedImage original =
                    ImageIO.read(file.getInputStream());

            if (original == null) {
                throw new RuntimeException("Could not read image.");
            }

            // Increase image size for better Kannada OCR
            int width = original.getWidth() * 4;
            int height = original.getHeight() * 4;

            BufferedImage enlarged =
                    new BufferedImage(
                            width,
                            height,
                            BufferedImage.TYPE_INT_RGB
                    );

            Graphics2D g = enlarged.createGraphics();

            g.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC
            );

            g.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );

            g.drawImage(original, 0, 0, width, height, null);
            g.dispose();

            // Convert to grayscale
            BufferedImage gray =
                    new BufferedImage(
                            width,
                            height,
                            BufferedImage.TYPE_BYTE_GRAY
                    );

            Graphics2D grayGraphics = gray.createGraphics();
            grayGraphics.drawImage(enlarged, 0, 0, null);
            grayGraphics.dispose();

            // Save processed image
            File output =
                    File.createTempFile("ocr_", ".png");

            ImageIO.write(gray, "png", output);

            return output;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to prepare image for OCR: "
                            + e.getMessage()
            );
        }
    }
}