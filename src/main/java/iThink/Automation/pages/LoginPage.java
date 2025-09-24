package iThink.Automation.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import iThink.Automation.base.BasePage;
import iThink.Automation.utils.CommonActions;
import iThink.Automation.utils.WaitUtils;

public class LoginPage {
	
	private WebDriver driver;
	private WaitUtils wait;
	private CommonActions comm;
	private static final Logger logger = LogManager.getLogger(LoginPage.class);
	
	public LoginPage(WebDriver driver) {	
		this.driver = driver;
		this.wait = new WaitUtils(driver);
		this.comm = new CommonActions(driver);
		PageFactory.initElements(driver, this);
		
	}
	
	@FindBy(id = "login_username")
	private WebElement usernameInputbox;
	
	@FindBy(id = "password")
	private WebElement passwordInputbox;
	
	@FindBy(xpath = "//button[@class='submit-button']/div")
	private WebElement loginButton;
	
	@FindBy(xpath = "//a[contains(@href, 'forgot')]/div")
	private WebElement forgotPasswordLink;
	
	@FindBy(xpath = "//a[contains(@href, 'signup')]")
	private WebElement signUpLink;
	
	@FindBy(xpath = "//div[@id='username_error']/ul/li")
	private WebElement usernameFieldError;
	
	@FindBy(xpath = "//div[@id='password_error']/ul/li")
	private WebElement passwordFieldError;
	
	@FindBy(xpath = "//div[@class='jquery-notify-bar error top']/span[@class='notify-bar-text-wrapper']")
	private WebElement invalidCredsNotifyError;
	
	@FindBy(xpath = "//div[@class='d-inline-block gray-800']")
	private WebElement textWelcome;
	
	public String getLoginPageTitle() {
		return comm.getCurrentPageTitle();
	}
	
	public void load() {
		logger.info("Waiting for LoginPage to load");
		wait.waitForPageLoads(10);
		wait.waitForVisibility(By.xpath("//button[@class='submit-button']/div"));
		logger.info("LoginPage loads successfully...");		
	}
	
	public void setUsername(String email) {
		comm.setInput(usernameInputbox, email);
	}
	
	public void setPassword(String password) {
		comm.setInput(passwordInputbox, password);
	}
	
	public Dashboard clickLoginButton() {
		comm.clickButton(loginButton);
		return new Dashboard(driver);
	}
	
	public LoginPage fillLogin(String email, String password) {
		setUsername(email);
		setPassword(password);
		clickLoginButton();
		return this;

	}
	
	public void clickForgotPassLink() {
		comm.clickButton(forgotPasswordLink);
	}
	
	public void clickSignupLink() {
		comm.clickButton(signUpLink);
	}
	
	public String getUsernameFieldError() {
		return comm.getElementText(usernameFieldError);
	}
	
	public String getPasswordFieldError() {
		return comm.getElementText(passwordFieldError);
	}
	
	public String getInvalidCredsNotifyError() {
		wait.waitForVisibility(invalidCredsNotifyError);
		return comm.getElementText(invalidCredsNotifyError);
	}
	
	public boolean isLoginSucessful() {
		WebElement welcome = wait.waitForVisibility(By.xpath("//div[@class='d-inline-block gray-800']"));
		return (wait.waitForTitleToBe("ITL - Itl Dashboard") && welcome.isDisplayed());
		
	}
	
	public Dashboard goToDashboard() {
		return new Dashboard(driver);
	}

}
