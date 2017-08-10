package tests;

import java.util.List;

import lib.Common;
import lib.ConnectPage;
import lib.WelcomePage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Demo test
 * 
 * @author Mahendra.Jaiswal
 *
 */

public class TestConnectionPage {
	
	@BeforeMethod
	public void setup(){	
		Common.goToBaseUrl();
	}
	
	@Test
	public void createNewConnection(){		
		WelcomePage.clickConnectLink();
		
		List<String> testData = Common.getTestData("TestConnectionPage.createNewConnection");
		String name = testData.get(0);
		String scheme = testData.get(1);
		String host = testData.get(2);
		String port = testData.get(3);
		String path = testData.get(4);
		
		ConnectPage.enterConnectionSettings(name, scheme, host, port, path);
		ConnectPage.clickSave();
		String currentSelection = ConnectPage.getSelectedConnection(); 
		Assert.assertTrue(currentSelection.equals(name), "Expected conection name " +name+ " not displayed in connections list");
	}
}