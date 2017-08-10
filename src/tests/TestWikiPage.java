package tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import lib.Common;
import lib.WelcomePage;
import lib.WikiPage;

public class TestWikiPage {
	
	@BeforeMethod
	public void setup(){
		Common.goToBaseUrl();
	}
	
	@Test
	public void createPageWithSpace(){		
		WelcomePage.clickWikiLink();
		List<String> data = Common.getTestData("TestWikiPage.createPageWithSpace");		
		WikiPage.createNewWiki(data.get(0));
		
		boolean errorFound = WikiPage.isErrorDisplayed(); 
		
		Assert.assertTrue(errorFound, "Expected error not found");
	}

}





