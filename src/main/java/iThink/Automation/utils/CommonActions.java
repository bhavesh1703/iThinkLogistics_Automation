package iThink.Automation.utils;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import iThink.Automation.base.BasePage;

public class CommonActions {
	
	private WebDriver driver;
	private WaitUtils wait;
	
	
	public CommonActions(WebDriver driver) {	
		this.driver = driver;
		this.wait = new WaitUtils(driver);
		PageFactory.initElements(driver, this);
		
	}
	
	public String getCurrentPageUrl() {
		return driver.getCurrentUrl();
	}
	
	public String getCurrentPageTitle() {
		return driver.getTitle();
	}
	
	public void navigateTo(String relativeUrl) {
		String fullUrl = ConfigReader.getProperty("baseUrl") + relativeUrl;
		driver.get(fullUrl);
	}
	
	public boolean isTextBoxEmpty(WebElement inputbox) {
		String value = inputbox.getAttribute("value");
		return value == null || value.isEmpty();
	}
	
	public String getInputboxValue(WebElement inputbox) {
		wait.waitForVisibility(inputbox);
		if(isTextBoxEmpty(inputbox)) {
			return inputbox.getAttribute("value");
		} else {
			return "Text box is empty...";
		}
	}
	
	public void setInput(WebElement inputbox, String input) {
		if(isElementDisplayed(inputbox)) {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			inputbox.sendKeys(input);
		} else {
			throw new RuntimeException("Inputbox is not displayed");
		}
	}
	
	public void clickButton(WebElement button) {
		if (isElementDisplayed(button)) {
			wait.waitForVisibility(button);
			button.click();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		} else {
			 throw new RuntimeException("Button is Not Displayed");
		}
	}
	
	public String getElementText(WebElement element) {
		return isElementDisplayed(element) ? element.getText().trim() : "Text is not present"; 
	}
	
	public boolean isElementDisplayed(WebElement element) {
	    try {
	        wait.waitForVisibility(element);
	        return element.isDisplayed();
	    } catch (Exception e) {
	        return false;
	    }
	}


}
