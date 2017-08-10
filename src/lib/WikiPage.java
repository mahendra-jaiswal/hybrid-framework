package lib;

public class WikiPage extends Common{
	
	public static void createNewWiki(String name){
		click("WIKI_CREATE_XPATH");
		sendKeys("WIKI_NAME_ID", name);
	}
	
	public static boolean isErrorDisplayed(){
		boolean errorFound = isElementDisplayed("WIKI_ERROR_XPATH");
		return errorFound;
	}
}
