package iThink.Automation.utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {

	private WebDriver driver;
	private WebDriverWait wait;
//	private long defaultTimeout;

	public WaitUtils(WebDriver driver, long timeoutInSeconds) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
	}

	public WaitUtils(WebDriver driver) {
		this(driver, 10);
	}
	
	public boolean waitForUrlToBe(String url) {
		return wait.until(ExpectedConditions.urlToBe(url));
	}
	
	public boolean waitForTitleToBe(String title) {
		return wait.until(ExpectedConditions.titleIs(title));
	}

	/** wait until element is appear in DOM **/
	public WebElement waitForElementToBePresent(By locator) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/** wait until element is visible in Page **/
	public WebElement waitForVisibility(WebElement element) {
		return wait.until(ExpectedConditions.visibilityOf(element));
	}

	public WebElement waitForVisibility(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public List<WebElement> waitForAllElementsVisibility(List<WebElement> elements) {
		return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
	}

	public List<WebElement> waitForAllElementsVisibility(By locator) {
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	public WebElement waitForClickable(WebElement element) {
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public WebElement waitForClickable(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public boolean waitForTextToBePresent(WebElement element, String text) {
		return wait.until(ExpectedConditions.textToBePresentInElementValue(element, text));
	}
	
	public void waitForPageLoads(long timeout) {
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeout));
	}

	public boolean isSkeletonDisplayed() {
		return wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath
				("//div[@class='p-datatable-loading-rows']//div[1]//div[5]//div[1]")));
	}
	
	public void waitForSkeletonToDisappear() {
		By skeletonLocator = By.cssSelector("div.p-skeleton.p-component.rounded-md");
		
		
	}
	public void sleep(long seconds) {
		try {
			Thread.sleep(Duration.ofSeconds(seconds));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.err.println("Sleep was interrupted: " + e.getMessage());
		}
	}

}
