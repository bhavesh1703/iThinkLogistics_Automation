package iThink.Automation.pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import iThink.Automation.base.BasePage;
import iThink.Automation.modules.forwardOrders.AllTab;
import iThink.Automation.utils.CommonActions;
import iThink.Automation.utils.ConfigReader;
import iThink.Automation.utils.PageURLs;
import iThink.Automation.utils.WaitUtils;

public class Dashboard{
	
	private WebDriver driver;
	private WaitUtils wait;
	private CommonActions comm;
	private static final Logger logger = LogManager.getLogger(Dashboard.class);
	
	public Dashboard(WebDriver driver) {	
		this.driver = driver;
		this.wait = new WaitUtils(driver);
		this.comm = new CommonActions(driver);
		PageFactory.initElements(driver, this);	
		
	}
	
	@FindBy(xpath = "//div[@class='d-inline-block gray-800']")
	private WebElement textWelcome;
	
	public String getDashboardTitle() {
		return driver.getTitle();
	}
	
	public boolean isWelcomeDisplayed() {
		return comm.isElementDisplayed(textWelcome);
	}
	
	public void load() {
		logger.info("Waiting for Dashboard to load...");
		wait.waitForPageLoads(10);
		wait.waitForVisibility(By.xpath("//div[@class='d-inline-block gray-800']"));
		logger.info("Dashboard loads successfully.");		
	}
	
	public AllTab goToOrderAll() {
		load();
		logger.info("Navigating to All tab of Domestic- View Order...");
		comm.navigateTo(PageURLs.ALL);
		wait.waitForPageLoads(10);
		WebElement header = wait.waitForVisibility(By.xpath("//div[@class='page-header-section mb-3']/div/div/div/span"));
		logger.info("Navigated to All tab successfully.");
		return new AllTab(driver);
	}
	
	
	
	
	


}
