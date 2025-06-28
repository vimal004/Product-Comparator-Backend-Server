package project.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class ScraperService {

    public String getAmazonASINByQuery(String productName) {
        try {
            String encodedQuery = URLEncoder.encode(productName, StandardCharsets.UTF_8);
            String url = "https://www.amazon.in/s?k=" + encodedQuery;

            WebDriver driver = getHeadlessDriver();
            driver.get(url);
            Thread.sleep(5000); // wait for full load

            Document doc = Jsoup.parse(driver.getPageSource());
            Element firstProduct = doc.select("div[data-asin]").stream()
                    .filter(el -> !el.attr("data-asin").isEmpty())
                    .findFirst().orElse(null);

            driver.quit();

            return firstProduct != null ? firstProduct.attr("data-asin") : "ASIN not found";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching ASIN";
        }
    }

    public String getFlipkartPIDByQuery(String productName) {
        try {
            String encodedQuery = URLEncoder.encode(productName, StandardCharsets.UTF_8);
            String url = "https://www.flipkart.com/search?q=" + encodedQuery;

            WebDriver driver = getHeadlessDriver();
            driver.get(url);
            Thread.sleep(5000); // wait for full load

            Document doc = Jsoup.parse(driver.getPageSource());
            Element productLink = doc.select("a[href*=\"pid=\"]").stream()
                    .findFirst().orElse(null);

            driver.quit();

            if (productLink != null) {
                String href = productLink.attr("href");
                String pid = href.split("pid=")[1].split("&")[0];
                return pid;
            }

            return "PID not found";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching PID";
        }
    }

    private WebDriver getHeadlessDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("user-agent=Mozilla/5.0");
        return new ChromeDriver(options);
    }
}
