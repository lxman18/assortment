
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
// import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
// import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class nykaaCategory {

    public static void main(String[] args) throws Exception {

      //   WebDriver driver = new FirefoxDriver();
        // WebDriver driver = new ChromeDriver();
        WebDriver driver = new EdgeDriver();
        driver.manage().window().maximize();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("NykaaData");

        int rowNum = 1;
        int headercount = 0;
        String[] headers = {"Category", "URL", "ProductName", "MRP", "SP", "UOM", "Offer", "Stars", "Ratings & Reviews"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Read input Excel file
        FileInputStream fis = new FileInputStream(new File(".\\input-data\\Nykaa Input-Makeup1.xlsx"));
        Workbook inputWorkbook = new XSSFWorkbook(fis);
        Sheet inputSheet = inputWorkbook.getSheetAt(0);
        int lastRow = inputSheet.getLastRowNum();

        for (int i = 1; i <= lastRow; i++) { // Assuming first row is header
            Row row = inputSheet.getRow(i);
            if (row == null || row.getCell(0) == null) continue;

            String categoryUrl = row.getCell(0).getStringCellValue();
            driver.get(categoryUrl);

            while (true) {
            	List<WebElement> products = new ArrayList<>();

            	Thread.sleep(5000);
            	try {
               products = driver.findElements(By.xpath("//div[@id='product-list-wrap']/div[@class='productWrapper css-17nge1h']"));
            	}
            	catch(NoSuchElementException ff){
            	    driver.get(categoryUrl);
                    products = driver.findElements(By.xpath("//div[@id='product-list-wrap']/div[@class='productWrapper css-17nge1h']"));

            	}
                for (int k = 0; k < products.size(); k++) {
                    String parentWindow = driver.getWindowHandle();

                    try {
                        WebElement product = products.get(k);
                        WebElement productLink = product.findElement(By.tagName("a"));

                        Set<String> oldWindows = driver.getWindowHandles();

                        productLink.click();
                        Thread.sleep(3000);
                        
                        WebDriverWait waitTab = new WebDriverWait(driver, Duration.ofSeconds(10));
                        waitTab.until(driver1 -> driver1.getWindowHandles().size() > oldWindows.size());


                        Set<String> newWindows = driver.getWindowHandles();
                        newWindows.removeAll(oldWindows);
                        String newTabHandle = newWindows.iterator().next();
                        driver.switchTo().window(newTabHandle);

                        Thread.sleep(5000);
                        
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                        WebElement visible = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class='css-1gc4x7i']")));

                        // Enter pincode
                        try {
                            WebElement pincode = driver.findElement(By.xpath("//input[@placeholder='Enter pincode']"));
                            pincode.click();
                            pincode.sendKeys("400076");

                            WebElement check = driver.findElement(By.xpath("//div[@class='css-pdkrim']/button"));
                            check.click();
                        } catch (Exception e) {
                            System.out.println("Pincode input not found or failed.");
                        }

                        String spValue = "", finalSp = "", offerValue = "NA";
                        String newName = "", mrpValue = "";
                        String Sizeuom = "NA", Rating = "NA", Review = "NA";
                        String Category = "";

                        // Category
                        try {
                            WebElement breabcrumb = driver.findElement(By.xpath("//ul[@class='css-1uxnb1o']"));
                            Category = breabcrumb.getText();
                            System.out.println(Category);
                        } catch (Exception e) {
                        }

                        Thread.sleep(2000);

                        // Name
                        try {
                            WebElement nameElement = driver.findElement(By.xpath("//h1[@class='css-1gc4x7i']"));
                            newName = nameElement.getText();
                        } catch (NoSuchElementException e) {
                            WebElement nameElement = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[1]/div[2]/div/div[1]/h1"));
                            newName = nameElement.getText();
                        }
                        System.out.println(newName);

                        String url = driver.getCurrentUrl();
                        System.out.println("headercount = " + headercount);
                        headercount++;

                        // MRP
                        try {
                            WebElement mrp = driver.findElement(By.xpath("(//div[@class='css-1d0jf8e']//span)[2]"));
                            mrpValue = mrp.getText().replace("₹", "");
                        } catch (NoSuchElementException e) {
                            WebElement mrp = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[1]/div[2]/div/div[1]/div[2]/div/span[1]/span"));
                            mrpValue = mrp.getText().replace("₹", "");
                        }
                        System.out.println(mrpValue);

                        // SP
                        try {
                            WebElement sp = driver.findElement(By.xpath("(//span[@class='css-1jczs19'])[1]"));
                            spValue = sp.getText().replace("₹", "");
                        } catch (NoSuchElementException e) {
                            WebElement sp = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[1]/div[2]/div/div[1]/div[2]/div/span[2]"));
                            spValue = sp.getText().replace("₹", "");
                        }
                        System.out.println(spValue);

                        // Offer
                        if (!mrpValue.equals(spValue)) {
                            try {
                                WebElement offer = driver.findElement(By.xpath("(//span[@class='css-bhhehx'])[1]"));
                                offerValue = offer.getText();
                            } catch (Exception e) {
                                try {
                                    WebElement offer = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[1]/div[2]/div/div[1]/div[2]/div/span[3]"));
                                    offerValue = offer.getText();
                                } catch (Exception ex) {
                                    offerValue = "NA";
                                }
                            }
                        }
                        System.out.println(offerValue);

                        // UOM
                        try {
                            WebElement sizeget = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[1]/div[2]/div/div[1]/h1/span"));
                            Sizeuom = sizeget.getText().replace("(", "").replace(")", "");
                        } catch (Exception e) {
                            Sizeuom = "NA";
                        }
                        System.out.println(Sizeuom);

                        // Rating
                        try {
                            WebElement ratingval = driver.findElement(By.xpath("//div[@class='css-m6n3ou']"));
                            Rating = ratingval.getText();
                        } catch (Exception e) {
                            Rating = "NA";
                        }
                        System.out.println(Rating);

                        // Review
                        try {
                            WebElement reviewval = driver.findElement(By.xpath("//div[@class='css-1eip5u4']"));
                            Review = reviewval.getText();
                        } catch (Exception e) {
                            Review = "NA";
                        }
                        System.out.println(Review);

                        Row dataRow = sheet.createRow(rowNum++);
                        dataRow.createCell(0).setCellValue(Category);
                        dataRow.createCell(1).setCellValue(url);
                        dataRow.createCell(2).setCellValue(newName);
                        dataRow.createCell(3).setCellValue(mrpValue);
                        dataRow.createCell(4).setCellValue(spValue);
                        dataRow.createCell(5).setCellValue(Sizeuom);
                        dataRow.createCell(6).setCellValue(offerValue);
                        dataRow.createCell(7).setCellValue(Rating);
                        dataRow.createCell(8).setCellValue(Review);

                        try (FileOutputStream fileOut = new FileOutputStream(".\\Output\\NykaaScrapedData1.xlsx")) {
                            workbook.write(fileOut);
                        }

                        driver.close();
                        driver.switchTo().window(parentWindow);

                    } catch (Exception e) {
                        System.out.println("Error processing product " + k + ": " + e.getMessage());
                     //   continue;
                        
                        driver.close();
                        driver.switchTo().window(parentWindow);
                        
                    }
                }

                if (!goToNextPageIfAvailable(driver)) {
                    break; // Break from the pagination loop and go to next category URL
                }

                Thread.sleep(1000);
            }
        }

        inputWorkbook.close();
        fis.close();
    }

    public static boolean goToNextPageIfAvailable(WebDriver driver) {
        try {
            WebElement paginationText = driver.findElement(By.cssSelector("span.css-62qqre"));
            String pageInfo = paginationText.getText();
            System.out.println("Pagination: " + pageInfo);

            String[] parts = pageInfo.replace("Page", "").trim().split("of");
            int currentPage = Integer.parseInt(parts[0].trim());
            int totalPages = Integer.parseInt(parts[1].trim());

            Thread.sleep(5000);

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, 500);");
            if (currentPage < totalPages) {
                WebElement nextPageBtn = driver.findElement(By.xpath("//a[@class='css-1zi560']"));
                nextPageBtn.click();
                Thread.sleep(3000);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Pagination not available or failed.");
            return false;
        }
    }
}
