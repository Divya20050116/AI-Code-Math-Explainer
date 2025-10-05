package com.example.explainer.explainer.controller;

import org.springframework.web.bind.annotation.*;
import com.example.explainer.explainer.service.GeminiService;

@RestController
@RequestMapping("/api")
public class ExplainerController {

    private final GeminiService geminiService;

    public ExplainerController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/explain")
    public String explain(@RequestBody RequestDTO request) {
        return geminiService.getExplanation(request.getInput(), request.getType());
    }
}

// ✅ Keep DTO inside same file (or create a separate class if you want)
class RequestDTO {
    private String input;
    private String type;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}