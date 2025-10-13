package iThink.Automation.base;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import iThink.Automation.utils.ConfigReader;
import iThink.Automation.utils.DevToolsManager;
import iThink.Automation.utils.DriverFactory;

public class BaseTest {

	protected WebDriver driver;
	protected DevToolsManager network;
	private static final Logger logger = LogManager.getLogger(BaseTest.class);

	@BeforeMethod
	public void setup() {
		// Load the browser from config file and initate it from DriverFactory class
		logger.info("Setting up the browser...");
		String browser = ConfigReader.getProperty("browser");
//		String browser = "chrome";
		driver = DriverFactory.initDriver(browser);

		// Browser basic setup
		logger.info("Navigating to Base URL...");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get(ConfigReader.getProperty("baseUrl"));
//		driver.get("https://my.ithinklogistics.net");
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(7));
		
//		network = new DevToolsManager(driver);
//		network.startCapture();
//		logger.info("Network capture started...");
	}
	
//	@Test
//	public void test() {
//		System.out.println(driver.getTitle());
//	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			logger.info("Closing the browser...");
			driver.quit();
		}
	}

}
