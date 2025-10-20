package scheduleNew;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;


	
		import java.util.Calendar;
		import java.util.concurrent.Executors;
		import java.util.concurrent.ScheduledExecutorService;
		import java.util.concurrent.TimeUnit;

		public class scheduleSupertailDel {
		    public static void main(String[] args) {
		    	ChromeOptions options = new ChromeOptions();
		    	options.addArguments("--headless"); // Run Chrome in headless mode
		    	options.addArguments("--disable-gpu"); // Disable GPU acceleration
		    	options.addArguments("--window-size=1920,1080");   //Set window size to full HD
		    	options.addArguments("--start-maximized");
		    	options.addArguments("--window-size=1920,1080");   //Set window size to full HD
		    	options.addArguments("--start-maximized");	

		        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		        // Schedule the task to run every day at 7:00 AM
		        Calendar now = Calendar.getInstance();
		        Calendar nextRunTime = Calendar.getInstance();
		        nextRunTime.set(Calendar.HOUR_OF_DAY, 3);							//3.45
		        nextRunTime.set(Calendar.MINUTE, 45);
		        nextRunTime.set(Calendar.SECOND, 0);

		        long initialDelay = nextRunTime.getTimeInMillis() - now.getTimeInMillis();
		        if (initialDelay < 0) {
		            initialDelay += 24 * 60 * 60 * 1000; // If it's already past 7 AM, schedule for the next day
		        }

		        scheduler.scheduleAtFixedRate(() -> {
		            try {
		                System.out.println("Starting web scraping task...");
		                scheduleSupertailDel.runWebScraping();
		                System.out.println("Web scraping task completed.");
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        }, initialDelay, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
		    }
		  		  

		    public static void runWebScraping() throws Exception{
		    	 ChromeOptions options = new ChromeOptions();
		         options.addArguments("--disable-gpu", "--window-size=1920x1080");
		         WebDriver driver = new ChromeDriver(options);
		         
		      //   driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(25));

		         try {
		             String filePath = ".\\input-data\\superTail Input data_Updated.xlsx";
		             FileInputStream file = new FileInputStream(filePath);
		             Workbook urlsWorkbook = new XSSFWorkbook(file);
		             Sheet urlsSheet = urlsWorkbook.getSheet("Data1");//delhi
		             int rowCount = urlsSheet.getPhysicalNumberOfRows();

		             List<String> inputPid = new ArrayList<>(), InputCity = new ArrayList<>(), InputName = new ArrayList<>(), InputSize = new ArrayList<>(),
		                     NewProductCode = new ArrayList<>(), uRL = new ArrayList<>(), UOM = new ArrayList<>(), Mulitiplier = new ArrayList<>(),
		                     Availability = new ArrayList<>(), Pincode = new ArrayList<>(), NameForCheck = new ArrayList<>();

		             for (int i = 1; i < rowCount; i++) {
		                 Row row = urlsSheet.getRow(i);
		                 if (row == null) continue;

		                 String url = getCellValue(row, 5);
		                 if (!url.isEmpty()) {
		                     inputPid.add(getCellValue(row, 0));
		                     InputCity.add(getCellValue(row, 1));
		                     InputName.add(getCellValue(row, 2));
		                     InputSize.add(getCellValue(row, 3));
		                     NewProductCode.add(getCellValue(row, 4));
		                     uRL.add(url);
		                     UOM.add(getCellValue(row, 6));
		                     Mulitiplier.add(getCellValue(row, 7));
		                     Availability.add(getCellValue(row, 8));
		                     Pincode.add(getCellValue(row, 9));
		                     NameForCheck.add(getCellValue(row, 10));
		                 }
		             }

		             Workbook resultsWorkbook = new XSSFWorkbook();
		             Sheet resultsSheet = resultsWorkbook.createSheet("Results");
		             createHeaderRow(resultsSheet);

		             int rowIndex = 1;
		             String currentPin = null;
		             int ProductCount= 1;

		             WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		             for (int i = 0; i < uRL.size(); i++) {
		                 String id = inputPid.get(i);
		                 String city = InputCity.get(i);
		                 String name = InputName.get(i);
		                 String size = InputSize.get(i);
		                 String productCode = NewProductCode.get(i);
		                 String url = uRL.get(i);
		                 String uom = UOM.get(i);
		                 String mulitiplier = Mulitiplier.get(i);
		                 String availability = Availability.get(i);
		                 String locationSet = Pincode.get(i);
		                 String namecheck = NameForCheck.get(i);

		                 try {
		                     // Skip navigation if the URL is empty or 'NA', and mark all values as 'NA'
		                     if (url.isEmpty() || url.equalsIgnoreCase("NA")) {
		                         populateRow(resultsSheet, rowIndex++, id, city , name, size, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", namecheck);
		                         System.out.println("URL is empty or NA for product ID " + id + ". Marking all values as 'NA' and skipping navigation.");
		                         continue; // Skip to the next product
		                     }

		                     // Valid URL, proceed with scraping
		                     driver.get(url);
		                     driver.manage().window().maximize();
		                     System.out.println("Navigating to URL: " + url);

		                     if (currentPin == null || !currentPin.equals(locationSet)) {
		                         selectLocation(driver, wait, locationSet);
		                         currentPin = locationSet;
		                         System.out.println("Location updated to: " + locationSet);
		                     }
		                     
		                     
//		                     try {
//		                     	  WebElement notFound = driver.findElement(By.xpath("//title[contains(text(), '404 Not Found')]"));
//		                           if (notFound.isDisplayed()) {
//		                             populateRow(resultsSheet, rowIndex++, id, city, name, size, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", namecheck);
//		                             System.out.println("Page not found for URL: " + url);
//		                             continue; // Skip further processing for this product
//		                         }
//		                     } catch (NoSuchElementException e) {
//		                         // If not found, proceed as usual
		                     
		                     String pageTitle = driver.getTitle();
		                     if (pageTitle.contains("404") || pageTitle.contains("Not Found")) {
		                         populateRow(resultsSheet, rowIndex++, id, city, name, size, "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", "NA", namecheck);
		                         System.out.println("404 Page detected for URL: " + url);
		                         continue;
		                     }
		                     else {
		                   

		                     String newName = getElementText(driver, By.xpath("//div[@class='grid__item medium-up--one-whole']//h1[@class='product-single__header h2']"), "NA");
		                     System.out.println("Product Name: " + newName);

		                     String spRaw = getElementText(driver, By.xpath(
		                     	    "//div[contains(@class, 'variant-input')]//input[@type='radio' and @checked='checked']/following-sibling::label//div[contains(@class,'variant-box-middle')]/h1"
		                     	), "NA");

		                     	String spValue = spRaw.equals("NA") ? "NA" : spRaw.replace("₹", "").replace(",", "");
		                     	System.out.println("SP Value: " + spValue);

		                     
		                     String mrpRaw = getElementText(driver, By.xpath(
		                     	    "//div[contains(@class, 'variant-input')]//input[@type='radio' and @checked='checked']/following-sibling::label//div[contains(@class,'variant-box-middle')]/p/span"
		                     	), "NA");

		                     	String mrpValue = mrpRaw.equals("NA") ? "NA" : mrpRaw.replace("₹", "").replace(",", "");
		                     	System.out.println("MRP Value: " + mrpValue);

		                     if (mrpValue.isEmpty()) {
		                         mrpValue = spValue;
		                     }

		                     int result = isAvailable(driver) ? 1 : 0;
		                     String NewAvailability1 = String.valueOf(result);
		                     System.out.println("Availability: " + (result == 1 ? "Available" : "Not Available"));

		                     String offerValue = (mrpValue.equals(spValue)) ? "NA" : getOffer(driver);
		                     System.out.println("Offer: " + offerValue);

		                     takeScreenshot(driver, id);

		                     // Populate row in the output Excel
		                     populateRow(resultsSheet, rowIndex++, id, city, name, size, productCode, url, newName, mrpValue, spValue, uom, mulitiplier, NewAvailability1, offerValue, namecheck);

		                     } 
		                     } catch (Exception e) {
		                     e.printStackTrace();
		                     // If there's an exception, populate with "NA"
		                     populateRow(resultsSheet, rowIndex++, id, city, name, size, productCode, url, "NA", "NA", "NA", uom, mulitiplier, "NA", "NA", namecheck);
		                     System.out.println("Failed to extract data for URL: " + url);
		                 }
		             }

		             String outputFilePath = ".\\Output\\SuperTail_OutputData_Delhi" + new SimpleDateFormat("dd-MM-yyyy_HH_mm_ss").format(new Date()) + ".xlsx";
		             FileOutputStream outFile = new FileOutputStream(outputFilePath);
		             resultsWorkbook.write(outFile);
		             outFile.close();
		             System.out.println("Output file saved: " + outputFilePath);

		         } catch (Exception e) {
		             e.printStackTrace();
		         } finally {
		             if (driver != null) {
		                 driver.quit();
		                 System.out.println("Scraping done.");
		             }
		         }
		     }

		     private static String getCellValue(Row row, int colIndex) {
		         Cell cell = row.getCell(colIndex);
		         return (cell != null && cell.getCellType() == CellType.STRING) ? cell.getStringCellValue() : "";
		     }

		     private static void createHeaderRow(Sheet resultsSheet) {
		         Row headerRow = resultsSheet.createRow(0);
		         String[] headers = {"InputPid", "InputCity", "InputName", "InputSize", "NewProductCode", "URL", "Name", "MRP", "SP", "UOM", "Multiplier", "Availability", "Offer", "Commands", "Remarks", "Correctness", "Percentage", "Name","Emp Id","Name Check"};
		         for (int i = 0; i < headers.length; i++) {
		             headerRow.createCell(i).setCellValue(headers[i]);
		         }
		     }

		     private static void populateRow(Sheet resultsSheet, int rowIndex, String id, String city, String name, String size, String productCode, String url, 
		                                     String newName, String mrpValue, String spValue, String uom, String mulitiplier, 
		                                     String availability, String offerValue, String namecheck) {
		         Row resultRow = resultsSheet.createRow(rowIndex);
		         resultRow.createCell(0).setCellValue(id);
		         resultRow.createCell(1).setCellValue(city);
		         resultRow.createCell(2).setCellValue(name);
		         resultRow.createCell(3).setCellValue(size);
		         resultRow.createCell(4).setCellValue(productCode);
		         resultRow.createCell(5).setCellValue(url);
		         resultRow.createCell(6).setCellValue(newName);
		         resultRow.createCell(7).setCellValue(mrpValue);
		         resultRow.createCell(8).setCellValue(spValue);
		         resultRow.createCell(9).setCellValue(uom);
		         resultRow.createCell(10).setCellValue(mulitiplier);
		         resultRow.createCell(11).setCellValue(availability); 
		         resultRow.createCell(12).setCellValue(offerValue);  
		         resultRow.createCell(13).setCellValue("");
		         resultRow.createCell(14).setCellValue("");
		         resultRow.createCell(15).setCellValue("");
		         resultRow.createCell(16).setCellValue("");
		         resultRow.createCell(17).setCellValue("");  
		     }

		     private static void selectLocation(WebDriver driver, WebDriverWait wait, String locationSet) {
		         try {
		             WebElement locationField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='header-icons_wrapper']//div[@id='pin-code']")));
		             locationField.click();
		             wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='pin-code-modal-popup']//input[@id='pincodeInput']")));
		             WebElement popupInputBoxClick = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='pin-code-modal-popup']//input[@id='pincodeInput']")));
		             popupInputBoxClick.click();
		             popupInputBoxClick.sendKeys(locationSet);
		             WebElement applyButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='pin-code-modal-popup']//button[@class='apply_btn']")));
		             applyButton.click();
		             Thread.sleep(2000);
		         } catch (Exception e) {
		             System.out.println("Error selecting location: " + e.getMessage());
		         }
		     }

		     private static boolean isAvailable(WebDriver driver) {
		         try {
		             WebElement availabilityElement = driver.findElement(By.xpath("//div[@class='atc_button_outer']//button[@id='AddToCart-template--16703736905966__main']"));
		             return availabilityElement.isEnabled();
		         } catch (Exception e) {
		             return false;
		         }
		     }

