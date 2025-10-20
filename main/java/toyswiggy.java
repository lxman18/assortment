

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;

public class toyswiggy {

    public static void main(String[] args) throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--start-maximized");
        options.addArguments("user-agent=Mozilla/5.0");

        WebDriver driver = new ChromeDriver(options);
        String inputFile = ".\\input-data\\ToyCategory.xlsx";
        String outputDir = ".\\Output\\Swiggy_OutputData.xlsx";

        FileInputStream fis = new FileInputStream(inputFile);
        Workbook inputWorkbook = new XSSFWorkbook(fis);
        Sheet sheet = inputWorkbook.getSheet("Sheet1");

        Workbook resultWorkbook = new XSSFWorkbook();
        Sheet resultSheet = resultWorkbook.createSheet("Results");
        Row header = resultSheet.createRow(0);
        String[] headers = {"InputPid", "InputCity", "InputName", "InputSize", "NewProductCode", "URL", "Name", "MRP", "SP", "UOM", "Multiplier", "Availability", "Offer", "NameForCheck"};
        for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);

        int outputRowIndex = 1;
        String currentPin = null;

        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String inputPid = getCellValue(row, 0);
            String city = getCellValue(row, 1);
            String name = getCellValue(row, 2);
            String size = getCellValue(row, 3);
            String productCode = getCellValue(row, 4);
            String url = getCellValue(row, 5);
            String uom = getCellValue(row, 6);
            String multiplier = getCellValue(row, 7);
            String pincode = getCellValue(row, 9);
            String nameCheck = getCellValue(row, 10);

            if (url.isEmpty() || url.equalsIgnoreCase("NA")) {
                writeResultRow(resultSheet, outputRowIndex++, inputPid, city, name, size, productCode, url, "NA", "NA", "NA", uom, multiplier, "NA", "NA", nameCheck);
                continue;
            }

            try {
                // Set location
                if (currentPin == null || !currentPin.equals(pincode)) {
                    driver.get("https://www.swiggy.com/");
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Other']"))).click();
                    Thread.sleep(500);
                    WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search for area, street name..']")));
                    input.clear();
                    input.sendKeys(pincode);
                    Thread.sleep(2000);
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='_2RwM6']"))).click();
                    currentPin = pincode;
                    Thread.sleep(2000);
                }

                // Open product URL
                driver.get(url);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                // Validate page
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'Something went wrong!')]")));
                    System.out.println("Error page detected for URL: " + url);
                    writeResultRow(resultSheet, outputRowIndex++, inputPid, city, name, size, productCode, url, "NA", "NA", "NA", uom, multiplier, "0", "NA", nameCheck);
                    continue;
                } catch (TimeoutException ignored) {}

                // Extract name
                String productName = driver.findElement(By.xpath("//div[contains(@class,'_AHZN')]")).getText();

                // Extract SP
                String sp = driver.findElement(By.xpath("//div[contains(@class,'_2XPBo')]")).getText().replace("₹", "").trim();

                // Extract MRP (fallback to SP)
                String mrp;
                try {
                    mrp = driver.findElement(By.xpath("//div[contains(@class,'_2KTMQ')]")).getText().replace("₹", "").trim();
                } catch (NoSuchElementException e) {
                    mrp = sp;
                }

                // Extract Offer
                String offer = "NA";
                try {
                    offer = driver.findElement(By.xpath("//div[contains(@class,'_1kaS2')]")).getText();
                } catch (Exception ignored) {}

                // Availability
                String pageSrc = driver.getPageSource();
                String[] unavailTexts = {"Currently Unavailable", "out of stock", "Sold Out"};
                boolean available = Arrays.stream(unavailTexts).noneMatch(pageSrc::contains);
                String availability = available ? "1" : "0";

                // Write data
                writeResultRow(resultSheet, outputRowIndex++, inputPid, city, name, size, productCode, url, productName, mrp, sp, uom, multiplier, availability, offer, nameCheck);
                System.out.println("Done -> " + url);

            } catch (Exception e) {
                e.printStackTrace();
                writeResultRow(resultSheet, outputRowIndex++, inputPid, city, name, size, productCode, url, "NA", "NA", "NA", uom, multiplier, "0", "NA", nameCheck);
            }
        }

        // Save output file
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File outDir = new File(outputDir);
        if (!outDir.exists()) outDir.mkdirs();

        FileOutputStream fos = new FileOutputStream(outputDir + "Swiggy_ToyResults_" + timeStamp + ".xlsx");
        resultWorkbook.write(fos);
        fos.close();

        driver.quit();
        System.out.println("Scraping Complete. File saved.");
    }

    private static void writeResultRow(Sheet sheet, int index, String... values) {
        Row row = sheet.createRow(index);
        for (int i = 0; i < values.length; i++) {
            row.createCell(i).setCellValue(values[i]);
        }
    }

    private static String getCellValue(Row row, int index) {
        if (row == null || row.getCell(index) == null) return "";
        Cell cell = row.getCell(index);

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue()); // Remove decimal if not needed
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

}
