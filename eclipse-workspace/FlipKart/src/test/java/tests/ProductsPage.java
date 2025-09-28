package tests;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import java.util.List;

public class ProductsPage {
    public WebDriver driver;

    @BeforeMethod
    public void setup() {
        
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.flipkart.com/");

        // Close login popup if displayed
        try {
            WebElement closeBtn = driver.findElement(By.cssSelector("button._2KpZ6l._2doB4z"));
            if (closeBtn.isDisplayed()) {
                closeBtn.click();
            }
        } catch (Exception e) {
            // Popup not displayed, ignore
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // TC001 - Verify Products Displayed
    @Test(priority = 1)
    public void verifyProductsDisplayed() {
        driver.findElement(By.name("q")).sendKeys("Mobiles");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        List<WebElement> products = driver.findElements(By.cssSelector("div._2kHMtA"));
        Assert.assertTrue(products.size() > 0, "Products not displayed!");
    }

    // TC002 - Product Navigation
    @Test(priority = 2)
    public void verifyProductNavigation() {
        driver.findElement(By.name("q")).sendKeys("Laptop");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement firstProduct = driver.findElement(By.cssSelector("div._2kHMtA"));
        String productName = firstProduct.getText();
        firstProduct.click();

        // Switch to new tab
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
        }

        String productTitle = driver.findElement(By.cssSelector("span.B_NuCI")).getText();
        Assert.assertTrue(productTitle.contains(productName.split(" ")[0]), "Navigation failed!");
    }

    // TC003 - Verify Sorting (Low to High)
    @Test(priority = 3)
    public void verifySortingLowToHigh() {
        driver.findElement(By.name("q")).sendKeys("TV");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.xpath("//div[text()='Price -- Low to High']")).click();

        List<WebElement> prices = driver.findElements(By.cssSelector("div._30jeq3._1_WHN1"));
        int firstPrice = Integer.parseInt(prices.get(0).getText().replace("₹", "").replace(",", ""));
        int lastPrice = Integer.parseInt(prices.get(prices.size() - 1).getText().replace("₹", "").replace(",", ""));
        Assert.assertTrue(firstPrice <= lastPrice, "Sorting not working!");
    }

    // TC004 - Verify Filters
    @Test(priority = 4)
    public void verifyFilters() {
        driver.findElement(By.name("q")).sendKeys("Charger");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        driver.findElement(By.xpath("//div[text()='SAMSUNG']")).click();

        List<WebElement> titles = driver.findElements(By.cssSelector("div._4rR01T"));
        for (WebElement t : titles) {
            Assert.assertTrue(t.getText().toUpperCase().contains("SAMSUNG"), "Filter not applied properly!");
        }
    }

    // TC005 - Add to Cart
    @Test(priority = 5)
    public void verifyAddToCart() {
        driver.findElement(By.name("q")).sendKeys("Mobiles");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement firstProduct = driver.findElement(By.cssSelector("div._2kHMtA"));
        firstProduct.click();

        // Switch to new tab
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
        }

        driver.findElement(By.xpath("//button[text()='Add to cart']")).click();

        WebElement cartItem = driver.findElement(By.cssSelector("a._2Kn22P.gBNbID"));
        Assert.assertTrue(cartItem.isDisplayed(), "Product not added to cart!");
    }

  
}
