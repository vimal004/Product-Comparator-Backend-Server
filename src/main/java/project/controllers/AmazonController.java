package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.services.AmazonDataService;
import project.services.ScraperService;

@RestController
@RequestMapping("/amazon")
public class AmazonController {
    @Autowired
    AmazonDataService amazonDataService;
    @Autowired
    ScraperService scraperService;

    @GetMapping("/data")
    public String getProductDetails() {
        return amazonDataService.getAmazonProductDetails(scraperService.getAmazonASINByQuery("Iphone"),"IN");
    }
}
