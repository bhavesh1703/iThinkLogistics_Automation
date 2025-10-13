package iThink.Automation.utils;


import java.util.Optional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v137.network.Network;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
	
	protected static WebDriver driver;
	protected static DevTools devTools;
	
	public static WebDriver initDriver(String browser) {
		
		switch(browser.toLowerCase()) {
			case "edge":
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
				break;
			
			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
				break;
				
			case "chrome-headless":
				WebDriverManager.chromedriver().setup();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--headless=new");
				options.addArguments("--disable-gpu");
				options.addArguments("--window-size=1980,1080");
				driver = new ChromeDriver(options);
				
//				devTools = ((ChromeDriver) driver).getDevTools();
//				devTools.createSession();
//				devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
				break;
				
			case "chrome":
			default:
				WebDriverManager.chromedriver().setup();
				ChromeOptions chromeoptions = new ChromeOptions();
				driver =  new ChromeDriver(chromeoptions);
				
//				devTools = ((ChromeDriver) driver).getDevTools();
//				devTools.createSession();
//				devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
				break;
		}
		
		return driver;
	}
	
	public static DevTools getDevTools() {
		return devTools;
	}
	
	public static WebDriver getDriver() {
		return driver;
	}
	
}
