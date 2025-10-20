package code3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

//import CommonUtility.BlinkitId;

public class Zepto {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 //System.setProperty("webdriver.chrome.driver", "./Drivers//chromedriver.exe");
	        WebDriver driver = new ChromeDriver();

	        int count = 0;
	        // int finalSp;
	          String spValue = ""; 
	          String finalSp = "";
	          String offerValue = "NA";
	          String newName = null;
	          String mrpValue = null;
	          String originalMrp1 = " ";
	          String originalMrp2 = " ";
	          String originalMrp3 = " ";
	          String originalSp1 = " ";
	          String originalSp2 = " ";
	          String NewAvailability1 = " ";
	          
	        try {
	            // Read URLs from Excel file
	            String filePath = ".\\input-data\\Zepto.xlsx";
	            FileInputStream file = new FileInputStream(filePath);
	            Workbook urlsWorkbook = new XSSFWorkbook(file);
	            Sheet urlsSheet = urlsWorkbook.getSheet("Sheet2");
	            int rowCount = urlsSheet.getPhysicalNumberOfRows();

		            List<String> inputPid = new ArrayList<>(),InputCity = new ArrayList<>(),InputName = new ArrayList<>(),InputSize = new ArrayList<>(),
		            		uRL = new ArrayList<>(),UOM = new ArrayList<>(),Mulitiplier = new ArrayList<>(),Availability = new ArrayList<>(),Pincode = new ArrayList<>(),NameForCheck = new ArrayList<>();
		            
	            // Extract URLs from Excel
	            for (int i = 0; i < rowCount; i++) {
	                Row row = urlsSheet.getRow(i);
	                
	                
	                  if (i == 0) {
	                continue;
	            }    
	                
	                
	                Cell inputPidCell = row.getCell(0);
	                Cell inputCityCell = row.getCell(1);
	                Cell inputNameCell = row.getCell(2);
	                Cell inputSizeCell = row.getCell(3);
	                //Cell newProductCodeCell = row.getCell(4);
	                Cell urlCell = row.getCell(4);
	                Cell uomCell = row.getCell(5);
	                Cell multiplierCell = row.getCell(6);
	                Cell availabilityCell = row.getCell(7);
	                Cell pinCodeCell = row.getCell(8);        
	                Cell oldNameCell = row.getCell(9);    
	                
	            //    Cell urlCell = row.getCell(0);
	              //  Cell urlCell = row.getCell(0);
	               // Cell idCell = row.getCell(1);
	               // Cell offerCell = row.getCell(2);
	                
	                if (urlCell != null && urlCell.getCellType() == CellType.STRING) {
	                    String url = urlCell.getStringCellValue();
	                    String id = (inputPidCell != null && inputPidCell.getCellType() == CellType.STRING) ? inputPidCell.getStringCellValue() : "";
	                    String city = (inputCityCell != null && inputCityCell.getCellType() == CellType.STRING) ? inputCityCell.getStringCellValue() : "";
	                    String name = (inputNameCell != null && inputNameCell.getCellType() == CellType.STRING) ? inputNameCell.getStringCellValue() : "";
	                    String size = (inputSizeCell != null && inputSizeCell.getCellType() == CellType.STRING) ? inputSizeCell.getStringCellValue() : "";
	                    //String productCode = (newProductCodeCell != null && newProductCodeCell.getCellType() == CellType.STRING) ? newProductCodeCell.getStringCellValue() : "";
	                    String uom = (uomCell != null && uomCell.getCellType() == CellType.STRING) ? uomCell.getStringCellValue() : "";
	                    String mulitiplier = (multiplierCell != null && multiplierCell.getCellType() == CellType.STRING) ? multiplierCell.getStringCellValue() : "";
	                    String availability = (availabilityCell != null && availabilityCell.getCellType() == CellType.STRING) ? availabilityCell.getStringCellValue() : "";
	                    String locationSet = (pinCodeCell != null && pinCodeCell.getCellType() == CellType.STRING) ? pinCodeCell.getStringCellValue() : "";
	                    String namecheck = (oldNameCell != null && oldNameCell.getCellType() == CellType.STRING) ? oldNameCell.getStringCellValue() : "";
	                    
	                    inputPid.add(id);
	                    InputCity.add(city);
	                    InputName.add(name);
	                    InputSize.add(size);
	                    //NewProductCode.add(productCode);
	                    uRL.add(url);
	                    UOM.add(uom);
	                    Mulitiplier.add(mulitiplier);
	                    Availability.add(availability);
	                    Pincode.add(locationSet);
	                    NameForCheck.add(namecheck);
	                    
						/*
						 * uRL.add(url); ids.add(id); offers.add(offer);
						 */
	                    
	                }
	            }
	            // Create Excel workbook for storing results
	            Workbook resultsWorkbook = new XSSFWorkbook();
	            Sheet resultsSheet = resultsWorkbook.createSheet("Results");

	            Row headerRow = resultsSheet.createRow(0);
	            
	            
	            headerRow.createCell(0).setCellValue("InputPid");
	            headerRow.createCell(1).setCellValue("InputCity");
	            headerRow.createCell(2).setCellValue("InputName");
	            headerRow.createCell(3).setCellValue("InputSize");
	            headerRow.createCell(4).setCellValue("NewProductCode");
	            headerRow.createCell(5).setCellValue("URL");
	            headerRow.createCell(6).setCellValue("Name");
	            headerRow.createCell(7).setCellValue("MRP");
	            headerRow.createCell(8).setCellValue("SP");
	            headerRow.createCell(9).setCellValue("UOM");
	            headerRow.createCell(10).setCellValue("Multiplier");
	            headerRow.createCell(11).setCellValue("Availability");
	            headerRow.createCell(12).setCellValue("Commands");
	            headerRow.createCell(13).setCellValue("Remarks");
	            headerRow.createCell(14).setCellValue("Correctness");
	            headerRow.createCell(15).setCellValue("Percentage");
	            headerRow.createCell(16).setCellValue("Name");
	            headerRow.createCell(17).setCellValue("Offer");
	            headerRow.createCell(18).setCellValue("NameForCheck");
	            
	            int rowIndex = 1;
	            int headercount = 0;
	            String currentPin = null;
	            
	            for (int i = 0; i < uRL.size(); i++) {
	                String id = inputPid.get(i);
	                String city = InputCity.get(i);
	                String name = InputName.get(i);
	                String size = InputSize.get(i);
	               // String productCode = NewProductCode.get(i);
	                String url = uRL.get(i);
	                String uom = UOM.get(i);
	                String mulitiplier = Mulitiplier.get(i);
	                String availability = Availability.get(i);
	                String locationSet = Pincode.get(i);
	                String namecheck = NameForCheck.get(i);
	                
	                try {
	                	
	                	  if (url.isEmpty() || url.equalsIgnoreCase("NA")) {
	                          // Set "NA" values in all three columns
	                          Row resultRow = resultsSheet.createRow(rowIndex++);
	                          resultRow.createCell(0).setCellValue(id);
	                          resultRow.createCell(1).setCellValue(city);
	                          resultRow.createCell(2).setCellValue(name);
	                          resultRow.createCell(3).setCellValue(size);
	                          resultRow.createCell(4).setCellValue("NA");
	                          resultRow.createCell(5).setCellValue(url);
	                          resultRow.createCell(6).setCellValue("NA");
	                          resultRow.createCell(7).setCellValue("NA");
	                          resultRow.createCell(8).setCellValue("NA");
	                          resultRow.createCell(9).setCellValue("NA");
	                          resultRow.createCell(10).setCellValue("NA");
	                          resultRow.createCell(11).setCellValue("NA");
	                          resultRow.createCell(12).setCellValue("NA");
	                          
	                          System.out.println("Skipped processing for URL: " + url);
	                          continue; // Skip to the next iteration
	                      }
	                	  
	                    driver.get(url);
	                    Thread.sleep(5000);
	                    driver.manage().window().maximize();
	                   
	        
	                    //location sets 
	                /*    if (currentPin == null || !currentPin.equals(locationSet)) {
	                        
	                        Thread.sleep(9000); 	
	                          	
	                          	
	                         for(int k=0;k < 100;k++) {
	                          	try {
	                          	Thread.sleep(3000);
	                          	//WebElement location = driver.findElement(By.xpath("/html/body/div[1]/div/header/div/div[1]/button"));
	                          	 WebElement location = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[1]/header/div[1]/div[2]/button/span"));
	           					location.click();
	           					break;
	                          }
	                          	catch(Exception r) {
	                          			try {
	                          		Thread.sleep(4000);
	                             	 WebElement location = driver.findElement(By.className("/html/body/div/div/div/header/div/div[1]/button"));          			 
	              					location.click();
	              					break;
	              					
	                          			}
	                          			catch(NoSuchElementException u) {
	                          				try {
	                          				Thread.sleep(4000);
	                                        	 WebElement location = driver.findElement(By.className("/html/body/div[2]/div/div/div/div[2]/div/button[1]/div/p"));          			 
	                         					location.click();
	                         					Thread.sleep(3000);
	                         					break;
	                          			}
	                          				catch(Exception tr) {
	                          					Thread.sleep(4000);
	                                           	 WebElement location = driver.findElement(By.xpath("/html/body/div[1]/div/header/div/div[1]/button"));          			 
	                            					location.click();
	                            					Thread.sleep(3000);
	                            					break;
	                          				}
	                          				//div[@class = 'flex flex-col justify-center relative lg:flex-initial flex-1 h-[2.75rem]']//following::span[2]
	                          			}
	                          	}
	                         }
	                          	
	           				//	Thread.sleep(1000);909
	                          	
	           					String tempPinNumber = "";
	           					for (int j = 0; j < 200; j++) {
	           						try {         
	           							
	           							
	           							
	           							Thread.sleep(3000);
	           							
	           							driver.findElement(
	           									By.xpath("/html/body/div[3]/div/div/div/div/div/div/div[2]/div/div[2]/div[1]/div/input"))
	           									.sendKeys(Keys.ENTER);
	           							
	           							Thread.sleep(3000);
	           							
	           							driver.findElement(
	           									By.xpath("/html/body/div[3]/div/div/div/div/div/div/div[2]/div/div[2]/div[1]/div/input")).clear();
	           							
	           							Thread.sleep(3000);
	           							
	           							System.out.println("print the crt pin number" + locationSet);
	           							
	           							String crtPin = locationSet;
	           							driver.findElement(
	           									By.xpath("/html/body/div[3]/div/div/div/div/div/div/div[2]/div/div[2]/div[1]/div/input"))
	           									.sendKeys(crtPin);
	           							
	           							Thread.sleep(3000);
	           							
	           							driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div/div/div/div[2]/div/div[2]/div[2]/div[1]/div/div/h4")).click();
	           							
	           							Thread.sleep(2000);
	           							
	           							currentPin = locationSet;
	           							
	           							
	           							System.out.println("============="  + currentPin+ "======================");
	           							
	           							driver.findElement(
	           									By.xpath("//div[@id='GLUXZipInputSection']//input[@id='GLUXZipUpdateInput']"))
	           									.sendKeys(Keys.ENTER);   
	           							
	           							Thread.sleep(3000);
	           							
	           							driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div/div/div/div[2]/div/div[2]/div/div[2]/div/button[2]/div")).click();
	           							
	           							Thread.sleep(2000);
	           							
	           							currentPin = locationSet;
	           							
	           							
	           							
	           							break;
	           						} 
	           						
	           						catch (Exception e) {
	           				
	           						}
	           						}
	           				
	           											}  */
	                    
	                    Thread.sleep(2000);
	                       
	                    try {
	                    	
	                    WebElement nameElement = driver.findElement(By.xpath("//div[@class='mt-2 flex items-center justify-between gap-6']//h1"));
	                    newName = nameElement.getText();
	                    System.out.println(newName);
	                    }
	                    
	                    catch(NoSuchElementException e) {
	                    	
	                    	WebElement nameElement = driver.findElement(By.xpath("/html/body/div/main/div[2]/div/div[2]/div[2]/div/h1"));
	                    	newName = nameElement.getText();
	                        System.out.println(newName);
	                    	
	                    }
	                    System.out.println("headercount = " + headercount);
	                    
	                    headercount++;

	           //sp
	                    Thread.sleep(2000);
	                   try {
	                    WebElement sp = driver.findElement(By.xpath("//span[@class='text-[32px] font-medium leading-[30px] text-[#262A33]']"));
	                    originalSp1 = sp.getText();
	                    spValue =  originalSp1.replace("₹", "");
	                    System.out.println(spValue);
	                   }
	                   catch(Exception e) {
	                	   
	                	   try {
	                	   WebElement sp = driver.findElement(By.xpath("//span[@class='text-[32px] font-medium leading-[30px] text-[#262A33]']"));
	                       originalSp2 = sp.getText();
	                       spValue =  originalSp2.replace("₹", "");
	                       System.out.println(spValue);
	                	   }
	                       catch(Exception exx) {
	                    	   spValue = "NA";
	                       }
	                   }
	                   
	                   //mrp
	                   Thread.sleep(2000);
	                    try {
	                    WebElement mrp = driver.findElement(By.xpath("//p[@class='text-[14px] font-[450] leading-[21.6px] tracking-[-0.24px] text-[#757C8D]']//span[2]"));
	                    originalMrp1 = mrp.getText();
	                    mrpValue = originalMrp1.replace("₹", "");
	                    System.out.println(mrpValue);
	                    
	                    }
	                    
	                    catch(NoSuchElementException e){ 
	                    	try {
	                    		
	                    		WebElement mrp = driver.findElement(By.xpath("//div[@class='mt-5']//div[@class='flex items-center gap-2']//span[2]"));
	                            originalMrp2 = mrp.getText();
	                            mrpValue = originalMrp2.replace("₹", "");
	                           // mrpValue = originalMrp2;                      
	                            System.out.println(mrpValue);
	                        
	                    }
	                    	catch(Exception ex) {
	                    		try {
	                    		WebElement mrp = driver.findElement(By.xpath("//*[@id=\"corePriceDisplay_desktop_feature_div\"]/div[1]/span[2]/span[2]/span[2]"));
	                           // WebElement mrp = driver.findElement(By.xpath("/html/body/div[2]/div/div[7]/div[3]/div[4]/div[12]/div/div/div[4]/div[2]/span/span[1]/span[2]/span/span[2]"));
	                            originalMrp3 = mrp.getText();
	                            mrpValue = originalMrp3.replace("₹", "");                      
	                            System.out.println(mrpValue);
	                    		}
	                    		catch(Exception exx) {
	                    			mrpValue = spValue;
	                    			System.out.println(mrpValue);
	                    		}
	                    		}
	                    	}
	                    
	                   Thread.sleep(2000);
//	                   // offer
//	                   try {
//	                       WebElement offer = driver.findElement(By.xpath(".//div[@class='flex items-center']//div"));
//	                       String NewOffer = offer.getText();
//	                       offerValue = NewOffer;
//	                      // offerValue = NewOffer.replace("-","").replace("%","% Off");
//	                      /* Pattern pattern = Pattern.compile("\\((.*?)\\)");
//	                       Matcher matcher = pattern.matcher(offer1);
//	                       
//							if(matcher.find()) { 
//								  String offer2 = matcher.group(1);
//								  offerValue = offer2.replace("%","% Off");
//							  }
//	                       //offerValue = offer.getText();  */
//	                       System.out.println(offerValue);
//	                      }
//	                      catch(Exception e) {
//	                    	  try {
//	                   	   WebElement offer = driver.findElement(By.xpath("//*[@id=\"corePriceDisplay_desktop_feature_div\"]/div[1]/span[2]"));
//	                   	   String NewOffer = offer.getText();
//	                      // offerValue = NewOffer.replace("-","").replace("%","% Off");
//	                   	   offerValue = NewOffer;
//	                          System.out.println(offerValue);
//	                    	  }
//	                    	  catch(Exception ex){
//	                    		  offerValue = "NA";
//	                    	  }
//	                      }    
						
						
						if(url.contains("NA")){
							String result = "NA";
						}	
						
						int result = 1;

						try {
						    // Get the page source to check for the presence of "notify me"
						    String pageSource = driver.getPageSource();

						    // If the page contains the text "notify me", set result to 0
						    if (pageSource.toLowerCase().contains("notify me")) {
						        result = 0;  // Out of stock or "notify me" condition
						    } else {
						        // Otherwise, set result based on element presence
						        result =  1;
						    }

						    // Print the result for debugging
						    System.out.println(result);

						} catch (Exception e) {
						    // Handle exceptions, print the error message
						    System.out.println(e.getMessage());
						}

						// Convert result to string and assign to NewAvailability1
						NewAvailability1 = String.valueOf(result);

//                         
//	                   
//	                   		//Screenshots 
//	                      BlinkitId screenshot = new BlinkitId();
//		                   try {
//		       				screenshot.screenshot(driver, "Zepto", id);
//		       			} catch (Exception e) {
//		       				e.fillInStackTrace();
//		       			
//		       			}
	                   
	                    Row resultRow = resultsSheet.createRow(rowIndex++);
	                    
	                    resultRow.createCell(0).setCellValue(id);
	                    resultRow.createCell(1).setCellValue(city);
	                    resultRow.createCell(2).setCellValue(name); 
	                    resultRow.createCell(3).setCellValue(size);
	                    resultRow.createCell(4).setCellValue("NA");
	                    resultRow.createCell(5).setCellValue(url);
	                    resultRow.createCell(6).setCellValue(newName);
	                    resultRow.createCell(7).setCellValue(mrpValue);
	                    resultRow.createCell(8).setCellValue(spValue);
	                    resultRow.createCell(9).setCellValue(uom);
	                    resultRow.createCell(10).setCellValue(mulitiplier);
	                    resultRow.createCell(11).setCellValue(NewAvailability1);
	                    resultRow.createCell(12).setCellValue(" ");
	                    resultRow.createCell(13).setCellValue(" ");
	                    resultRow.createCell(14).setCellValue(" ");
	                    resultRow.createCell(15).setCellValue(" ");
	                    resultRow.createCell(16).setCellValue(" ");
	                    resultRow.createCell(17).setCellValue(offerValue);
	                    resultRow.createCell(18).setCellValue(namecheck);
	                    
	                    System.out.println("Data extracted for URL: " + url);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    
	                    Row resultRow = resultsSheet.createRow(rowIndex++);
	                    resultRow.createCell(0).setCellValue(id);
	                    resultRow.createCell(1).setCellValue(city);
	                    resultRow.createCell(2).setCellValue(name);
	                    resultRow.createCell(3).setCellValue(size);
	                    resultRow.createCell(4).setCellValue("NA");
	                    resultRow.createCell(5).setCellValue(url);
	                    resultRow.createCell(6).setCellValue("NA");
	                    resultRow.createCell(7).setCellValue("NA");
	                    resultRow.createCell(8).setCellValue("NA");
	                    resultRow.createCell(9).setCellValue(uom);
	                    resultRow.createCell(10).setCellValue(mulitiplier);
	                    resultRow.createCell(11).setCellValue(NewAvailability1);
	                    resultRow.createCell(12).setCellValue(" ");
	                    resultRow.createCell(13).setCellValue(" ");
	                    resultRow.createCell(14).setCellValue(" ");
	                    resultRow.createCell(15).setCellValue(" ");
	                    resultRow.createCell(16).setCellValue(" ");
	                    resultRow.createCell(17).setCellValue(offerValue);
	                    resultRow.createCell(18).setCellValue(namecheck);
	                    

	                    System.out.println("Failed to extract data for URL: " + url);
	                    
	                }
	            }
	            
	            // Write results to Excel file
	            FileOutputStream outFile = new FileOutputStream(".\\Output\\Zepto_citywise_Output.xlsx");
	            resultsWorkbook.write(outFile);
	            outFile.close();

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (driver != null) {
	            	System.out.println("DoNe DoNe Scraping DoNe");
	                driver.quit();
	            }
	        }
	        
	        

	}

}
