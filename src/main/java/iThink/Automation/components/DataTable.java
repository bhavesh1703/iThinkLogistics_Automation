package iThink.Automation.components;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

import iThink.Automation.utils.CommonActions;
import iThink.Automation.utils.DateRangeType;
import iThink.Automation.utils.DateUtils;
import iThink.Automation.utils.WaitUtils;

public class DataTable {

	private WebDriver driver;
	private WaitUtils wait;
	private CommonActions comm;
	private Actions action;
	private DateUtils date;
	private static final Logger logger = LogManager.getLogger(DataTable.class);

	public DataTable(WebDriver driver) {
		this.driver = driver;
		this.wait = new WaitUtils(driver);
		this.comm = new CommonActions(driver);
		this.action = new Actions(driver);
		this.date = new DateUtils(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//div[@class='search-outer']/input")
	private WebElement universalAwbSearchInput;

	@FindBy(xpath = "//table/thead/tr/th/div/div/div/div[@class='text-light-800 p-column-title']")
	private List<WebElement> tableHeaders;

	@FindBy(xpath = "//div[@class='no-record-outer flex flex-col justify-center items-center gap-1']/img")
	private WebElement noRecordFoundImage;

	@FindBy(xpath = "//div[@class='no-record-outer flex flex-col justify-center items-center gap-1']/div")
	private List<WebElement> noRecordFoundContext;

	@FindBy(xpath = "//table/tbody/tr[@role='row']")
	private List<WebElement> tableEntryRow;

	@FindBy(xpath = "//table/tbody/tr/td[@class='awb_no_logistics data-table-header']/div/div/*")
	private List<WebElement> awbNoWithLogo;

	@FindBy(xpath = "//table/tbody/tr/td[contains(@class,'order_id data-table-header')]/div/div/div")
	private List<WebElement> orderIDText; // All OrderID's on Datatable.

	@FindBy(xpath = "//div[@class='datepicker-outer']")
	private WebElement pageDateFilter;

	@FindBy(xpath = "//div/p")
	private List<WebElement> dateFilterOptions;

	@FindBy(xpath = "//span[@class='filters nest-hub-max-inner-filter']")
	private WebElement filtersButton;

	@FindBy(xpath = "//div/p[@class='header']")
	private WebElement allFiltersHeader;

	@FindBy(xpath = "//div[@class='w-[168px] p-2 lists hover:cursor-pointer']")
	private List<WebElement> filtersList;

	@FindBy(xpath = "//div/span[@class='right-filter-header pl-[14px]']")
	private List<WebElement> allFiltersHeaders;

	@FindBy(xpath = "//div[@class='flex gap-2 filter-modal-btn-div']/button[1]")
	private WebElement applyFilterButton;

	@FindBy(xpath = "//div[@class='flex gap-2 filter-modal-btn-div']/button[2]")
	private WebElement closeFilterButton;

	@FindBy(xpath = "//div/div/img[@alt='Search']")
	private List<WebElement> allSearchIconsInFilters; // captures all search icons in filter modalbox

	@FindBy(xpath = "//div[@id='order_id']/div/*")
	private List<WebElement> orderIDFilterField; // captures both search icon and inputbox in filter modalbox

	@FindBy(xpath = "//div[@id='order_id']/div/input")
	private WebElement orderIDFilterInputbox;

	@FindBy(xpath = "//div/div[@data-pc-section='label' and .='Shipping AWB']")
	private WebElement shippingAWBFilterDropdown;

	@FindBy(xpath = "//div[@class='p-multiselect-items-wrapper']/ul/li")
	private List<WebElement> logisticsOptionsInShippingFilters;
	
	@FindBy(xpath = "//div[@id='order_amount']//span")
	private WebElement amountFilterSelectRangeDropdown;
	
	@FindBy(xpath = "//div[@id='order_amount']//input[@placeholder='Min']")
	private WebElement minInputbox;
	
	@FindBy(xpath = "//div[@id='order_amount']//input[@placeholder='Max']")
	private WebElement maxInputbox;
	
	@FindBy(xpath = "//div[@class='filter-dropdown']")
	private WebElement savedFilterButton;
	
	@FindBy(xpath = "//div[@class='block']//div/div")
	private List<WebElement> filterLiOnDatatable;

//	@FindBy(xpath = "//div[@class='datepicker-outer']")	//Date Picker on
//	private WebElement datePickerElement;
//	
	/**
	 * Search the Single AWB in Universal Search
	 * 
	 * @param AWB Number
	 */
	public void setUniversalAwbSearch(String AWB) {
		wait.waitForVisibility(universalAwbSearchInput);
		universalAwbSearchInput.clear();
		logger.info("Entering AWB...");
		comm.setInput(universalAwbSearchInput, AWB);
//		action.sendKeys(Keys.ENTER).perform();
		comm.clickEnterButton();
		logger.info("AWB No. " + AWB + " has been entered in universal search.");
//		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		wait.sleep(2);
	}

	/**
	 * Get the All Active Column Names
	 * 
	 * @return Column Headers in List of String.
	 */
	public List<String> getListOfActiveTableHeaders() {
		wait.waitForAllElementsVisibility(tableHeaders);
		List<String> headers = new ArrayList<>();

		for (WebElement header : tableHeaders) {
			wait.waitForVisibility(header);
			String text = header.getText().trim();
			headers.add(text);
		}
		return headers;
	}

	/**
	 * Private Method to check the No Record Found Image is Displayed or not.
	 * 
	 * @return True or False
	 */
	private boolean isNoRecordFoundImageDisplayed() {
		wait.waitForVisibility(
				By.xpath("//div[@class='no-record-outer flex flex-col justify-center items-center gap-1']/img"));
		return noRecordFoundImage.isDisplayed();
	}

	/**
	 * Private Method to check the No Record Found context is Displayed or not.
	 * 
	 * @return True or False
	 */
	private boolean isNoRecordFoundContextDisplayed() {
		wait.waitForAllElementsVisibility(noRecordFoundContext);
		return noRecordFoundContext.isEmpty();
	}

	/**
	 * This Method checks the No Record Found Image and Context are Displayed or
	 * not.
	 * 
	 * @return True or False
	 */
	public boolean isNoRecordDisplayed() {
		if (isNoRecordFoundImageDisplayed() && isNoRecordFoundContextDisplayed()) {
			logger.info("No Record found Displayed");
			return true;
		} else {
			logger.warn("No Record found not displayed!");
			return false;
		}
	}
	
	public String getNoRecordText() {
		WebElement noRecord = driver.findElement(By.xpath("//div[@class='head']"));
		return noRecord.getText().trim();
	}

	/**
	 * Get the currently displayed row or entry count
	 * 
	 * @return count of current displaying table entries in integer.
	 */
	public int getEntryRowCount() {
		String rowXpath = "//table/tbody/tr[@data-pc-section='%s']";
		String rowAttribute = "bodyrow";
		String noRecordAttribute = "emptymessage";
//		wait.waitForAllElementsVisibility(By.xpath("//table/tbody/tr[@data-pc-section='bodyrow']"));
		List<WebElement> rows = driver.findElements(By.xpath(String.format(rowXpath, rowAttribute)));
		
		if(!rows.isEmpty()) {
			logger.info("Current row size : " + rows.size());
			return rows.size();
		} 
		{
			List<WebElement> norow = driver.findElements(By.xpath(String.format(rowXpath, noRecordAttribute)));
			if(!norow.isEmpty()) {
				return 0;
			}
		}
//		System.out.println(tableEntryRow.size());
//		WebElement noRecordRow = driver.findElement(By.xpath("//table/tbody/tr[@data-pc-section='emptymessage']"));	
		return 0;
	}

	/** Checks the is AWB displayed with logo **/
	public boolean isAwbDisplayedWithLogo() {
		wait.waitForAllElementsVisibility(
				By.xpath("//table/tbody/tr/td[@class='awb_no_logistics data-table-header']/div/div"));
		WebElement logisticLogo = awbNoWithLogo.getFirst();
		WebElement awbNumber = awbNoWithLogo.getLast();
		return logisticLogo.isDisplayed() && awbNumber.isDisplayed();
	}

	public String getAwbFromDatatable() {
		try {
		wait.waitForVisibility(awbNoWithLogo.getFirst());
		WebElement firstAWB = awbNoWithLogo.getLast();
		String firstAWBText = firstAWB.getText().trim();
		return firstAWBText;
		} catch(StaleElementReferenceException e) {
			List<WebElement> awbOnTable = driver.findElements(By.xpath("//table/tbody/tr/td[@class='awb_no_logistics data-table-header']/div/div/*"));
			WebElement awb = awbOnTable.getFirst();
			return awb.getText().trim();
		}
//		return awbNoWithLogo.getLast().getText().trim();
//		WebElement awbNumber = awbNoWithLogo.getLast();
//		List<String> awbs = new ArrayList<>();
//		for(WebElement awb : awbNumber) {
//			comm.getElementText(awb);
//			awbs.add()
//		}
	}

	/**
	 * Wait For Loading Order ID displayed on Datatable
	 * 
	 * @return List of WebElement of Order IDs
	 **/
	public List<WebElement> waitForLoadOrderList() {
		return wait.waitForAllElementsVisibility(orderIDText);
	}

	public List<String> getOrderIDList() {
		wait.waitForAllElementsVisibility(orderIDText);
		List<String> orderIDList = new ArrayList<>();
		try {
			for (WebElement id : orderIDText) {
				String text = comm.getElementText(id);
				orderIDList.add(text);
			}
			return orderIDList;
		} catch (Exception e) {
			throw new RuntimeException("Order ID not captured.", e);
		}
	}

	// pending to add to checkpoint for data displayed or not after loading the
	// page.

	/*** Click on Date Filter displayed on Page ***/
	public void clickPageDateFilter() {
		wait.waitForVisibility(pageDateFilter);
		comm.clickButton(pageDateFilter);
	}

	/***
	 * Get List of Date Options
	 * 
	 * @return List of All Date Options in Date Filter displayed on page
	 **/
	public List<String> getDateOptions() {
//		wait.waitForAllElementsVisibility(By.xpath("//div/p"));	//this method throws stale element exeception
		List<WebElement> options = driver.findElements(By.xpath("//div[@role='dialog']//div/div/p"));
		List<String> dateOptions = new ArrayList<>();
		try {
//			for(WebElement option : dateFilterOptions) {
			for (WebElement option : options) {
				String text = option.getText().trim();
				if (!text.isEmpty() && option.isDisplayed())
					dateOptions.add(text);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return dateOptions;
	}

	/*** Get the Selected Date Option in Date Filter **/
	public String getSelectedDateOption() {
//		wait.waitForAllElementsVisibility(dateFilterOptions);
		List<WebElement> options = driver.findElements(By.xpath("//div[@role='dialog']//div/div/p"));
//		wait.waitForAllElementsVisibility(options);
//		for(WebElement option : dateFilterOptions) {
		for (WebElement option : options) {
			String selectedOption = option.getDomAttribute("class");
			if (selectedOption.contains("m-0 text-sm text-black dark:text-white")) {
				return option.getText().trim();
			}
		}
		return "Any Date is not selected.";
	}

	/** Select Date Option on Page **/
	public void selectDateFilterOptionOnPage(String dateOption) {
		List<WebElement> options = driver.findElements(By.xpath("//div[@role='dialog']//div/div/p"));
		for (WebElement option : options) {
			if (option.getText().trim().equalsIgnoreCase(dateOption)) {
				option.click();
				logger.info(dateOption + " is selected.");
			}
		}

	}

	public void selectYearOption(String year) {
		WebElement yearDropdown = wait.waitForElementToBePresent(By.xpath("//select"));
		Select select = new Select(yearDropdown);
		logger.info("Selecting the given year...");
		select.selectByValue(year);
		logger.info("Given Year Selected!");
	}

	/** click on from Month dropdown in Custom Range **/
	public void selectFromMonth(String fromMonth) {
		By fromMonthLocator = (By.xpath("//body/div[@role='dialog']/div[@class='p-overlaypanel-content']"
				+ "/div[@class='block xl:flex items-start justify-between gap-0 h-full']/div[@class='border-l dark:border-l-input-border-color main-dt-picker-div-border']"
				+ "/div[@class='block xl:flex items-start justify-start relative divide-y divide-x-0 lg:divide-x lg:divide-y-0 dark:divide-input-border-color']"
				+ "/div[1]/div[1]/div[1]"));
		wait.waitForElementToBePresent(fromMonthLocator);
		WebElement fromMonthDropdown = driver.findElement(fromMonthLocator);
		fromMonthDropdown.click();
		logger.info("Selecting the given 'from Month'...");

		List<WebElement> months = getCurrentMonths();

		Optional<WebElement> match = months.stream().filter(option -> option.getText().equalsIgnoreCase(fromMonth))
				.findFirst();

		match.ifPresent(WebElement::click);

		if (match.isPresent()) {
			match.get().click();
			logger.info("Month '" + fromMonth + "' Selected successfully!");
		} else {
			throw new RuntimeException("Given month  '" + fromMonth + "' is not present");
		}
	}

	/** click on to Month dropdown in Custom Range **/
	public void selectToMonth(String toMonth) {
		By toMonthLocator = (By.xpath("//body/div[@role='dialog']/div[@class='p-overlaypanel-content']"
				+ "/div[@class='block xl:flex items-start justify-between gap-0 h-full']/div[@class='border-l dark:border-l-input-border-color main-dt-picker-div-border']"
				+ "/div[@class='block xl:flex items-start justify-start relative divide-y divide-x-0 lg:divide-x lg:divide-y-0 dark:divide-input-border-color']"
				+ "/div[2]/div[1]/div[1]"));
		wait.waitForElementToBePresent(toMonthLocator);
		wait.waitForElementClickable(toMonthLocator, 10, 10);
		WebElement toMonthDropdown = driver.findElement(toMonthLocator);
		toMonthDropdown.click();

		// Select to Month
		logger.info("Selecting the given 'To Month'...");

		List<WebElement> months = getCurrentMonths();

		Optional<WebElement> match = months.stream().filter(option -> option.getText().equalsIgnoreCase(toMonth))
				.findFirst();
		match.ifPresent(WebElement::click);

		if (match.isPresent()) {
			wait.waitForElementClickable(toMonthLocator, 10, 10);
			match.get().click();

			logger.info("Month " + toMonth + " Selected successfully!");
		} else {
			throw new RuntimeException("Given month : " + toMonth + " is not present");
		}

	}

	/** Select Day in Date filter **/
	public void selectFromDay(String fromDay) {
		By activeDaysLocator = (By.xpath(
				"//div[@class='p-2 flex-1']/div[@class='dp__main dp__theme_light dp__flex_display left-side-date-picker']"
						+ "//div[contains(@class,'dp__cell_inner dp__pointer')]"));

		List<WebElement> activeDays = driver.findElements(activeDaysLocator);
		try {
			for (WebElement selectDay : activeDays) {
				if (selectDay.getText().trim().equalsIgnoreCase(fromDay)) {
					logger.info("Selecting a day...");
					try {
						wait.waitForElementClickable(activeDaysLocator, 10, 10);
						selectDay.click();

					} catch (StaleElementReferenceException e) {
						driver.findElement(activeDaysLocator);
					}
//					wait.sleep(10);
					logger.info("from Day '" + fromDay + "' selected successfully...");
					break;
				}

			}
		} catch (StaleElementReferenceException e) {
			throw new RuntimeException("Element is Stale.", e);

		}
	}

	public void selectToDay(String toDay) {
		By activeDaysLocator = (By.xpath(
				"//div[@class='p-2 flex-1']/div[@class='dp__main dp__theme_light dp__flex_display right-side-date-picker']"
						+ "//div[contains(@class,'dp__cell_inner dp__pointer')]"));

		List<WebElement> activeDays = driver.findElements(activeDaysLocator);
		try {
			for (WebElement selectDay : activeDays) {
				if (selectDay.getText().trim().equalsIgnoreCase(toDay)) {
					logger.info("Selecting a day...");
					try {
						wait.waitForElementClickable(activeDaysLocator, 10, 10);
						selectDay.click();

					} catch (StaleElementReferenceException e) {
						driver.findElement(activeDaysLocator);
					}
//					wait.sleep(10);
					logger.info("from Day '" + toDay + "' selected successfully...");
					break;
				}

			}
		} catch (StaleElementReferenceException e) {
			throw new RuntimeException("Element is Stale.", e);

		}
	}

	public void clickDateRangeApplyButton() {
		WebElement applyButton = wait
				.waitForVisibility(By.xpath("//div[@class='flex items-center font-interRegular justify-center']"));
		comm.clickButton(applyButton);

	}

	/**
	 * Get the list of String of Month Dropdown
	 *
	 * @return List of String of month Dropdown.
	 */
	public List<String> getCurrentMonthList() {
		List<WebElement> months = driver.findElements(
				By.xpath("//div[@class='p-dropdown-items-wrapper w-[14rem]']/ul[@id='baseDropdown_list']/li/div/div"));
		List<String> actualMonths = new ArrayList<String>();
		for (WebElement month : months) {
			String text = comm.getElementText(month);
			actualMonths.add(text);
		}
		return actualMonths;
	}

	/**
	 * Private method for getting list of WebElements of months.
	 * 
	 * @return List of WebElements of months
	 **/
	private List<WebElement> getCurrentMonths() {
		List<WebElement> months = driver.findElements(
				By.xpath("//div[@class='p-dropdown-items-wrapper w-[14rem]']/ul[@id='baseDropdown_list']/li/div/div"));
		return wait.waitForAllElementsVisibility(months);
	}

	private WebElement getCurrentSelectedFromDay() {
//		By daylocator = By.xpath("//div[@class='dp__calendar_item' and @aria-pressed='true']/div");
		By fromDayLocator = By.xpath("//div[contains(@class,'dp__cell_inner dp__pointer dp--past dp__range_start')]"); // start
																														// day
																														// of
																														// selected
																														// range.
		WebElement fromDay = driver.findElement(fromDayLocator);
		wait.waitForVisibility(fromDay);
		return fromDay;
	}

	private WebElement getCurrentSelectedToDay() {
		By toDaylocator = By.xpath("//div[contains(@class,'dp__cell_inner dp__pointer dp--past dp__range_end')]"); // end
																													// day
																													// of
																													// selected
																													// range
		WebElement toDay = driver.findElement(toDaylocator);
		wait.waitForVisibility(toDay);
		return toDay;
	}

	public void selectCustomRangeDateOption(String fromDateStr, String toDateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy", Locale.ENGLISH);
		LocalDate fromDate = LocalDate.parse(fromDateStr, formatter);
		LocalDate toDate = LocalDate.parse(toDateStr, formatter);
		DateRangeType type = DateUtils.classifyDateRange(fromDate, toDate);

		switch (type) {
		case SAME_MONTH_CURRENT_YEAR:
			selectYearOption("This Year");
			selectRangeInSingleCalendar("to", fromDate, toDate);
			break;
		case SAME_MONTH_PREVIOUS_YEAR:
			selectYearOption(String.valueOf(fromDate.getYear()));
			selectRangeInSingleCalendar("from", fromDate, toDate);
			break;
		case CROSS_MONTH_SAME_YEAR:
			selectYearOption(String.valueOf(fromDate.getYear()));
//				selectFromMonth(fromDate.getMonth().name());
			getCurrentMonthList();
			System.out.println(getCurrentMonthList());
			selectFromMonth(fromDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
			selectFromDay(String.valueOf(fromDate.getDayOfMonth()));
//				selectToMonth(toDate.getMonth().name());
			System.out.println(getCurrentMonthList());
			selectFromMonth(fromDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
			selectToDay(String.valueOf(toDate.getDayOfMonth()));
			break;
		case CROSS_YEAR_WITHIN_ONE_YEAR:
			selectYearOption("Last One Year");
//				selectFromMonth(fromDate.getMonth().name());
			System.out.println(getCurrentMonthList());
			selectFromMonth(fromDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
			selectFromDay(String.valueOf(fromDate.getDayOfMonth()));
//	            selectToMonth(toDate.getMonth().name());
			getCurrentMonthList();
			System.out.println(getCurrentMonthList());
			selectFromMonth(fromDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
			selectToDay(String.valueOf(toDate.getDayOfMonth()));
			break;
		case CROSS_YEAR_OVER_ONE_YEAR:
			throw new RuntimeException("Cannot select range more than 1 year.");
		case FUTURE_DATE:
			throw new RuntimeException("Cannot select future date");
		}

	}

	private void selectRangeInSingleCalendar(String calendarSide, LocalDate fromDate, LocalDate toDate) {
		String fromDay = String.valueOf(fromDate.getDayOfMonth());
		String toDay = String.valueOf(toDate.getDayOfMonth());
		String monthName = fromDate.getMonth().name().substring(0, 1)
				+ fromDate.getMonth().name().substring(1).toLowerCase();

		// Locate calendar side
		By activeDaysLocator;

		if (calendarSide.equalsIgnoreCase("from")) {
			activeDaysLocator = By.xpath(
					"//div[@class='p-2 flex-1']/div[contains(@class,'left-side-date-picker')]//div[contains(@class,'dp__cell_inner dp__pointer')]");
		} else {
			activeDaysLocator = By.xpath(
					"//div[@class='p-2 flex-1']/div[contains(@class,'right-side-date-picker')]//div[contains(@class,'dp__cell_inner dp__pointer')]");
		}

		// Select month (since single calendar)
		logger.info("Selecting month '" + monthName + "' in " + calendarSide + " calendar...");
		if (calendarSide.equalsIgnoreCase("from")) {
			selectFromMonth(monthName);
		} else {
			selectToMonth(monthName);
		}

		// Select range of days
		logger.info("Selecting day range: " + fromDay + " - " + toDay);
		wait.waitForAllElementsVisibility(activeDaysLocator);
		List<WebElement> days = driver.findElements(activeDaysLocator);

		boolean fromFound = false;
		for (WebElement dayElement : days) {
			String text = dayElement.getText().trim();
			if (text.equals(fromDay)) {
				dayElement.click();
				fromFound = true;
				logger.info("From day '" + fromDay + "' selected.");
			} else if (fromFound && text.equals(toDay)) {
				dayElement.click();
				logger.info("To day '" + toDay + "' selected.");
				break;
			}
		}
	}

	public boolean isFilterButtonDisplayed() {
		wait.waitForVisibility(filtersButton);
		return comm.isElementDisplayed(filtersButton);
	}

	public void clickOnFiltersButton() {
		try {
			if (isFilterButtonDisplayed()) {
				wait.sleep(3);
				comm.clickButton(filtersButton);
				
			} else {
				wait.waitForVisibility(By.xpath("//span[@class='filters nest-hub-max-inner-filter']")).click();
			}
		} catch (Exception e) {
			throw new RuntimeException("Filter button is not displayed.", e);
		}
	}

	/** Check Filters Modalbox is opened or not **/
	public boolean isFiltersModalboxOpens() {
		wait.waitForVisibility(By.xpath("//div/p[@class='header']"));
		return comm.isElementDisplayed(allFiltersHeader);
	}

	/**
	 * Gets Filters List displayed at Left side in Filter modalbox. In
	 * this method subfilter names are not captured. eg. Customer Details (captured)
	 * Customer Name, Email, Mobile. (ommitted)
	 * 
	 * @return List of String of Filters displayed at left side.
	 */
	public List<String> getListOfFilters() {
		wait.waitForAllElementsVisibility(By.xpath("//div[@class='w-[168px] p-2 lists hover:cursor-pointer']"));
		List<String> filterList = new ArrayList<>();
		for (WebElement filter : filtersList) {
			String name = comm.getElementText(filter);
			filterList.add(name);
		}
		return filterList;
	}

	/**
	 * This Method return all the Filters which headers are displayed at right side. This method
	 * omitts Main Filter header, if sub filter is present. eg. Customer Details
	 * (ommitted) - Customer Name, Customer Email, Customer Mobile No. (captured).
	 * 
	 * @return List of String of filters displayed at right side.
	 */
	public List<String> getFiltersHeaders() {
		wait.waitForVisibility(By.xpath("//div/span[@class='right-filter-header pl-[14px]']"));
		List<String> filterHeaders = new ArrayList<>();
		for (WebElement filter : allFiltersHeaders) {
			String header = comm.getElementText(filter);
			filterHeaders.add(header);
		}
		return filterHeaders;
	}

	/** Enter Order ID in Order ID Filter **/
	public void setOrderIDFilter(String orderID) {
		wait.waitForVisibility(orderIDFilterInputbox);
		orderIDFilterInputbox.clear();
		comm.setInput(orderIDFilterInputbox, orderID);
	}

	public boolean isAllSearchIconsAreDisplayed() {
		wait.waitForVisibility(By.xpath("//div/div/img[@alt='Search']"));
		for (WebElement icon : allSearchIconsInFilters) {
			return icon.isDisplayed();
		}
		return false;
	}

	public void clickApplyFilterButton() {
		comm.clickButton(applyFilterButton);
		logger.info("Filter Applied successfully.");
		wait.sleep(3);
	}

	/**Enter the filterName and this Method will redirects to that filter in Filter Modal box of presents.
	 * if filter is not present in filter modalbox it throw a @RuntimeException
	 * @param filterName in Filter modalbox.
	 */
	
	public void goToFilter(String filterName) {
//		wait.waitForTextToBePresent(awbNoWithLogo.getFirst(), getAwbFromDatatable());
		waitForLoadOrderList();
		wait.waitForAllElementsVisibility(filtersList);
		try {
			for (WebElement name : filtersList) {
				if (name.getText().equalsIgnoreCase(filterName)) {
					name.click();
					logger.info("'"+filterName + "' filter is Selected!");
				}
//			name.click();
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid filter name.", e);
		}
	}

	public List<String> getShippingAWBOptionsList() {
		wait.waitForClickable(shippingAWBFilterDropdown);
		comm.clickButton(shippingAWBFilterDropdown);
		wait.waitForAllElementsVisibility(logisticsOptionsInShippingFilters);
		List<String> allLogistics = new ArrayList<>();
		for (WebElement logistics : logisticsOptionsInShippingFilters) {
			String name = logistics.getAttribute("aria-label").trim();
			allLogistics.add(name);
		}
		return allLogistics;
	}

	/** Select single logistic from Shippping AWB Filter **/
	public void selectLogisticsInShippingAWBFilter(String logisticsName) {
		wait.waitForVisibility(shippingAWBFilterDropdown);
		comm.clickButton(shippingAWBFilterDropdown);
		String xpath = String.format(
				"//li[@class='p-multiselect-item']//div[contains(text(),'%s')]/preceding-sibling::div", logisticsName);
		WebElement checkbox = wait.waitForVisibility(By.xpath(xpath));
		if (!checkbox.isSelected()) {
			checkbox.click();
		}
		wait.sleep(10);
	}

	public List<String> getSelectedLogistics() { // Need to check this method.
		wait.waitForAllElementsVisibility(logisticsOptionsInShippingFilters);
		List<String> selectedLogistics = new ArrayList<>();

		List<WebElement> logistics = driver.findElements(By.xpath("//div[@class='p-multiselect-items-wrapper']/ul/li"));

		for (WebElement logistic : logistics) {
			wait.waitForAttributeToBe(logistic, "aria-selected", "true");

			String ariaSelected = logistic.getDomAttribute("aria-selected");
			if ("true".equalsIgnoreCase(ariaSelected) && logistic.isSelected()) {
				String selected = comm.getElementText(logistic);
				selectedLogistics.add(selected);
			}
		}
		return selectedLogistics;
	}

//	public void clickDatePickerOnPage() {
//		wait.waitForElementToBePresent(By.xpath("//div[@class='datepicker-outer']"));
//		comm.clickButton(datePickerElement);
//	}

	public void setRiskFilter(String riskType) {
		wait.sleep(3);
		clickOnFiltersButton();
		wait.sleep(3);

		try {
			List<String> filters = getListOfFilters();
			if (!filters.contains("Risk")) {
				throw new RuntimeException("Risk filter is not available on this tab.");
			}
			goToFilter("Risk");
			switch (riskType.trim().toLowerCase()) {
			case "na":
				driver.findElement(By.id("0_order_risk")).click();
				break;
			case "low risk":
				driver.findElement(By.id("1_order_risk")).click();
				break;
			case "medium risk":
				driver.findElement(By.id("2_order_risk")).click();
				break;
			case "high risk":
				driver.findElement(By.id("3_order_risk")).click();
				break;
			default:
				throw new IllegalArgumentException("Given risk type '" + riskType + "' is not present");
			}
			logger.info("Risk filter '" + riskType + "' applied successfully.");

		} catch (NoSuchElementException e) {
			throw new RuntimeException("Risk filter in not displayed in this tab.", e);
		}

	}
	
	/** Get Pagination count on the page. **/
	public String getPaginationCount() {
//		WebElement paginationCount = driver.findElement(By.xpath("//div[@class='p-paginator-left-content']"));
//		String count = paginationCount.getText().trim();
		return getCurrentEntryCount();
//		return count;
	}
	
	private String getCurrentEntryCount() {
		By paginationCountLocator = By.xpath("//div[@class='p-paginator-left-content']");
		if(wait.waitForElementToBePresent(paginationCountLocator).isDisplayed()) {
		WebElement paginationCount = driver.findElement(By.xpath("//div[@class='p-paginator-left-content']"));
//		if(paginationCount.isDisplayed()) {
		String count = paginationCount.getText().trim();
		String[] entries = count.split("of");
		String entryCount = entries[1];
		return entryCount;
		
		} else {
			return "No Entries found.";
		}
	}
	
	public void setAmountFilter(String selectCondition, String...values) {
		boolean isAmountfilterDisplayed =getFiltersHeaders().stream()
		.anyMatch(header -> header.equalsIgnoreCase("Amount"));
		
		if(isAmountfilterDisplayed) {
			comm.clickButton(amountFilterSelectRangeDropdown);
			
			By conditionsOptions = (By.xpath("//div[@id='order_amount']//div/ul/li/div/div"));
			wait.waitForAllElementsVisibility(conditionsOptions);
			
			List<WebElement> conditions = driver.findElements(conditionsOptions)
											.stream().filter(opt -> !opt.getText().trim().isEmpty())
											.collect(Collectors.toList());
		
			Optional<WebElement> match = conditions.stream()
					.filter(option -> option.getText().equalsIgnoreCase(selectCondition))
					.findFirst();
			
			if(match.isEmpty()) {
				logger.warn("'" + selectCondition + "' condition not present in Amount filter.");
		        return;
			}
				match.get().click();
				wait.sleep(1);
						logger.info(" '"+ selectCondition + "'condition is selected!");
				
				switch(selectCondition.toLowerCase()) {
					case "select range":
						if(values.length < 2) {
							throw new IllegalArgumentException("For 'Select Range' condition, must provide both 'Min' and 'Max' values");
						} 
						
						WebElement minInput = driver.findElement(By.xpath("//div[@id='order_amount']//input[@placeholder='Min']"));
						WebElement maxInput = driver.findElement(By.xpath("//div[@id='order_amount']//input[@placeholder='Max']"));
						comm.setInput(minInput, values[0]);
						comm.setInput(maxInput, values[1]);
						logger.info("Min value set to " + values[0]+ " Max value set to "+ values[1]);
						break;
						
					case "greater than" :
					case "less than" :
					case "greater than equal to" : 
					case "less than equal to" : 
						if(values.length < 1) {
							throw new IllegalArgumentException("For '"+ selectCondition+ "' you must provide one value.");
						}
						WebElement valueInput = driver.findElement(By.xpath("//input[@placeholder='Value']"));
						comm.setInput(valueInput, values[0]);
						logger.info("Value set to : "+values[0]);
						break;
						
					default : 
						logger.warn("No matching condition for '" + selectCondition + "'");
				}
		}
//			} else {
//				logger.warn(" '"+ selectCondition + "' condition not present in Amount filter.");
//			}
//		}
//		else {
//			logger.info("Amount filter is not present in this tab.");
//		}
}

	/** Get selected Range type value*/
	public String getCurrentSelectedConditionOfAmountFilter() {
		return amountFilterSelectRangeDropdown.getAttribute("aria-label").trim();
	}
	
	public boolean isSelectRangeConditionSelected() {
		return getCurrentSelectedConditionOfAmountFilter().equalsIgnoreCase("Select Range");
				
	}
	
	public void setMinValue(WebElement minbox, String minValue) {
		minbox = this.minInputbox;
		comm.setInput(minbox, minValue);
	}

	
	public void setMaxValue(WebElement maxbox, String maxValue) {
		maxbox = this.maxInputbox;
		comm.setInput(maxbox, maxValue);
	}
	
	public String getMaxValue() {
		WebElement maxInput = driver.findElement(By.xpath("//div[@id='order_amount']//input[@placeholder='Max']"));
		String enteredMax = maxInput.getDomProperty("value");
		if(enteredMax != null) {
			logger.info("The DOM value of min is : "+ enteredMax);
			return enteredMax;
		} else {
			logger.info("No value captured in DOM");
			return "___";
		}
	}
	
	public String getToastMessageError() {
		List<WebElement> toast = driver.findElements(By.xpath("//div[@class='p-toast-detail' and @data-pc-section='detail']"));
		WebElement toastDetails = toast.getFirst();
		if(toastDetails.isDisplayed()) {
			logger.info("Toast message is displayed.");
			String toastMessage = toastDetails.getText().trim();
			return toastMessage;
		} else {
			logger.warn("Toast message is not displayed!");
			return "";
		}
		
	}
	
	/** Click on saved filter button
	 * @return **/
	public SavedFilters clickOnSavedFilterButton() {
		if(wait.waitForVisibility(savedFilterButton).isDisplayed()) {
			savedFilterButton.click();
			return new SavedFilters(driver);
		} else {
			logger.info("Filter button is not displayed.");
		}
		return null;
		
	}
	
	/** Get the current Applied Filter**/
	public String getCurrentAppliedFilter() {
		WebElement filtername = driver.findElement(By.xpath("//div[@class='filter-dropdown']/span[2]"));
		return filtername.getText().trim();
	}
	
	public List<String> getListOfCurrentFilterLi() {
		List<String> liValue = new ArrayList<>();
		try {
		wait.waitForAllElementsVisibility(filterLiOnDatatable);
		for(WebElement li : filterLiOnDatatable) {
			String liText = li.getText().trim();
			liValue.add(liText);
		}
			return liValue;
		} catch(Exception e) {
			throw new RuntimeException("No Li are displayed.");
		}
		
	}
}

