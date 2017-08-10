package lib;

public class ConnectPage extends Common{
	
	public static void enterConnectionSettings(String name, String scheme, String host, String port, String path){
		sendKeys("CONNECT_NAME_NAME", name);	
		select("CONNECT_SCHEME_NAME", scheme);		
		sendKeys("CONNECT_HOST_NAME", host);
		sendKeys("CONNECT_PORT_NAME", port);
		sendKeys("CONNECT_PATH_NAME", path);
	}
	
	public static void clickSave(){
		click("CONNECT_SAVE_XPATH");
	}
	
	public static String getSelectedConnection(){
		String selectedOption = getSelecedValue("CONNECT_CONNECTIONS_XPATH");
		return selectedOption;
	}	
}