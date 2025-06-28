package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.services.FlipkartDataService;
import project.services.ScraperService;

@RestController
@RequestMapping("/flipkart")
public class FlipkartController {
    @Autowired
    FlipkartDataService flipkartDataService;
    @Autowired
    ScraperService scraperService;

    @GetMapping("/data")
    public String getProductData(){
        return flipkartDataService.getFlipkartProductDetails(scraperService.getFlipkartPIDByQuery("Iphone"),"600100");
    }
}
