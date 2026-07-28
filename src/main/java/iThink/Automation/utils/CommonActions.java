package iThink.Automation.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CommonActions {
	
	private final WebDriver driver;
	private final WaitUtils wait;
	
	
	public CommonActions(WebDriver driver) {	
		this.driver = driver;
		this.wait = new WaitUtils(driver);
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
		if(!isTextBoxEmpty(inputbox)) {
			return inputbox.getAttribute("value");
		}
			return "Text box is empty...";
	}
	
	public void setInput(WebElement inputbox, String input) {
		if(isElementDisplayed(inputbox)) {
			inputbox.clear();
			inputbox.sendKeys(input);
		} else {
			throw new RuntimeException("Inputbox is not displayed");
		}
	}
	
	public void clickButton(WebElement button) {
		if (isElementDisplayed(button)) {
			wait.waitForClickable(button);
			button.click();
		} else {
			 throw new RuntimeException("Button is Not Displayed");
		}
	}
	
	public String getElementText(WebElement element) {
		if (isElementDisplayed(element)) {
			return element.getText().trim();
		}
		return "Text is not present.";
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
