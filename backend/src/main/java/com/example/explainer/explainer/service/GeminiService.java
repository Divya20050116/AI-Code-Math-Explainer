package com.example.explainer.explainer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

        // replace with env var or secure config in production
        private static final String GEMINI_API_KEY = System.getenv().getOrDefault("GEMINI_API_KEY",
                        "YOUR_API_KEY_HERE");
        private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
                        + GEMINI_API_KEY;

        private final ObjectMapper mapper = new ObjectMapper();

        public String getExplanation(String input, String type) {
                try (CloseableHttpClient client = HttpClients.createDefault()) {

                        HttpPost post = new HttpPost(GEMINI_API_URL);
                        post.setHeader("Content-Type", "application/json");

                        // Very explicit instruction: return valid JSON only, short output.
                        String userMessage = "You are a concise assistant. Respond ONLY with valid JSON and nothing else.\n"
                                        + "Format exactly: {\"explanation\":\"...\", \"summary\":[\"s1\",\"s2\",\"s3\"]}\n"
                                        + "Constraints: explanation = at most 2 short sentences. Each summary item = max 6 words.\n\n"
                                        + "Now explain the following input:\n" + input;

                        // Request body: contents -> parts -> text and token/temperature limits for
                        // brevity
                        Map<String, Object> requestBody = Map.of(
                                        "contents", List.of(
                                                        Map.of("parts", List.of(Map.of("text", userMessage)))),
                                        "maxOutputTokens", 250,
                                        "temperature", 0.0,
                                        "candidateCount", 1);

                        String jsonBody = mapper.writeValueAsString(requestBody);
                        post.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

                        String rawResponse = client.execute(post, new BasicHttpClientResponseHandler());
                        // System.out.println("Raw Response: " + rawResponse); // optional debug

                        // Extract the model text
                        JsonNode root = mapper.readTree(rawResponse);
                        JsonNode textNode = root.path("candidates")
                                        .get(0)
                                        .path("content")
                                        .path("parts")
                                        .get(0)
                                        .path("text");

                        String modelText = textNode.isMissingNode() ? "" : textNode.asText("").trim();

                        // 1) Try to parse the modelText as JSON (best case: model obeyed instructions)
                        try {
                                JsonNode parsed = mapper.readTree(modelText);
                                // If it has the keys we want, return compact JSON string
                                if (parsed.has("explanation")) {
                                        return parsed.toString();
                                }
                        } catch (Exception ignored) {
                                // not JSON — fall through to fallback handling
                        }

                        // 2) If the model returned some text but not JSON, attempt to extract a short
                        // part
                        if (!modelText.isEmpty()) {
                                // Try to find a JSON-like substring first (some models wrap JSON in ``` or
                                // extra text)
                                int start = modelText.indexOf('{');
                                int end = modelText.lastIndexOf('}');
                                if (start >= 0 && end > start) {
                                        String maybeJson = modelText.substring(start, end + 1);
                                        try {
                                                JsonNode parsed2 = mapper.readTree(maybeJson);
                                                if (parsed2.has("explanation")) {
                                                        return parsed2.toString();
                                                }
                                        } catch (Exception ignored) {
                                        }
                                }

                                // Fallback: return a short first paragraph (trimmed)
                                String firstPara;
                                int doubleNl = modelText.indexOf("\n\n");
                                if (doubleNl > 0)
                                        firstPara = modelText.substring(0, doubleNl).trim();
                                else {
                                        firstPara = modelText;
                                        // if it's very long, cut to 700 chars
                                        if (firstPara.length() > 700)
                                                firstPara = firstPara.substring(0, 700) + "...";
                                }

                                // Build a JSON fallback with single explanation paragraph and empty summary
                                ObjectNode fallback = mapper.createObjectNode();
                                fallback.put("explanation", firstPara);
                                fallback.putArray("summary").add("See full explanation").add("Use shorter input")
                                                .add("Refine request");
                                return fallback.toString();
                        }

                        // 3) No content returned -> generic fallback JSON
                        ObjectNode empty = mapper.createObjectNode();
                        empty.put("explanation", "No explanation returned by the model.");
                        empty.putArray("summary").add("Model returned no text").add("Check API key/limits")
                                        .add("Try shorter prompt");
                        return empty.toString();

                } catch (Exception e) {
                        e.printStackTrace();
                        // Return error as JSON so frontend can handle it
                        ObjectNode err = mapper.createObjectNode();
                        err.put("error", "Error while calling Gemini API: " + e.getMessage());
                        return err.toString();
                }
        }
}