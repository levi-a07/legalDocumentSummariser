package com.levi.clauseClear.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SummarisationService {
    private final RestTemplate restTemplate;

    @Value("${huggingface.api.token}")
    private String hfApiToken;
    private static final String HF_API_URL = "https://api-inference.huggingface.co/models/facebook/bart-large-cnn";

    public SummarisationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public List<String> summarize(List<String> chunks) throws Exception {
        List<String> summaries = new ArrayList<>();
        for (String chunk : chunks) {
            String summary = requestSummary(chunk);
            summaries.add(summary);
        }
        return summaries;
    }

    public String requestSummary(String textChunk) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(hfApiToken);

        String prompt = "Legal attorney, keep eye on every small detail, summarize: ";
        String jsonPayload = "{\"inputs\": \"" + escapeJson(prompt + textChunk) + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(HF_API_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());

                if (root.isArray() && !root.isEmpty()) {
                    return root.get(0).get("summary_text").asText();
                } else {
                    throw new Exception("Unexpected response format from Hugging Face API");
                }
            } else {
                throw new Exception("Hugging Face API call failed with status: " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            log.error("Error during Hugging Face API call: {}", e.getMessage(), e);
            throw new Exception("Error during Hugging Face API call: " + e.getMessage(), e);
        }

    }

    private String escapeJson(String text) {
        return text.replace("\"", "\\\"").replaceAll("\\s+", " ").trim();
    }
}