//		     private static String getElementText(WebDriver driver, By by, String defaultValue) {
//		         try {
//		             WebElement element = driver.findElement(by);
//		             return element.getText().trim();
//		         } catch (Exception e) {
//		             return defaultValue;
//		         }
//		     }

		     private static String getOffer(WebDriver driver) {
		         try {
		             WebElement offerElement = driver.findElement(By.xpath("//div[contains(@class, 'variant-input')]//input[@type='radio' and @checked='checked']/following-sibling::label//div[contains(@class,'variant-box-footer')]/p"));
		             String offerText = offerElement.getText().trim();
		             if (offerText.contains("SAVE")) {
		                 offerText = offerText.replace("SAVE", "").trim(); 
		             }
		             return offerText.endsWith("%") ? offerText + " Off" : offerText;
		         } catch (Exception e) {
		             return "NA";
		         }
		     }

		     private static void takeScreenshot(WebDriver driver, String id) {
		         try {
		             TakesScreenshot ts = (TakesScreenshot) driver;
		             File sourceFile = ts.getScreenshotAs(OutputType.FILE);
		             String timestamp = new SimpleDateFormat("dd-MM-yyyy_HH_mm_ss").format(new Date());
		             String screenshotPath = ".\\Screenshot\\" +"SuperTail_"+ id + "_" + timestamp + ".png";
		             File destinationFile = new File(screenshotPath);
		             FileUtils.copyFile(sourceFile, destinationFile);
		             
		             System.out.println("Screenshot saved for Product ID: " + id);
		             //System.out.println("ProductCount :"+ ProductCount++);
		         } catch (Exception e) {
		             System.out.println("Failed to capture screenshot: " + e.getMessage());
		         }
		     }
		     
		     public static String getElementText(WebDriver driver, By locator, String defaultValue) {
		         try {
		             WebElement element = driver.findElement(locator);

		             // New check: if element is hidden, return default value
		             if (!element.isDisplayed()) {
		                 return defaultValue;
		             }

		             String text = element.getText().trim();
		             return text.isEmpty() ? defaultValue : text;
		         } catch (NoSuchElementException e) {
		             return defaultValue;
		         }
		     }


			}