package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.BaseTest;

public class LoginTest extends BaseTest {

    @Test(priority = 1)
    public void validLoginTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername("9782017696");   // valid mobile
        loginPage.clickRequestOtp();

        // ⚡ For demo, assume fixed OTP = 123456
        loginPage.enterOtp("123456");
        loginPage.clickVerify();

        Assert.assertTrue(driver.getTitle().contains("Online Shopping"),
                "❌ Login failed with valid OTP!");
    }

    @Test(priority = 2)
    public void invalidOtpTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername("9782017696");   // valid mobile
        loginPage.clickRequestOtp();

        loginPage.enterOtp("999999"); // wrong OTP
        loginPage.clickVerify();

		/*
		 * String error = loginPage.getErrorMessage();
		 * Assert.assertTrue(error.contains("invalid") || error.contains("incorrect"),
		 * "❌ Invalid OTP not handled properly!");
		 */
    }

    @Test(priority = 3)
    public void blankUsernameTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername("");   // blank
        loginPage.clickRequestOtp();

		/*
		 * String error = loginPage.getErrorMessage();
		 * Assert.assertTrue(error.contains("valid Email ID/Mobile number"),
		 * "❌ Blank username validation missing!");
		 */}

    @Test(priority = 4)
    public void blankOtpTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername("9782017696");
        loginPage.clickRequestOtp();

        loginPage.enterOtp(""); // blank OTP
        loginPage.clickVerify(); }

      /*  String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("OTP"),
                "❌ Blank OTP validation missing!");
    }*/

    @Test(priority = 5)
    public void blankBothFieldsTest() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername("");   // blank username
        loginPage.clickRequestOtp();

        // Since no username, OTP flow shouldn't even start
		/*
		 * String error = loginPage.getErrorMessage();
		 * Assert.assertTrue(error.contains("valid Email ID") ||
		 * error.contains("Mobile"), "❌ Both blank fields validation missing!");
		 */
    }
}
