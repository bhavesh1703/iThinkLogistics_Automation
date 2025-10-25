package iThink.Automation.modules.forwardOrders;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import iThink.Automation.components.DataTable;
import iThink.Automation.components.SavedFilters;
import iThink.Automation.components.WebTable;
import iThink.Automation.pages.Dashboard;
import iThink.Automation.utils.CommonActions;
import iThink.Automation.utils.DevToolsManager;
import iThink.Automation.utils.WaitUtils;

public class AllTab {

	private WebDriver driver;
	private WaitUtils wait;
	private CommonActions comm;
	private DataTable datatable;
	private SavedFilters filtersModalbox;
	private DevToolsManager devtools;
	private WebTable webtable;
	private static final Logger logger = LogManager.getLogger(AllTab.class);

	public AllTab(WebDriver driver) {
		this.driver = driver;
		this.wait = new WaitUtils(driver);
		this.comm = new CommonActions(driver);
		this.datatable = new DataTable(driver);
		this.filtersModalbox = new SavedFilters(driver);
		this.devtools = new DevToolsManager(driver);
		this.webtable = new WebTable(driver);
		PageFactory.initElements(driver, this);
	}
//	
//	public void load() {
//		logger.info("Waiting for Order All tab to load");
//		wait.waitForPageLoads(10);
//		wait.waitForVisibility(By.xpath("//div[@class='d-inline-block gray-800']"));
//		logger.info("Dashboard loads successfully...");		
//	}

	public List<String> getAllTabsActiveColumns() {
		return datatable.getListOfActiveTableHeaders();
	}

	public String universalAWBSearch(String AWB) {
		datatable.setUniversalAwbSearch(AWB);
		boolean isEntriesDisplayed = datatable.getEntryRowCount() >= 1;
		if (isEntriesDisplayed) {
			return datatable.getAwbFromDatatable();

		} else {
			logger.info("No Record Found Displayed.");
			return datatable.getNoRecordText();
	}
//		throw new IllegalStateException("Unexpected state: Neither rows found nor 'No Record Found' displayed.");
	}

	public List<String> getDateOptionsOnPage() {
		datatable.clickPageDateFilter();
		logger.info("getting all options of Date filter...");
//		wait.sleep(3);
//		wait.waitForAllElementsVisibility(By.xpath("//div[@role='dialog']//div/div/p"));
		return datatable.getDateOptions();
	}

	public void selectDateOptionOnPage(String dateOption) {
		datatable.selectDateFilterOptionOnPage(dateOption);
	}

	public String getSelectedDateOnPage() {
		datatable.clickPageDateFilter();
		return datatable.getSelectedDateOption();
	}

//	public void selectCustomRange(String fromDay, String toDay, String fromMonth, String toMonth, String year) {
//	public void selectCustomRange(String dateRange) {
	public void selectCustomRange(String fromDate, String toDate) {
//		String[] dates = parseDateRange(dateRange);
//		String[] fromParts = splitDate(dates[0]);
//		String[] toParts = splitDate(dates[1]);
//		
//		String fromDay = fromParts[0];
//		String fromMonth = fromParts[1];
//		String year = fromParts[2];
//		
//		String toDay = toParts[0];
//		String toMonth = toParts[2];
//		
//		datatable.selectDateFilterOptionOnPage("Custom Range");
//		wait.waitForAllElementsVisibility(By.xpath("//select"));
//		
//		datatable.selectYearOption(year);
//		datatable.selectFromMonth(fromMonth);
//		datatable.selectFromDay(fromDay);
//		wait.sleep(5);
//		
//		datatable.selectToMonth(toMonth);
//		datatable.selectToDay(toDay);
//		wait.sleep(5);
//		
//		datatable.clickDateRangeApplyButton();
//		datatable.selectDateFilterOptionOnPage("Custom Range");
		wait.waitForAllElementsVisibility(By.xpath("//select"));
		datatable.selectCustomRangeDateOption(fromDate, toDate);
//		datatable.clickApplyFilterButton();

	}

	public String getSelectedCustomRange() {
		WebElement selectedRange = driver
				.findElement(By.xpath("//div[@class='align-middle ml-[6px] pt-[3px] text-sm text-itl-primary']"));
		return selectedRange.getText().trim();
	}

	public boolean isDefaultSelectedDateIsLast30Days() {
		datatable.clickPageDateFilter();
		return getSelectedDateOnPage().equalsIgnoreCase("Last 30 Days");

	}

