package project.services;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.stereotype.Service;

@Service
public class AmazonDataService {

    private static final String API_URL = "https://prod.api.market/api/v1/openwebninja/real-time-amazon-data/product-details";
    private static final String API_KEY = "cmc9dymot0007jy049b2w11i5";

    public String getAmazonProductDetails(String asin, String country) {
        try {
            var request = Unirest.get(API_URL)
                    .queryString("asin", asin)
                    .header("accept", "application/json")
                    .header("x-magicapi-key", API_KEY);

            if (country != null) {
                request.queryString("country", country);
            }

            HttpResponse<String> response = request.asString();
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to fetch product details for ASIN: " + asin + "\"}";
        }
    }
}
