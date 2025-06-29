# 🛠️ Product Comparison Backend (Spring Boot + Selenium + Executor Service)

This backend service powers the Product Comparison App. It fetches real-time product data from **Amazon** and **Flipkart** using **Selenium**, compares the results using Gemini AI, and returns a detailed JSON report.

---

## 🌟 Features

- ✅ Scrapes live Amazon & Flipkart listings using **Selenium WebDriver**
- ✅ Compares features, specs, pricing, and more
- ✅ Uses **Gemini 1.5 Flash** for intelligent comparison
- ✅ Clean, structured API response (JSON)
- ✅ Deployable to Render without Docker

---

## 🚀 Local Development

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/product-comparison-backend.git
cd product-comparison-backend

2. Requirements

    Java 17

    Maven

    Chrome or Chromium browser

    ChromeDriver installed and in PATH

    💡 Download ChromeDriver from https://sites.google.com/chromium.org/driver

3. Run the App Locally

./mvnw spring-boot:run

Or (if Maven is installed):

mvn spring-boot:run

4. Test the API

curl -X POST http://localhost:8080/combined \
  -H "Content-Type: application/json" \
  -d '"wireless headphones"'

🧪 API Endpoint

POST /combined
Request Body: a raw JSON string containing the product name

Example:

"laptop backpack"

🧠 How It Works

    🕵️ Selenium scrapes Amazon & Flipkart using headless Chrome

    📦 Product JSONs are created using custom logic

    🧠 Gemini AI compares product features and pricing

    📤 JSON summary is returned to the frontend