	private String[] parseDateRange(String dateRange) {
		// Split given date range in 'from' and 'to' format.
		String[] parts = dateRange.split(":");
		if (parts.length != 2) {
			throw new IllegalArgumentException("Invalid Date Range Input. Expected Format - 'dd-MMM-yyyy:dd-MMM-yyyy'");
		}
		return parts;
	}

	private String[] splitDate(String date) {
		return date.split("-");
	}

	/** Gets First AWB from Datatable **/
	public String getFirstAWBFromDatatable() {
		return datatable.getAwbFromDatatable();
	}

	public void clickOnFilterButton() {
		datatable.clickOnFiltersButton();
		if (datatable.isFiltersModalboxOpens()) {
			logger.info("Filter Modal box opens");
		} else {
			logger.warn("Filter Modalbox failed to open.");
		}
	}

	public List<String> getAllTabFiltersList() {
		return datatable.getListOfFilters();
	}

	public List<String> getCurrentListOfOrderIDs() {
		return datatable.getOrderIDList();
	}

	public String getThirdOrderIDFromDatable() {
		try {
			if (getCurrentListOfOrderIDs().size() > 4 && (!datatable.isNoRecordDisplayed())) {
				return getCurrentListOfOrderIDs().get(2);
			} else {
				return getCurrentListOfOrderIDs().getFirst();
			}
		} catch (Exception e) {
			throw new RuntimeException("Cannot get the Order ID", e);
		}
	}

	public List<String> getAllTabAllFilters() {
		return datatable.getFiltersHeaders();
	}

	public void shippingAWBfilter(String logisticsName) { // need to check.
//		wait.waitForTextToBePresent(null, getFirstAWBFromDatatable())
		datatable.waitForLoadOrderList();
		datatable.goToFilter("Shipping AWB");
		datatable.selectLogisticsInShippingAWBFilter(logisticsName);

	}

	public List<String> getSelectedLogistic() { // Need to check.
		return datatable.getSelectedLogistics();
	}

	/** Takes the first Order ID in datatable and searched in Order ID Filter. And validates is Li is created or not.
	 * @return true and false if order Id is searched or not and Li is created or not.*/
	public boolean searchValidOrderID() {
		String orderID = datatable.getOrderIDList().getFirst();
		wait.sleep(3);
		datatable.clickOnFiltersButton();
		datatable.setOrderIDFilter(orderID);
		devtools.startCapture();
		datatable.clickApplyFilterButton();
		devtools.stopCapture();
		wait.sleep(3);
		logger.info("Order Id Filter applied successfully.!");
		
//		Optional<String> countStr = devtools.getJSONFieldFromResponse("order/forward/get/count", "count");
//		countStr.ifPresent(c -> System.out.println("Count value: "+ c));
		List<WebElement> liList = driver.findElements(By.xpath("//div[@class='block']/div/div"));

		if (!liList.isEmpty()) {
			WebElement orderIDLi = liList.getFirst();
			String orderIdOnLi = orderIDLi.getText().trim();
			String orderIDTrimmed = orderID.trim();

			System.out.println("UI Value on Li : " + orderIdOnLi);
			System.out.println("Filter Value that applied : " + orderIDTrimmed);
//			String lower = orderIdOnLi.toLowerCase();
//			System.out.println(lower);

			if (orderIDTrimmed.contains(orderIDTrimmed)) {
				logger.info("Order ID : '" + orderID + "' searched successfully!");
				return true;
			} else {
				logger.warn("searched Order ID : " + orderID + " not found");
				return datatable.isNoRecordDisplayed();
			}

		}
		return false;
	}
	
	public String getAPICountAfterFilter() {
		Optional<String> apiCount = devtools.getJSONFieldFromResponse("order/forward/get/count", "count");
		
		if(apiCount.isPresent()) {
			logger.info("API Count of: {}", apiCount.get());
			return apiCount.get();
		} else {
			logger.warn("API count not found in response!");
			return "";
		}
	}
	
	
	public boolean searchInvalidOrderID(String invalidOrderID) {
		wait.sleep(3);
		datatable.clickOnFiltersButton();
		datatable.setOrderIDFilter(invalidOrderID);
		datatable.clickApplyFilterButton();
		wait.sleep(5);
//		return datatable.isNoRecordDisplayed();
		WebElement norecordImage = driver.findElement(By.xpath("//div[@class='no-record-outer flex flex-col justify-center items-center gap-1']/img"));
		if(norecordImage.isDisplayed()) {
			logger.info("No Record found is displayed.");
			return true;
		} else {
			logger.warn("No Record found not displayed");
			return false;
		}
	}
	
