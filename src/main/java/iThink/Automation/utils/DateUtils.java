package iThink.Automation.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import iThink.Automation.modules.forwardOrders.AllTab;

public class DateUtils {
	
	private WebDriver driver;
	private WaitUtils wait;
	private static final Logger logger = LogManager.getLogger(DateUtils.class);
	
	public DateUtils(WebDriver driver) {
		this.driver = driver;
		this.wait = new WaitUtils(driver, 10);
	}

	

	// Parse InputString 'dd-MMM-yyyy' to LocalDate[]
//	public static LocalDate[] parseDateRange(String rangeInput) {
//		String[] parts = rangeInput.split(":");
//		
//		if(parts.length != 2) {
//			throw new IllegalArgumentException("Invalid date range. Expected format: dd-MMM-yyyy:dd-MMM-yyyy");
//		}
//		
//		LocalDate fromDate = LocalDate.parse(parts[0], FORMATTER);
//		LocalDate toDate = LocalDate.parse(parts[1], FORMATTER);
//		
//		return new LocalDate[] {fromDate, toDate};
//	}
//	
//	//Map LocalDate to This year or Last One Year
//	public void resolveYearOption(LocalDate fromDate, LocalDate toDate) {
//		LocalDate today = LocalDate.now();
//		LocalDate startOfThisYear = LocalDate.of(today.getYear(), 1, 1);
//		LocalDate oneYearAgo = today.minusYears(1).plusDays(1);
//		
//		if(!fromDate.isBefore(today) && !toDate.isAfter(today)) {
//			
//		}
//	}
	
//	public void selectDaysInSingleCalendar(LocalDate fromDate, LocalDate toDate, boolean leftCalendar) {
//	    String calendarXpath = leftCalendar ?
//	        "//div[@class='dp__main dp__flex_display left-side-date-picker']//div[contains(@class,'dp__cell_inner dp__pointer')]" :
//	        "//div[@class='dp__main dp__flex_display right-side-date-picker']//div[contains(@class,'dp__cell_inner dp__pointer')]";
//
//	    List<WebElement> activeDays = driver.findElements(By.xpath(calendarXpath));
//
//	    String fromDay = String.valueOf(fromDate.getDayOfMonth());
//	    String toDay = String.valueOf(toDate.getDayOfMonth());
//
//	    for (WebElement day : activeDays) {
//	        String text = day.getText().trim();
//	        if (text.equals(fromDay) || text.equals(toDay)) {
//	            try {
//	                wait.waitForElementClickable(By.xpath(calendarXpath), 10, 10);
//	                day.click();
//	                logger.info("Day '" + text + "' selected successfully on " + (leftCalendar ? "FROM" : "TO") + " calendar");
//	            } catch (StaleElementReferenceException e) {
//	                driver.findElement(By.xpath(calendarXpath));
//	            }
//	        }
//	    }
//	}


	public static DateRangeType classifyDateRange(LocalDate fromDate, LocalDate toDate) {
		LocalDate today = LocalDate.now();

		if (fromDate.isAfter(today)) {
			return DateRangeType.FUTURE_DATE;
		}

		long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate);
		if (daysBetween > 365) {
			return DateRangeType.CROSS_YEAR_OVER_ONE_YEAR;
		}

		if (fromDate.getYear() == toDate.getYear()) {
			if (fromDate.getMonth() == toDate.getMonth()) {
				if (fromDate.getYear() == today.getYear())
					return DateRangeType.SAME_MONTH_CURRENT_YEAR;
				else
					return DateRangeType.SAME_MONTH_PREVIOUS_YEAR;
			} else {
				return DateRangeType.CROSS_MONTH_SAME_YEAR;
			}

		} else {
			return DateRangeType.CROSS_YEAR_WITHIN_ONE_YEAR;
		}
	}
}
