package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import project.services.AmazonDataService;
import project.services.FlipkartDataService;
import project.services.ScraperService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping("/combined")
public class CombinedController {

    @Autowired
    FlipkartDataService flipkartDataService;

    @Autowired
    AmazonDataService amazonDataService;

    @Autowired
    ScraperService scraperService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("")
    public ResponseEntity<?> getCombinedAndComparedProductData(@RequestBody Map<String, String> Body) {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            String product=Body.get("product");
            Callable<Map<String, Object>> amazonTask = () -> {
                String asin = scraperService.getAmazonASINByQuery(product);
                String amazonJson = amazonDataService.getAmazonProductDetails(asin, "IN");
                return objectMapper.readValue(amazonJson, Map.class);
            };

            Callable<Map<String, Object>> flipkartTask = () -> {
                String pid = scraperService.getFlipkartPIDByQuery(product);
                String flipkartJson = flipkartDataService.getFlipkartProductDetails(pid, "600100");
                return objectMapper.readValue(flipkartJson, Map.class);
            };

            List<Future<Map<String, Object>>> results = executorService.invokeAll(List.of(amazonTask, flipkartTask));
            Map<String, Object> amazon = results.get(0).get();
            Map<String, Object> flipkart = results.get(1).get();
            executorService.shutdown();

            // Construct prompt
            String prompt = buildPrompt(amazon, flipkart);

            // Call Gemini 1.5 Flash backend
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> body = new HashMap<>();
            body.put("prompt", prompt);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            String geminiUrl = "https://gemini-backend-uiuz.onrender.com/gemini";
            ResponseEntity<Map> geminiResponse = restTemplate.postForEntity(geminiUrl, entity, Map.class);

            return ResponseEntity.ok(geminiResponse.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    private String buildPrompt(Map<String, Object> amazon, Map<String, Object> flipkart) {
        return """
You are an expert consumer research analyst with deep domain knowledge across all product categories — including electronics, home & kitchen, fashion, sports, books, and more. Your job is to help buyers make **intelligent, value-driven, and context-aware purchasing decisions**.

You have received two product listings for the **same product type** from **Amazon** and **Flipkart**. Perform a **comprehensive, multi-dimensional comparison** of both listings, focusing on both **explicit specs** and **subtle consumer implications**.

Go beyond surface-level features and create a report with:

---

### 📊 1. Comparison Table
Generate a **side-by-side tabular JSON comparison** of the most relevant attributes for this product category (e.g., material, price, features, size, ratings, warranty, shipping, performance, etc.).

- Ensure each row compares one meaningful feature or spec.
- Normalize units (e.g., cm vs inches) if possible.

---

### 🧠 2. Feature-Level Insight
For each significant feature (e.g., durability, price, aesthetics, usability, performance, build quality, etc.):
- Provide a **short insight or critique** highlighting which product does it better and **why**.
- Capture trade-offs not obvious at first glance.

---

### ⚖️ 3. Smart Trade-off Evaluation
Detect **subtle trade-offs** like:
- Does one product sacrifice durability for aesthetics?
- Is one option overpriced for what it offers?
- Are premium features actually useful for the target user?

---

### 🛍️ 4. Value & Utility Breakdown
Analyze:
- **Which product offers better value for money**, and
- Which one **fits real-world usage scenarios** better (e.g., daily use, gifting, heavy-duty, aesthetics).

---

### 🧩 5. Buyer Personas & Use Cases
Generate 2–3 **buyer personas** (e.g., student, athlete, budget shopper, brand loyalist, minimalist) and recommend which product better fits each one — and why.

---

### 💬 6. Marketing Language Detection
Scan both listings for **persuasive, exaggerated, or vague marketing terms** (e.g., “game-changer”, “ultra-premium”, “revolutionary feel”). Flag them.

---

### ✅ 7. Pros & Cons Summary
Give concise, realistic **pros and cons** lists for both listings — avoid generic statements. Focus on differentiators that matter to buyers.

---

### 🧾 8. Final Verdict
Make a confident recommendation:
- **Which product is better and why?**
- If neither is strictly better, offer a **decision framework**: “Choose A if you care about X; choose B if you care about Y.”

---

### 📂 Output Format (JSON):
{
  "comparison_table": [ {"feature": "Price", "amazon": "...", "flipkart": "..."}, ... ],
  "differences_explained": [ {"feature": "Build Quality", "analysis": "..."}, ... ],
  "smart_tradeoffs": [ "..." ],
  "value_analysis": "Product A is better for everyday users who prioritize...", 
  "buyer_personas": {
    "amazon": ["Ideal for...", "Also good for..."],
    "flipkart": ["Better suited for...", "Not great for..."]
  },
  "pros_cons": {
    "amazon": { "pros": ["..."], "cons": ["..."] },
    "flipkart": { "pros": ["..."], "cons": ["..."] }
  },
  "marketing_language_flags": {
    "amazon": ["..."], "flipkart": ["..."]
  },
  "verdict": "The better option is ... because ..."
}

---

Use real-world reasoning, not just feature matching. Your analysis should **feel like a conversation with an expert reviewer who understands buyer psychology**.

Here is the raw product data:

Amazon Product:
""" + toJsonPretty(amazon) + "\n\nFlipkart Product:\n" + toJsonPretty(flipkart);
    }



    private String toJsonPretty(Map<String, Object> map) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } catch (Exception e) {
            return map.toString();
        }
    }
}
