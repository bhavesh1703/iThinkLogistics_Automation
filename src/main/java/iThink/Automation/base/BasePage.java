package iThink.Automation.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import iThink.Automation.utils.CommonActions;
import iThink.Automation.utils.ConfigReader;
import iThink.Automation.utils.DriverFactory;
import iThink.Automation.utils.WaitUtils;

public abstract class BasePage {	
	
	protected WebDriver driver;
	protected WaitUtils wait;
	protected CommonActions comm;
	
	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WaitUtils(driver);
		this.comm = new CommonActions(driver);
		PageFactory.initElements(driver, this);
	}

}
