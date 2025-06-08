package com.levi.clauseClear.controller;

import com.levi.clauseClear.service.SummarisationService;
import com.levi.clauseClear.service.TextChunkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
public class FileUploadController {
    public static final int MAX_CHAR_LIMIT = 1500;
    private final Tika tika;
    private final TextChunkService textChunkService;
    private final SummarisationService summarisationService;

    public FileUploadController(Tika tika, TextChunkService textChunkService, SummarisationService summarisationService) {
        this.tika = tika;
        this.textChunkService = textChunkService;
        this.summarisationService = summarisationService;
    }

    @RequestMapping("/upload-and-summarize")
    public ResponseEntity<?> uploadAndSummarize(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file");
        }
        log.info("Uploading file: " + file.getOriginalFilename());
        try {
            String extractedText = tika.parseToString(file.getInputStream());
            List<String> chunks = textChunkService.splitIntoChunks(extractedText, MAX_CHAR_LIMIT);

            List<String> summaries = summarisationService.summarize(chunks);
            log.info("Succesfully Summarised {} into {} chunks", file.getOriginalFilename(), summaries.size());
            return ResponseEntity.ok(summaries);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
}
