package tests;

import lib.Common;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

public class AutomationSetup {
	
	@BeforeSuite
	@Parameters({"browser", "profile", "driverFile", "url", "objectMapFile", "testDataFile", "pageLoadTimeOutInSec", 
		"implicitWaitInSec", "screenshotsFolder"})
	public void setup(String browser, String profile, String driverFile, String url, String objectMapFile, 
			String testDataFile, String pageLoadTimeOutInSec, String implicitWaitInSec, String screenshotsFolder){
		
		Common.setTestDataFile(testDataFile);
		
		Common.setObjectMapFile(objectMapFile);		

		Common.launchBrowser(browser, profile, driverFile);
		
		Common.setTimeOuts(Integer.parseInt(pageLoadTimeOutInSec), Integer.parseInt(implicitWaitInSec));
		
		Common.launchUrl(url);
		
		Common.setScreenshotFolder(screenshotsFolder);
	}
}
