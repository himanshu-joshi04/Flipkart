package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    WebDriver driver;

    // Locators
    By usernameField = By.xpath("//input[@class='r4vIwl BV+Dqf']"); // Email/Mobile field
    By requestOtpBtn = By.xpath("//button[normalize-space()='Request OTP']"); // Request OTP button
    By otpField = By.xpath("//input[@placeholder='Enter OTP']"); // OTP input field
    By loginBtn = By.xpath("//button[normalize-space()='Verify']"); // Verify button

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public void enterUsername(String username) {
        driver.findElement(usernameField).clear();
        driver.findElement(usernameField).sendKeys(username);
    }

    public void clickRequestOtp() {
        driver.findElement(requestOtpBtn).click();
    }

    public void enterOtp(String otp) {
        driver.findElement(otpField).clear();
        driver.findElement(otpField).sendKeys(otp);
    }

    public void clickVerify() {
        driver.findElement(loginBtn).click();
    }
}
