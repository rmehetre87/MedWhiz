package com.java;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.Assertion;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.model.Log;

import freemarker.log.Logger;
import sun.util.logging.resources.logging;

	public class MethodLibrary  {

	static WebDriver driver;
	private static Properties properties =  new Properties();
	static Actions action;
	static ExtentTest logger;
	public static JavascriptExecutor js = (JavascriptExecutor) driver; 


	/* Start methods  */	

	public static WebDriver openBrowser(String browsername) {
		switch (browsername) {
		case "Chrome":
			System.setProperty("webdriver.chrome.silentOutput", "true");
			
			DesiredCapabilities cap=DesiredCapabilities.chrome();
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			System.setProperty("webdriver.chrome.driver","D:\\ProjectWork\\Drivers\\chromedriver\\chromedriver.exe");
			driver = new ChromeDriver(cap);
			break;

		case "IE":
			System.setProperty("webdriver.ie.driver","C:\\Users\\home\\Desktop\\work\\software\\selenium_jars\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();			
			break;

		default:
			System.setProperty("webdriver.chrome.driver","D:\\ProjectWork\\Drivers\\chromedriver\\chromedriver.exe");
			driver = new ChromeDriver();
			break;
		}

		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		return driver;


	}
     public static String verifyValue(WebDriver driver, SoftAssert softassert, String identifier, String attribute, String expectedValue) throws FileNotFoundException, IOException{
		
		String actualValue = getElementvalue(driver, identifier, attribute);
		expectedValue = expectedValue.trim();
		actualValue = actualValue.trim();
		
		softassert.assertEquals(actualValue, expectedValue);
		
		if(actualValue.equals(expectedValue))
		{
			System.out.println("test expected==="+actualValue);
		}
		else
		{
			
			logger.fail("Field Actual Value: "+actualValue +"is not Mached with Expected Value: "+expectedValue);
			String temp = MethodLibrary.getScreenshot(driver, "failed value");
			logger.fail("<b>"+"<font color="+"red>"+"Screenshot of failure"+"</font>"+"</b>", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
		
		}
		
		return actualValue;
	}
	
	public static String verifyFieldValue(ExtentTest logger, WebDriver driver, SoftAssert softassert, String identifier, String attribute, String expectedValue) throws FileNotFoundException, IOException{
		
		String actualValue = getElementvalue(driver, identifier, attribute);
		expectedValue = expectedValue.trim();
		actualValue = actualValue.trim();
		//int fail = 0;
		
		softassert.assertEquals(actualValue, expectedValue);
		
		if(actualValue.equals(expectedValue))
		{			
			logger.info("Validatation point : "+identifier+ " actual value: '"+actualValue+"' matches with expected value: "+expectedValue);
		}
		else
		{
			logger.fail(" Actual Value: "+actualValue +", is not Mached with Expected Value: "+expectedValue);
			String temp = MethodLibrary.getScreenshot(driver, "failedvalue");
			logger.fail("<font color="+"red>"+"Screenshot: "+"</font>", MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
					
		}
		
		return actualValue;
	}
	
	public static void OpenMedWhiz(WebDriver driver,String username, String password) throws FileNotFoundException, IOException {
		
		properties.load(new FileReader(".//Data//ObjectRepository2.properties"));
		driver.get(properties.getProperty("test_URL"));
		driver.findElement(By.id("user_name")).sendKeys(properties.getProperty(username)); //MED-ADM1
		driver.findElement(By.id("password")).sendKeys("test@123"); //test@123
		driver.findElement(By.id("login_btn")).click();
		
	}

	public void handleAlertPresent(WebDriver driver) {
		/*WebDriverWait wait = new WebDriverWait(driver, 10);
		if(wait.until(ExpectedConditions.alertIsPresent())==null) {
			System.out.println("Alert not present");
			return false;
		}
		else {
			System.out.println("Alert Present");
			return true;

		}*/
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();

		}
		catch(NoAlertPresentException e) {
			System.out.println("No alert present!");

		}


	}


	public static void MedWhizlogout(WebDriver driver) {
		properties = new Properties();	
		try {
			properties.load(new FileReader(".//Data//ObjectRepository2.properties"));
			
			driver.findElement(By.xpath(properties.getProperty("link_logout"))).click();
			driver.findElement(By.xpath(properties.getProperty("btn_signout"))).click();
			driver.quit();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	//************************* Screen shot code **********************************

	public static String getScreenshot(WebDriver driver, String screenshotname)
	{
		TakesScreenshot ts= (TakesScreenshot) driver;

			File src = ts.getScreenshotAs(OutputType.FILE);

		String path = System.getProperty("user.dir")+"/Screenshots/"+screenshotname+".png";
		
		System.out.println("Screenshot taken");

		File destination=new File(path);

		try 
		{
			FileUtils.copyFile(src, destination);
		} catch (IOException e) 
		{
			System.out.println("Capture Failed "+e.getMessage());
		}

		return path;
	}

	/////////****************/////////////////

	public static String getElementvalue(WebDriver driver,String identifier, String attribute) throws FileNotFoundException, IOException {
		properties.load(new FileReader(".//Data//ObjectRepository2.properties"));
		action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath(properties.getProperty(identifier)));
		action.moveToElement(element).perform();
		String value = element.getAttribute(attribute);
		return value;
	}
	
	public static boolean verifyFieldValue(WebDriver driver,String identifier, String attribute, String expectedValue) throws FileNotFoundException, IOException{
		      String actualValue = getElementvalue(driver, identifier, attribute);
		      if(actualValue.equalsIgnoreCase(expectedValue))
		      {
		    	  return true;
		      }
		      return false;
	}
	
	
	public static boolean verifyText(WebDriver driver,String identifier, String expectedValue) throws FileNotFoundException, IOException{
	      String actualValue = getElement(driver,identifier).getText();
	      expectedValue = expectedValue.trim();
	      
	      if(actualValue.equalsIgnoreCase(expectedValue))
	      {
	    	  return true;
	      }
	      return false;
}

	public static WebElement getElement(WebDriver driver,String identifier) throws FileNotFoundException, IOException {
		properties.load(new FileReader(".//Data//ObjectRepository2.properties"));
		action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath(properties.getProperty(identifier)));
		action.moveToElement(element).perform();
		return element;
	}

	/* sendKeys method  */
	public static void sendKeys(WebDriver driver,String identifier, String Value) throws FileNotFoundException, IOException{
		if(Value!=null)
		{
			properties.load(new FileReader(".//Data//ObjectRepository2.properties"));

			driver.findElement(By.xpath(properties.getProperty(identifier))).clear();
			driver.findElement(By.xpath(properties.getProperty(identifier))).sendKeys(Value);
		}
	}


	public static void checkEleclickable(WebDriver driver,String xpathExpression) throws FileNotFoundException, IOException{
		properties.load(new FileReader(".//Data//ObjectRepository2.properties"));
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(properties.getProperty(xpathExpression))));
	}

	public static void checkEleVisibility(WebDriver driver,String xpathExpression) throws FileNotFoundException, IOException{
		properties.load(new FileReader(".//Data//ObjectRepository2.properties"));
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(properties.getProperty(xpathExpression))));
	}
	
	public static boolean checkElementPresent(WebDriver driver,String xpathExpression) throws FileNotFoundException, IOException{
		
		properties.load(new FileReader(".//Data//ObjectRepository2.properties"));
		WebDriverWait wait = new WebDriverWait(driver, 5);
		WebElement ele =  wait.until(ExpectedConditions.visibilityOf(MethodLibrary.getElement(driver,xpathExpression)));
		if(ele.isEnabled()==true && ele.isDisplayed()==true){
			return true;
		}
		return false;
	}
	
	public static boolean isElementPresent(WebDriver driver,String xpathExpression){
				
		List<WebElement> elements = driver.findElements(By.xpath(xpathExpression));
		if(elements.size()>0){
			return true;
		}
		return false;
	}

	/* alertAccept method  */

	public static void alertAccept(){
		Alert alert = driver.switchTo().alert();
		System.out.println(alert.getText());
		alert.accept();
	}

	/* alertDismiss method  */

	public static void alertDismiss(){
		Alert alert = driver.switchTo().alert();
		System.out.println(alert.getText());
		alert.dismiss();
	}

	/*    maxwindow    */
	public static void maxwindow()
	{
		driver.manage().window().maximize();
	}

	/* swtich to child window and back to parent window */
	// Store the current window handle

	public static void switchChildwindow()
	{


		for(String winHandle : driver.getWindowHandles()){
			driver.switchTo().window(winHandle);
		}
		driver.manage().window().maximize();
	}


	/*   actionContolplusrightarrow       */


	public static void actionContolplusrightarrow()
	{
		Actions action = new Actions(driver);
		action.keyDown(Keys.CONTROL).sendKeys(Keys.RIGHT).sendKeys(Keys.ENTER).perform();
	}

	/* backspace method  */

	public static void backspace(String xpathExpression) throws Exception{
		if(xpathExpression!=null)
		{
			driver.findElement(By.xpath(xpathExpression)).click();
		}
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_BACK_SPACE);
		r.keyRelease(KeyEvent.VK_BACK_SPACE);
	}

	/* checkEleVisibility method  */

	/* clear method  */

	public static void clear(String xpathExpression){
		driver.findElement(By.xpath(xpathExpression)).clear();
	}

	/* click method  */

	public static void clicknHold(WebDriver driver,String identifier) throws FileNotFoundException, IOException
	{	properties.load(new FileReader(".//Data//ObjectRepository2.properties"));
		//driver.findElement(By.xpath(properties.getProperty(identifier))).click();
		action = new Actions(driver);
		WebElement element = driver.findElement(By.xpath(properties.getProperty(identifier)));
		action.moveToElement(element).clickAndHold(element).build().perform();
	}

	public static void onclick(WebDriver driver,String identifier) throws FileNotFoundException, IOException
	{
		//driver.findElement(By.xpath(properties.getProperty(identifier))).click();

		properties.load(new FileReader(".//Data//ObjectRepository2.properties"));
		WebElement element = driver.findElement(By.xpath(properties.getProperty(identifier)));
		element.click();
	}

	public static void click(WebDriver driver,String identifier) throws FileNotFoundException, IOException
	{
		//driver.findElement(By.xpath(properties.getProperty(identifier))).click();
		action = new Actions(driver);
		properties.load(new FileReader(".//Data//ObjectRepository2.properties"));
		WebElement element = driver.findElement(By.xpath(properties.getProperty(identifier)));
		action.moveToElement(element).click().build().perform();

	}
	
	public static WebElement getElementbyvlaue(WebDriver driver,String value){

		if(value.length()>1){
			String xpath = "//td[text()='"+value+"']"; 
			//System.out.println(xpath);
			try{
				WebElement element = driver.findElement(By.xpath(xpath));
				return element;
			}
			catch(NoSuchElementException e){
				System.out.println("Element with value "+value+" Not found. \n"+e);
				return null;
			}
		}
		else{
			System.out.println("No value provided!!");
			return null;
		}

	}
		
	public static void clickByValue(WebDriver driver,String value){
		
		action = new Actions(driver);
		WebElement ele = getElementbyvlaue(driver,value);
		if(ele!= null){
		action.moveToElement(ele).click().build().perform();
		}
		
	}
	
	public static WebElement getCIbyValaue(WebDriver driver,String value){
	
		if(value.length()>1){
			String xpath = "//*[@id='T301389439']/tbody/tr/td/nobr/span[text()='"+value+"']"; 
			//System.out.println(xpath);
			try{
				WebElement element = driver.findElement(By.xpath(xpath));
				return element;
			}
			catch(NoSuchElementException e){
				System.out.println("Element with value "+value+" Not found. \n"+e);
				return null;
			}
		}
		else{
			System.out.println("No value provided!!");
			return null;
		}
		
	}
	
	public static void clicknHoldbyValue(WebDriver driver,String value)
	{
		//driver.findElement(By.xpath(properties.getProperty(identifier))).click();
		action = new Actions(driver);
		WebElement element = getElementbyvlaue(driver,value);
		action.moveToElement(element).clickAndHold(element).build().perform();
	}
	
