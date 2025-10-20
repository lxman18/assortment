package Blinkit;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Scanner;

public class LazadaScraperFromExcel {

    private static String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    private static String outputFile = "./Output/LazadaProducts_" + timestamp + ".xlsx";
    private static String inputFile = "./input-data/KS.xlsx"; // Excel with product URLs

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        Workbook outputWorkbook = new XSSFWorkbook();
        Sheet sheet = outputWorkbook.createSheet("Lazada Products");

        // Headers
        String[] headers = {"Scraped Count", "Product URL", "Product Name", "Brand Name",
                "Selling Price", "MRP", "Offer", "Promotions", "Delivery Option", "Warranty", "SKU"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) headerRow.createCell(i).setCellValue(headers[i]);

        int rowCount = 1;
        int scrapedCount = 0;

        try (FileInputStream fis = new FileInputStream(inputFile)) {
            Workbook inputWorkbook = new XSSFWorkbook(fis);
            Sheet inputSheet = inputWorkbook.getSheetAt(0);

            driver.manage().window().maximize();

            // Loop through product URLs in Excel
            for (int i = 1; i <= inputSheet.getLastRowNum(); i++) {
                Row row = inputSheet.getRow(i);
                if (row == null) continue;
                Cell urlCell = row.getCell(0);
                if (urlCell == null) continue;
                String productUrl = urlCell.getStringCellValue().trim();
                if (productUrl.isEmpty()) continue;

                driver.get(productUrl);
                handleCaptcha(driver);

                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h1[contains(@class,'pdp-mod-product-badge-title')]")));

                scrapedCount++;
                System.out.println("📌 Scraping Product #" + scrapedCount + ": " + productUrl);

                Row outRow = sheet.createRow(rowCount++);
                outRow.createCell(0).setCellValue(scrapedCount);
                outRow.createCell(1).setCellValue(productUrl);
                outRow.createCell(2).setCellValue(safeGetText(driver, By.xpath("//h1[contains(@class,'pdp-mod-product-badge-title')]")));
                outRow.createCell(3).setCellValue(safeGetText(driver, By.xpath("//a[contains(@class,'pdp-product-brand')]")));
                outRow.createCell(4).setCellValue(safeGetText(driver, By.xpath("(//span[contains(@class,'pdp-v2-product-price-content-salePrice-amount')])[1]")));
                outRow.createCell(5).setCellValue(safeGetText(driver, By.xpath("(//span[contains(@class,'pdp-v2-product-price-content-originalPrice-amount')])[1]")));
                outRow.createCell(6).setCellValue(safeGetText(driver, By.xpath("(//span[contains(@class,'pdp-v2-product-price-content-originalPrice-discount')])[1]")));
                outRow.createCell(7).setCellValue(safeGetText(driver, By.xpath("//div[contains(@class,'promotion-tag-item-v2')]")));
                outRow.createCell(8).setCellValue(safeGetText(driver, By.xpath("//span[contains(@class,'delivery__remain-text')]")));
                outRow.createCell(9).setCellValue(safeGetText(driver, By.xpath("//span[contains(@class,'warranty-v2-label-text')]")));
                outRow.createCell(10).setCellValue(safeGetText(driver, By.xpath("//li[span[normalize-space(text())='SKU']]/div[@class='key-value']")));

                saveWorkbook(outputWorkbook);
            }

            inputWorkbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);
            saveWorkbook(outputWorkbook);
            try { outputWorkbook.close(); } catch (IOException ignored) {}
            driver.quit();
        }

        System.out.println("🎯 Total products scraped: " + scrapedCount);
    }

    private static void handleCaptcha(WebDriver driver) {
        try {
            if (driver.findElements(By.xpath("//iframe[contains(@src,'captcha')]")).size() > 0 ||
                driver.findElements(By.xpath("//div[contains(text(),'Please verify')]")).size() > 0 ||
                driver.findElements(By.xpath("//div[contains(@class,'captcha')]")).size() > 0) {

                System.out.println("⚠️ CAPTCHA detected! Please solve it manually and press ENTER to continue...");
                new Scanner(System.in).nextLine();
            }
        } catch (Exception ignored) {}
    }

    private static String safeGetText(WebDriver driver, By locator) {
        try { return driver.findElement(locator).getText().trim(); }
        catch (Exception e) { return "N/A"; }
    }

    private static void saveWorkbook(Workbook workbook) {
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            workbook.write(out);
            System.out.println("💾 Excel saved: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
