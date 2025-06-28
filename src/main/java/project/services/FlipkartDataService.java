package project.services;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.stereotype.Service;

@Service
public class FlipkartDataService {

    public String getFlipkartProductDetails(String pid, String pincode) {
        try {
            HttpResponse<String> response = Unirest.get("https://real-time-flipkart-data2.p.rapidapi.com/product-details")
                    .queryString("pincode", pincode)
                    .queryString("pid", pid)
                    .header("x-rapidapi-host", "real-time-flipkart-data2.p.rapidapi.com")
                    .header("x-rapidapi-key", "84fb6c0a96msh07857e9017e02f1p18eabfjsn827727333c2d")
                    .asString();

            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Failed to fetch product details\"}";
        }
    }
}
