package iThink.Automation.components;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import iThink.Automation.modules.forwardOrders.AllTab;
import iThink.Automation.utils.CommonActions;
import iThink.Automation.utils.WaitUtils;

public class SavedFilters {

	protected WebDriver driver;
	private CommonActions comm;
	private WaitUtils wait;
	private DataTable datatable;
	private static final Logger logger = LogManager.getLogger(SavedFilters.class);

	public SavedFilters(WebDriver driver) {
		this.driver = driver;
		this.comm = new CommonActions(driver);
		this.wait = new WaitUtils(driver);
		this.datatable = new DataTable(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "save-filter-input")
	private WebElement searchFilterInputbox;

	@FindBy(css = ".view-filter-data")
	private List<WebElement> savedFiltersList;

	private By filterNames = By.cssSelector(".filter-name");
	private By filterLi = By.xpath("//div[@class='block']/div/div");
	private By updateFilterIcon = By.xpath("//div[@class='filter-actions']//ul/li[3]");
	private By deleteFilterIcon = By.xpath("//div[@class='filter-actions']//ul/li[4]");

	public void searchFilterName(String filterName) {
		searchFilterInputbox.clear();
		comm.setInput(searchFilterInputbox, filterName);
	}

	public List<String> getSavedFilterNames() {
		wait.waitForAllElementsVisibility(filterNames);
		List<WebElement> filtersList = driver.findElements(filterNames);
		List<String> filters = new ArrayList<String>();
		for (WebElement filter : filtersList) {
			String name = filter.getText().trim();
			filters.add(name);
		}
		return filters;
	}

	private List<WebElement> getSavedFiltersListByNames() {
		wait.waitForAllElementsVisibility(filterNames);
		List<WebElement> filtersList = driver.findElements(filterNames);

		return filtersList;
	}

	private boolean isNoRecordFoundImageIsDisplayed() {
		WebElement image = driver.findElement(By.xpath("//div[@class='view-filter-body']/div/div/img"));
		wait.waitForVisibility(image);
		return image.isDisplayed();
	}

	private boolean isNoRecordFoundContextDisplayed() {
		List<WebElement> noRecord = driver.findElements(By.xpath("//div[@class='view-filter-body']/div/div/div"));
		WebElement noRecordText = noRecord.getFirst();
		WebElement noRecordContext = noRecord.getLast();
		return noRecordText.isDisplayed() || noRecordContext.isDisplayed();
	}
	
	public boolean isNoRecordFoundDisplayedInFiltersModalbox() {
		return (isNoRecordFoundImageIsDisplayed() && isNoRecordFoundContextDisplayed());
	}

	public WebElement selectFilterByName(String filterName) {
		if (!savedFiltersList.isEmpty()) {
			for (WebElement filter : getSavedFiltersListByNames()) {
				String name = filter.getText().trim();
//				String name = filter.findElement(filterNames).getText().trim();
//			System.out.println("Filter "+name+ " found.");

				if (name.equalsIgnoreCase(filterName)) {
					logger.info("Selecting filter : " + name);
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", filter);
					filter.click();
					WebElement filtername = driver.findElement(By.xpath("//div[@class='filter-dropdown']/span[2]"));
					wait.sleep(3);
//					WebElement filtername = driver.findElement(By.xpath("//div[@class='filter-dropdown']/span[2]"));
					logger.info(filtername.getText() + " is selected.");
					return filtername;

				}
			}
			throw new NoSuchElementException("No Such filter exists in current saved filters list.");

		} else if (isNoRecordFoundImageIsDisplayed() && isNoRecordFoundContextDisplayed()) {
			logger.info("There is No Saved Filters, and No Record found image is displayed");
		} else {
			logger.warn("No Record Found Image is not displayed.");
		}
		return null;
	}
	
	public String selectFirstfilter() {
//		if(!isNoRecordFoundDisplayedInFiltersModalbox()) {
//			WebElement firstFilter = getSavedFiltersListByNames().getFirst();
//			logger.info("The First filter name is : " + firstFilter.getText().trim());
//			return firstFilter.getText().trim();
//		} else {
//			logger.info("No saved filters are present.");
//			return "";
//		}
		wait.waitForVisibility(By.cssSelector(".view-filter-body"));
		 List<WebElement> filters = getSavedFiltersListByNames();
		 
		    if (filters.isEmpty()) {
		        logger.info("No saved filters are present in modal box.");
		        return null;
		    }

		    WebElement firstFilter = filters.get(0);
		    String filterName = firstFilter.getText().trim();
		    logger.info("The first filter name is: " + filterName);
//		    
		    try { 
		    	firstFilter.click();
		    	
		    } catch(ElementClickInterceptedException e) {
		    	logger.warn("Element click intercepted. Retrying with JS click...");
		        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstFilter);
		    }
		    return filterName;
	}
}
