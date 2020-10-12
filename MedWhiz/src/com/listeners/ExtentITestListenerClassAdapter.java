package com.listeners;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.model.Log;
import com.java.ExtentReportUtility;
import com.java.Inc_Utility;
import com.java.MethodLibrary;

public class ExtentITestListenerClassAdapter implements ITestListener{

	 private static ExtentReports extent; 
	 private static ExtentTest logger;
	 static WebDriver driver;
	 private static Properties properties;
	 
	 	 public static ExtentTest getLogger(){
		 return logger;
	 }
	 
	 @Override
	public void onFinish(ITestContext arg0) {
		
		try {
			logger.info("***************Test execution Ended!!***************");
			System.out.println("!!**************Test Execution Ended*************!!");
			ExtentReportUtility.tearDown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onStart(ITestContext context) {
		
		System.out.println("!!********* Test Execution Started ***********!!");
		
		}	
	

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
			
	}

	@Override
	public void onTestFailure(ITestResult result) {

		driver = (WebDriver)result.getTestContext().getAttribute("webDriver");
				
				String exceptionMsg = result.getThrowable().getMessage();
				
				logger.fail("<details>" + "<summary>"+ "<b>"+ "<font color="+"red>" +"Exception Occured:Click to see"
							+"</font>"+"</b>"+"</summary>"+
							exceptionMsg.replaceFirst(",","<br>")
							+"</details>"+" \n"					
						);
				
				
				String temp = MethodLibrary.getScreenshot(driver, "");
				try {
					logger.fail("<b>"+"<font color="+"red>"+"Screenshot of failure"+"</font>"+"</b>", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Testcase Failed:"+result.getName());
				String failurelog = "TEST CASE FAILED";
				Markup m =MarkupHelper.createLabel(failurelog, ExtentColor.RED);
				logger.log(Status.FAIL, m);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		String testNamme  = result.getName();
		String logText = "<b>"+"TEST CASE:- "+testNamme.toUpperCase()+" Skipped."+"</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.AMBER);
		logger.skip(m);
	}

	@Override
	public void onTestStart(ITestResult result) {
		
		extent = (ExtentReports) result.getTestContext().getAttribute("extent");	
		logger = extent.createTest(result.getName());
				logger.info(result.getName()+" Test execution started..!!");
				System.out.println(result.getName()+" Execution started..!!");
				
	}

	@Override
	public void onTestSuccess(ITestResult result) {
				
		String testNamme  = result.getName();
		String logText = "<b>"+"TEST CASE:- "+testNamme.toUpperCase()+" PASSED."+"</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		logger.pass(m);
		
		System.out.println("Testcase Passed: "+result.getStatus());
		driver = (WebDriver)result.getTestContext().getAttribute("webDriver");	
		//MethodLibrary.logout(driver);
	}

}
