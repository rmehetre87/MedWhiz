package com.test;

import static org.testng.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.java.ExtentReportUtility;
import com.java.MethodLibrary;
import com.listeners.ExtentITestListenerClassAdapter;



//@Listeners(ExtentITestListenerClassAdapter.class)
public class TC_login  extends MethodLibrary {
	 
		static WebDriver driver;
	    private static Properties properties;
	    static ExtentTest logger;
	    static ExtentReports extent;
	    
	    	    
	    @BeforeTest
		public <ITestContest> void beforeTest(ITestContext context) {
						properties = new Properties();
							try {
																						
									properties.load(new FileReader(".//Data//ObjectRepository2.properties"));
									driver = openBrowser("Chrome");
									context.setAttribute("webDriver", driver);
									extent = ExtentReportUtility.reportSetup();
									context.setAttribute("extent", extent);
																		
								}
								catch(Exception e) {

										e.printStackTrace();
									}
			  }
		
		@Test(priority = 1, enabled=true)
		public void LoginMedWhiz() throws FileNotFoundException, IOException, InterruptedException{
			logger = ExtentITestListenerClassAdapter.getLogger();
			
			OpenMedWhiz(driver,"username","password");
			Thread.sleep(9000);
			
			String temp = MethodLibrary.getScreenshot(driver, "login succesful");
			logger.info("<font color="+"Black>"+"Screenshot: "+"</font>", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
			
		}

							
		
		
		@Test(priority = 2, enabled=true)
		public void VerifyHomepage() throws FileNotFoundException, IOException, InterruptedException{
			
			logger = ExtentITestListenerClassAdapter.getLogger();
					
			try {
				assertTrue(validateByText("Students", "//*[text() = 'Students']"));
				String temp = MethodLibrary.getScreenshot(driver, "text Validated");
				logger.info("<font color="+"Black>"+"Screenshot:Students text present "+"</font>", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
				
				Thread.sleep(200);
				
				/*assertTrue(validateByText("Students", "//*[text() = ' Faculty ']"));
				String temp1 = MethodLibrary.getScreenshot(driver, "text Validated");
				logger.info("<font color="+"Black>"+"Screenshot: Faculty text present "+"</font>", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
			*/
				driver.findElement(By.xpath(properties.getProperty("Videos_tab"))).click();
				Thread.sleep(9000);
				String temp2 = MethodLibrary.getScreenshot(driver, "Video tab");
				logger.info("<font color="+"Black>"+"Screenshot: Video tab "+"</font>", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
			
				
				driver.findElement(By.xpath(properties.getProperty("Models_tab"))).click();
				Thread.sleep(9000);
				String temp3 = MethodLibrary.getScreenshot(driver, "Models_tab");
				logger.info("<font color="+"Black>"+"Screenshot: Models_tab "+"</font>", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
			
				driver.findElement(By.xpath(properties.getProperty("Faculty_tab"))).click();
				Thread.sleep(9000);
				String temp4 = MethodLibrary.getScreenshot(driver, "Faculty_tab");
				logger.info("<font color="+"Black>"+"Screenshot: Faculty_tab "+"</font>", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
			
				driver.findElement(By.xpath(properties.getProperty("Students_tab"))).click();
				Thread.sleep(9000);
				String temp5 = MethodLibrary.getScreenshot(driver, "Students_tab");
				logger.info("<font color="+"Black>"+"Screenshot: Students_tab "+"</font>", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
			
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		  @AfterTest(enabled=true)
		   public void teardown() {
		   	try {
					Thread.sleep(5000);
					MethodLibrary.MedWhizlogout(driver);
					driver.quit();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
		   	
		   }
	

}
