package iThink.Automation.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

//import iThink.Automation.base.BasePage;
import iThink.Automation.base.BaseTest;
import iThink.Automation.pages.Dashboard;
import iThink.Automation.pages.LoginPage;
import iThink.Automation.utils.ExcelUtils;

public class LoginPageTests extends BaseTest{
	
	@DataProvider(name = "loginData")
	public Object[][] getLoginData() {
		return ExcelUtils.getTestData("Login");
	}
	
	@Test(dataProvider = "loginData")
	public void testLogin(String testCase, String username, String password, String usernameError, String passwordError, String expectedResult) {
		LoginPage loginpage = new LoginPage(driver);
		loginpage.fillLogin(username, password);
		
		if(expectedResult.contains("Successful")) {
			//Validate Success
			Assert.assertTrue(loginpage.isLoginSucessful(), "Dashboard Not Displayed. Test : " + testCase+ " - failed!");
			Dashboard dashboard = loginpage.goToDashboard();
			Assert.assertTrue(dashboard.isWelcomeDisplayed());
		}
		else if (expectedResult.contains("Invalid Login")) {
			//Validate Notify Error message
			Assert.assertEquals(loginpage.getInvalidCredsNotifyError(), "Invalid Login Credentials.", "Test : "+testCase+ " - failed!");
		}
		else if (expectedResult.contains("required.")) {
			//Validate Field Errors
			if(!usernameError.isEmpty()) {
				Assert.assertEquals(loginpage.getUsernameFieldError(), usernameError, "Test : "+ testCase + " failed!");
			}	
			if(!passwordError.isEmpty()) {
				Assert.assertEquals(loginpage.getPasswordFieldError(), passwordError, "Test : "+testCase + " failed!");
			}
		}
		else if (expectedResult.contains("valid email.")) {
			//Validate Email validations
			Assert.assertEquals(loginpage.getUsernameFieldError(), usernameError, "Test : "+testCase+ " failed!");
		}
		else if (expectedResult.contains("characters")) {
			//Validate Password validations
			Assert.assertEquals(loginpage.getPasswordFieldError(), passwordError, "Test : "+testCase+ " failed!");
		}
		
		
	
	}
	
	}
