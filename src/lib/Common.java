package lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

/**
 * Library for common APIs 
 * 
 * @author Mahendra.Jaiswal
 *
 */
public class Common {

	private static Properties objectMapProps;
	private static XSSFSheet sheet;
	private static WebDriver driver;
	private static String baseUrl;
	private static String screenshotsFolder;

	public static void sleep(int millSec){
		try {
			Thread.sleep(millSec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void launchBrowser(String browser, String profile, String driverFile){
		
		if(browser.endsWith("firefox.exe")){
			System.setProperty("webdriver.gecko.driver", driverFile);
			System.setProperty("webdriver.firefox.bin", browser);
			FirefoxProfile pro = new FirefoxProfile(new File(profile));
			driver = new FirefoxDriver(pro);		
		}
		
		if(browser.endsWith("chrome.exe")){
			System.setProperty("webdriver.chrome.driver", driverFile);
			driver = new ChromeDriver();			
		}
		
		if(browser.endsWith("iexplore.exe")){
			System.setProperty("webdriver.ie.driver", driverFile);
			driver = new InternetExplorerDriver();			
		}
		
		driver.manage().window().maximize();
	}

	public static WebDriver getDriver(){
		return driver;
	}

	public static void launchUrl(String url){
		driver.get(url);
		baseUrl = url;
	}	

	public static void setTimeOuts(int pageLoadTimeOutInSec, int implicitWaitInSec){
		driver.manage().timeouts().pageLoadTimeout(pageLoadTimeOutInSec, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(implicitWaitInSec, TimeUnit.SECONDS);
	}

	public static void switchToDefaultContent(){
		driver.switchTo().defaultContent();
	}

	public static void switchToFrame(String id){
		driver.switchTo().frame(getObjectValue(id));
	}

	public static void quit(){		
		driver.quit();
	}

	public static void setObjectMapFile(String configFilePath){
		objectMapProps = new Properties();
		InputStream fis;
		try {
			fis = new FileInputStream(configFilePath);
			objectMapProps.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getObjectValue(String objectName){
		return objectMapProps.getProperty(objectName);
	}

	public static void setTestDataFile(String testDataFilePath){

		try {			
			XSSFWorkbook workbook = new XSSFWorkbook (testDataFilePath);		
			sheet = workbook.getSheetAt(0);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	public static ArrayList<String> getTestData(String testCaseName){
		ArrayList<String> testData = new ArrayList<String>();

		Iterator<Row> ite = sheet.rowIterator();
		while(ite.hasNext()){
			Row row = ite.next();
			Iterator<Cell> cite = row.cellIterator();
			while(cite.hasNext()){
				Cell c = cite.next();
				testData.add(c.toString());
			}
			if(testData.get(0).equals(testCaseName)){
				testData.remove(testCaseName);
				return testData;		
			}
			testData = null;
			testData = new ArrayList<String>();
		}
		return testData;
	}	

	public static void setScreenshotFolder(String folder){
		screenshotsFolder = folder;
	}
	
	public static void captureScreenshot(String screenshotFileName){
		String screenshotFile = screenshotsFolder + "/" + screenshotFileName;
		try {
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(screenshotFile));
			System.out.println("Saved screenshot: " + screenshotFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (NullPointerException npe) {
			System.out.println("Failed to capture screenshot to file : " + screenshotFile);
			npe.printStackTrace();
		}
	}

	public static void goToBaseUrl(){
		if(!driver.getCurrentUrl().equals(baseUrl))
			driver.get(baseUrl);
	}

	// Click	
	public static void click(String locator){
		getElement(locator).click();
	}	

	// sendKeys
	public static void sendKeys(String locator, String value){
		WebElement ele = getElement(locator);
		ele.clear();
		ele.sendKeys(value);
	}

	// Select
	public static void select(String locator, String value){
		WebElement ele = getElement(locator);
		Select listBox = new Select(ele);
		listBox.selectByVisibleText(value);
	}

	public static String getSelecedValue(String locator){
		WebElement ele = getElement(locator);
		Select listBox = new Select(ele);
		WebElement selectedEle = listBox.getFirstSelectedOption();
		return selectedEle.getText();
	}	

	// getText
	public static String getText(String locator){
		return getElement(locator).getText();
	}

	// dragAndDrop
	public static void dragAndDrop(String sourceLocator, String targetLocator){
		Actions actions = new Actions(driver);
		actions.dragAndDrop(getElement(sourceLocator), getElement(targetLocator)).perform();
	}

	// Verification
	public static boolean isElementPresent(String locator){
		List<WebElement> eles = getElements(locator);
		return eles.size() > 0;
	}

	public static boolean isElementDisplayed(String locator){
		List<WebElement> eles = getElements(locator);
		if(eles.size() == 0)
			return false;

		return eles.get(0).isDisplayed();
	}

	public static List<WebElement> getElements(String locator){
		if(locator.toLowerCase().endsWith("_id"))
			return driver.findElements(By.id(getObjectValue(locator)));

		if(locator.toLowerCase().endsWith("_name"))
			return driver.findElements(By.name(getObjectValue(locator)));

		if(locator.toLowerCase().endsWith("_class"))
			return driver.findElements(By.className(getObjectValue(locator)));

		if(locator.toLowerCase().endsWith("_css"))
			return driver.findElements(By.cssSelector(getObjectValue(locator)));

		if(locator.toLowerCase().endsWith("_link"))
			return driver.findElements(By.linkText(getObjectValue(locator)));

		if(locator.toLowerCase().endsWith("_partiallink"))
			return driver.findElements(By.partialLinkText(getObjectValue(locator)));

		if(locator.toLowerCase().endsWith("_xpath"))
			return driver.findElements(By.xpath(getObjectValue(locator)));
		
		if(locator.toLowerCase().endsWith("_tag"))
			return driver.findElements(By.tagName(getObjectValue(locator)));

		throw new RuntimeException("INVALID LOCATOR: " + locator);
	}

	public static WebElement getElement(String locator){
		if(locator.toLowerCase().endsWith("_id"))
			return driver.findElement(By.id(getObjectValue(locator)));

		if(locator.toLowerCase().endsWith("_name"))
			return driver.findElement(By.name(getObjectValue(locator)));

		if(locator.toLowerCase().endsWith("_class"))
			return driver.findElement(By.className(getObjectValue(locator)));

		if(locator.toLowerCase().endsWith("_css"))
			return driver.findElement(By.cssSelector(getObjectValue(locator)));

		if(locator.toLowerCase().endsWith("_link"))
			return driver.findElement(By.linkText(getObjectValue(locator)));

		if(locator.toLowerCase().endsWith("_partiallink"))
			return driver.findElement(By.partialLinkText(getObjectValue(locator)));

		if(locator.toLowerCase().endsWith("_xpath"))
			return driver.findElement(By.xpath(getObjectValue(locator)));
		
		if(locator.toLowerCase().endsWith("_tag"))
			return driver.findElement(By.tagName(getObjectValue(locator)));

		throw new RuntimeException("INVALID LOCATOR: " + locator);
	}
}