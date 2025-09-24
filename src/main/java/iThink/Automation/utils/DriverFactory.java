package iThink.Automation.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
	
	protected static WebDriver driver;
	
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
				break;
				
			case "chrome":
			default:
				WebDriverManager.chromedriver().setup();
				driver =  new ChromeDriver();
		}
		
		return driver;
	}
	
}