	public boolean riskFilter(String riskType) {
		datatable.setRiskFilter(riskType);
		devtools.startCapture();
		datatable.clickApplyFilterButton();
		devtools.stopCapture();
		wait.sleep(2);
		
		List<WebElement> liList = driver.findElements(By.xpath("//div[@class='block']/div/div"));
		
		if (!liList.isEmpty()) {
			WebElement riskLi = liList.getFirst();
			String riskTypeOnLi = riskLi.getText().trim();

			System.out.println("UI Value on Li : " + riskTypeOnLi);
			if (riskTypeOnLi.contains(riskType)) {
				logger.info("'" + riskType + "' selected successfully!");
				return true;
			} else {
				logger.warn("Searched Risk type: " + riskType + " not found");
				return datatable.isNoRecordDisplayed();
			}

		}
		return false;
	}
	
	
	public String getAllTabCurrentPagination() {
		return datatable.getPaginationCount();
	}
	

	public void amountFilter(String condition, String...values) {
		datatable.goToFilter("Amount");
		datatable.setAmountFilter(condition, values);
//		datatable.clickApplyFilterButton();
	}
	
	/**set the value of min Field and get the Actual Dom Value**/
	public void amountMinFieldValue(String minValue) {
		datatable.goToFilter("Amount");
		WebElement orderMin = driver.findElement(By.xpath("//div[@id='order_amount']//input[@placeholder='Min']"));
		String beforeMinValue = orderMin.getDomProperty("value");
		orderMin.clear();
		datatable.setMinValue(orderMin, minValue);
		wait.sleep(1);
		String afterMinValue = orderMin.getDomProperty("value");
		
		if(afterMinValue == null || afterMinValue.isEmpty()) {
			logger.info("Input value : " + minValue + " is restricted to enter.");
		} else if (afterMinValue.equals(minValue)) {
			logger.info("Input value : " + minValue + " is accepted. Value in inputbox : " + afterMinValue);
		} else {
			logger.info("Input value: " + minValue+" accepts partially. Value in inputbox : "+ afterMinValue);
		}
		
	}
	
	public void amountMaxFieldValue(String maxValue) {
		datatable.goToFilter("Amount");
		WebElement orderMax = driver.findElement(By.xpath("//div[@id='order_amount']//input[@placeholder='Max']"));
		String beforeMaxValue = orderMax.getDomProperty("value");
		orderMax.clear();
		datatable.setMaxValue(orderMax, maxValue);
		wait.sleep(1);
		String afterMaxValue = orderMax.getDomProperty("value");
		
		if(afterMaxValue == null || afterMaxValue.isEmpty()) {
			logger.info("Input value : " + maxValue + " is restricted to enter.");
		} else if (afterMaxValue.equals(maxValue)) {
			logger.info("Input value : " + maxValue + " is accepted. Value in inputbox : " + afterMaxValue);
		} else {
			logger.info("Input value: " + maxValue+" accepts partially. Value in inputbox : "+ afterMaxValue);
		}
		
	}
	
	public void clickApplyFilterButtonInAllTab() {
		devtools.startCapture();
		datatable.clickApplyFilterButton();
		devtools.stopCapture();
		
		devtools.printAPIResponseStatus("forward/get/count");
	}
	
	public String getToastMessageInAllTab() {
		return datatable.getToastMessageError();
	}
	
	/** Select the Exisiting filter By Name if there is no filter is present
	 * then it will also check that No Record found is displayed or not.
	 * @param filterName - existing saved filter name.
	 */
	public void selectSavedFilterByName(String filterName) {
		datatable.clickOnSavedFilterButton();
		filtersModalbox.selectFilterByName(filterName);
	}
	
	public String selectFirstSavedFilter() {
		datatable.clickOnSavedFilterButton();
		return filtersModalbox.selectFirstfilter();
	}
	
	public String getAppliedFilterName() {
		return datatable.getCurrentAppliedFilter();
	}
	
	public List<String> getListOfLiOnDatatable() {
		if(!datatable.getListOfCurrentFilterLi().isEmpty()) {
		return datatable.getListOfCurrentFilterLi();
	} else {
		return null;
		
	}
	}
 	
	public boolean isUpdateButtonDisplayed() {
		return datatable.isUpdateButtonDisplayed();
	}
	
	public void clickOnExportOptions() {
		datatable.clickOnExportOptionsButton();
	}
	
	public void clickOnExportButton() {
		datatable.clickOnExportButton();
	}
	
	public void getCellClasses() {
		webtable.getClassNameOfEachCell();
	}
}
