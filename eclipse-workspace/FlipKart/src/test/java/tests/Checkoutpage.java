package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Set;
import java.util.List;

public class Checkoutpage {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.flipkart.com/");

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Close login popup
        try {
            WebElement closePopup = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button._2KpZ6l._2doB4z")));
            closePopup.click();
        } catch (Exception e) {
            System.out.println("Login popup not displayed, continuing test.");
        }
    }

    @Test(priority = 1)
    public void searchValidProduct() {
        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
        searchBox.clear();
        searchBox.sendKeys("iPhone 15");
        searchBox.sendKeys(Keys.ENTER);

        List<WebElement> results = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("div._1AtVbE div._4rR01T")));

        Assert.assertTrue(results.size() > 0, "No search results found for valid product.");
    }

    @Test(priority = 2)
    public void buyNowAndCheckout() throws InterruptedException {
        // Click on first product
        WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("div._1AtVbE div._4rR01T")));
        firstProduct.click();

        // Switch to new tab
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            driver.switchTo().window(handle);
        }

        // Click Buy Now
        WebElement buyNowBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'BUY NOW')]")));
        buyNowBtn.click();

        // Login page appears
        WebElement phoneInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input._2IX_2-._35mXW5")));
        WebElement continueBtn = driver.findElement(By.cssSelector("button._2KpZ6l._2HKlqd._3AWRsL"));

        phoneInput.sendKeys("your_phone_number"); // Enter your phone number
        continueBtn.click();

        // Wait for manual OTP entry
        System.out.println("Enter OTP manually within 30 seconds...");
        Thread.sleep(30000); // Wait for OTP

        try {
            WebElement verifyBtn = driver.findElement(By.cssSelector("button._2KpZ6l._2HKlqd._3AWRsL"));
            verifyBtn.click();
        } catch (Exception ignored) {}

        // Wait for checkout page
        WebElement checkout = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(),'Select delivery address')]")));

        Assert.assertTrue(checkout.isDisplayed(), "Checkout page not loaded");
        System.out.println("Reached Checkout page successfully");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) driver.quit();
    }
}
