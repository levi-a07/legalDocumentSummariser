package com.levi.clauseClear.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TextChunkService {

    public TextChunkService() {
    }

    public List<String> splitIntoChunks(String text, int maxCharLimit) {
        String[] sentences = text.split("(?<=[.!?])\\s+");
        List<String> chunks = new ArrayList<>();

        StringBuilder currentChunk = new StringBuilder();

        for (String sentence : sentences) {
            if (currentChunk.length() + sentence.length() + 1 <= maxCharLimit) {
                if (!currentChunk.isEmpty()) {
                    currentChunk.append(" ");
                }
                currentChunk.append(sentence);
            } else {
                chunks.add(currentChunk.toString().trim());
                currentChunk = new StringBuilder(sentence);
            }
        }

        if (!currentChunk.isEmpty()) {
            chunks.add(currentChunk.toString().trim());
        }

        return chunks;
    }
}
