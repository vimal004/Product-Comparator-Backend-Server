# 🛠️ Product Comparison Backend (Spring Boot + Selenium + Gemini AI)

This is the backend for the Product Comparison App. It fetches real-time data from **Amazon** and **Flipkart** using **Selenium**, compares the products using **Gemini AI**, and returns a smart, structured response.

---

## 🌟 Features

- 🔍 Scrapes live Amazon & Flipkart listings with **Selenium**
- ⚡ Uses **ExecutorService** for parallel scraping (fast!)
- 🧠 Sends data to **Gemini AI** for intelligent comparison
- 📦 Returns structured product analysis in JSON format
- 🚀 Works on **Render.com** with headless Chrome setup

---

## 🚀 Local Development

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/product-comparison-backend.git
cd product-comparison-backend

2. Requirements

    Java 17+

    Maven or ./mvnw wrapper

    Google API Key + Gemini model access (if using)

    Chrome or Chromium browser

    ChromeDriver (installed and in PATH)

    💡 Get ChromeDriver: https://sites.google.com/chromium.org/driver

3. Run the App Locally

./mvnw spring-boot:run

Or (with installed Maven):

mvn spring-boot:run

🧪 API Usage
POST /combined

Request body (raw string):

"wireless headphones"

Response:

{
  "comparison_table": [...],
  "differences_explained": "...",
  "value_analysis": "...",
  ...
}
