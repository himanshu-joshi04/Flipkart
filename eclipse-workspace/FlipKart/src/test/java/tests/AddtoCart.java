package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AddtoCart {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.manage().window().maximize();
        driver.get("https://www.flipkart.com");
    }

    @Test
    public void addAndRemoveProductFromCart() {
        try {
            // 1. Close login popup if displayed
            try {
                WebElement closeLoginPopup = wait.until(
                        ExpectedConditions.elementToBeClickable(By.cssSelector("button._30XB9F"))
                );
                closeLoginPopup.click();
                System.out.println("Login popup closed.");
            } catch (Exception e) {
                System.out.println("Login popup not displayed.");
            }

            // 2. Search product
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
            searchBox.sendKeys("iphone 15");
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            // 3. Click first product (stable locator)
            WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//a[@class='CGtC98'])[1]")));
            firstProduct.click();

            // 4. Handle window/tab
            List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            if (tabs.size() > 1) {
                driver.switchTo().window(tabs.get(1));
            } else {
                driver.switchTo().window(tabs.get(0));
            }

            // 5. Add to Cart
            WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Add to cart']")));
            addToCartBtn.click();
            System.out.println("Product added to cart.");

            // 6. Go to Cart
            WebElement cartBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@href='/viewcart?exploreMode=true&preference=FLIPKART']")));
            cartBtn.click();

            // 7. Remove from Cart
            WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[text()='Remove']")));
            removeBtn.click();

            WebElement confirmRemoveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[text()='Remove Item']")));
            confirmRemoveBtn.click();

            // 8. Assert cart empty
            WebElement emptyMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(text(),'Your cart is empty')]")));
            Assert.assertTrue(emptyMsg.isDisplayed(), "Cart is not empty after removing product!");
            System.out.println("Product removed successfully. Cart is empty.");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
