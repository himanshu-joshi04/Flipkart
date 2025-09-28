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
import java.util.List;

public class ProductSearch {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
     
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.flipkart.com/");

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

   
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

        // Wait for search results
        List<WebElement> results = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("div._1AtVbE div._4rR01T")));

        Assert.assertTrue(results.size() > 0, "No search results found for valid product.");
    }

    @Test(priority = 2)
    public void searchInvalidProduct() {
        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
        searchBox.clear(); 
        searchBox.sendKeys("asdasdasdasd1234");
        searchBox.sendKeys(Keys.ENTER);

        WebElement noResults = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(text(),'No results found') or contains(text(),'Sorry, no results found')]")));

        Assert.assertTrue(noResults.isDisplayed(), "No results message not displayed for invalid product.");
    }

    @Test(priority = 3)
    public void searchPartialProduct() {
        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
        searchBox.clear();
        searchBox.sendKeys("iPh");

        // Wait for autocomplete suggestions
        List<WebElement> suggestions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("ul._1wXgwG li")));

        Assert.assertTrue(suggestions.size() > 0, "No suggestions found for partial product.");
    }

    @Test(priority = 4)
    public void searchEmptyProduct() {
        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
        searchBox.clear();
        searchBox.sendKeys(Keys.ENTER);  // Empty search

        // URL check
        String currentURL = driver.getCurrentUrl();
        Assert.assertEquals(currentURL, "https://www.flipkart.com/", "Empty search behavior changed.");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