/* clickElementByText method  */	

	public static void clickElementByText(WebDriver driver,String linkText)
	{
		driver.findElement(By.linkText(linkText)).click();;
	}

	public static WebElement fluentWait(final String identifier) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);

		WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.xpath(properties.getProperty(identifier)));
			}
		});

		return  foo;
	};
	/* clickOk method  */

	public static void clickOk()
	{

		driver.findElement(By.linkText("Ok")).click();
		/*int ok_size=driver.findElements(By.xpath("//button[text()='OK']")).size();
    	driver.findElements(By.xpath("//button[text()='OK']")).get(ok_size).click();*/

		//driver.findElement(By.xpath("//button[text()='OK']")).click();
	}

	/* fetchTableData method  */	

	public static List<WebElement> fetchTableData(String xpathExpression ) throws Exception{

		List<WebElement> rows = driver.findElements(By.xpath(xpathExpression));

		return rows;	

	}

	/* findElement method  */	

	public static WebElement findElement(String xpathExpression)
	{
		return driver.findElement(By.xpath(xpathExpression));
	}


	/* findElementByCSS method  */	

	public static WebElement findElementByCSS(String selectorExpression)
	{
		return driver.findElement(By.cssSelector(selectorExpression));
	}

	/* getCSSValue method  */	

	public static String getCSSValue(String selectorExpression , String Attribute){

		return driver.findElement(By.cssSelector(selectorExpression)).getCssValue(Attribute);
	}

	/* robotDropDown method  */	

	public static void robotDropDown(String clickXpath ,String searchXpath , String Value, long sleep) throws Exception{
		driver.findElement(By.xpath(clickXpath)).click();
		if(Value!=null)
		{
			driver.findElement(By.xpath(searchXpath)).sendKeys(Value);
		}
		Thread.sleep(sleep);
		Thread.sleep(4000);
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_ENTER);
		r.keyRelease(KeyEvent.VK_ENTER);		
	}

	/* EnterKeys method  */
	public static void EnterKeys(String xpathExpression){


		driver.findElement(By.xpath(xpathExpression)).sendKeys(Keys.ENTER);

	}

	/* DownKeys method  */
	public static void DownKeys(String xpathExpression){


		driver.findElement(By.xpath(xpathExpression)).sendKeys(Keys.DOWN);

	}

	/* DownKeys method  */
	public static void RightKeys(String xpathExpression){

		//Actions action=new Actions(driver);
		driver.findElement(By.xpath(xpathExpression)).sendKeys(Keys.ARROW_RIGHT);
		//action.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_RIGHT);
	}

	/* sendKeysJS method  */

	public static void sendKeysJS (String elementId , CharSequence Value){

		if(Value!=null){

			js.executeScript("document.getElementById("+elementId+").value="+Value+";");
		}
	}

	/* selectDropDown method  */	

	public static void selectDropDown(String xpathExpression, String Value){
		if(Value!=null){
			Select s1= new Select(driver.findElement(By.xpath(xpathExpression)));
			s1.selectByValue(Value);
		}
	}


	/* selectJS method  */	

	public static void selectJS(String xpathExpression, String jsScript, String Value){
		if(Value!=null){
			Select s1= new Select(driver.findElement(By.xpath(xpathExpression)));
			js.executeScript(jsScript);
			s1.selectByVisibleText(Value);
		}
	}

	/* selectRadiobutton method  */


	public static void selectRadiobutton(String xpathExpression){
		driver.findElement(By.xpath(xpathExpression)).click();
	}


	/* selectCheckbox method  */

	public static void selectCheckbox(String xpathExpression)
	{
		WebElement s2=driver.findElement(By.xpath(xpathExpression));
		s2.click();
	}

	/* ClickTab method  */

	public static void ClickTab(String xpathExpression)
	{
		WebElement s3=driver.findElement(By.xpath(xpathExpression));
		s3.sendKeys(Keys.TAB);
	}

	/* sendKeysActions method  */

	public static void sendKeysActions(String xpathExpression, String Value) {
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(By.xpath(xpathExpression)));
		//actions.click();
		actions.sendKeys(Value).build().perform();
		//actions.build().perform();

		//Action action = new Action(driver);

	}

	/* tableData method  */

	public static WebElement tableData(String xpathRow){

		/*List<WebElement> col = driver.findElements(By.xpath(xpathCol));
		List<WebElement> row = driver.findElements(By.xpath(xpathRow));*/

		WebElement baseTable = driver.findElement(By.tagName("table"));
		WebElement instance = baseTable.findElement(By.xpath(xpathRow));

		return instance;
	}

	/* validateByText method  */
	
	
	protected boolean isTextPresent(String text){
	    try{
	        boolean b = driver.getPageSource().contains(text);
	        return b;
	    }
	    catch(Exception e){
	        return false;
	    }
	  }
	
	

	public boolean validateByText(String text, String xpathExpression){

		String visibleText = driver.findElement(By.xpath(xpathExpression)).getText();
		if(visibleText.equalsIgnoreCase(text)){

			return true;
		}
		else{
			return false;
		}

	}

	/* waitUntilVisible method  */	

	public static void waitUntilVisible(String expression){

		WebDriverWait wait = new WebDriverWait(driver , 20);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(expression)))); 
	}

	/*
	 * 
	 */
	public static String  ScreenshotPath;
	public static String  screenShotName;
	public static String  timeStamp;
	public static  void captureScreenShot() throws IOException
	{

		File screFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

		screenShotName = "./Screenshots/"+"_Screenshot"+timeStamp+".jpg";

		FileUtils.copyDirectory(screFile, new File(System.getProperty("user.dir")+"\\target\\screenShotName"));
		FileUtils.copyFile(screFile, new File("D:\\selenium\\"+System.currentTimeMillis()+".png"));

	}

	/* captureScreenShotMethod method  */

	public static String captureScreenShotMethod(String className) throws IOException{
		String timeStamp;
		String screenShotName;
		File screenShot;
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		//The below method will save the screen shot in d drive with name "screenshot.png"
		timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		screenShotName = "./Screenshots/"+className+"_Screenshot"+timeStamp+".png";
		screenShot = new File(screenShotName);
		//screenShotName = new File("D:\\MyTest\\Screenshots\\"+timeStamp+".png");
		FileUtils.copyFile(scrFile, screenShot);

		return screenShotName;
	}

	public static void sendEnterKey(WebDriver driver, String identifier, Keys enter) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		properties.load(new FileReader(".//Data//ObjectRepository2.properties"));

		WebElement element = driver.findElement(By.xpath(properties.getProperty(identifier)));
		element.sendKeys(Keys.ENTER);
	}

	/* End methods  */	

}
