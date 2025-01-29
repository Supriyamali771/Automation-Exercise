package SeleniumProject.SeleniumProject;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
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

public class EbayAddToCartTest {
    private WebDriver driver;
    private WebDriverWait wait;
//Adding AddToCart
    public EbayAddToCartTest() { }

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testAddItemToCart() {
        driver.get("https://www.ebay.com");

        // Step 3: Search for 'book'
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='text']")));
        searchBox.sendKeys("book");
        searchBox.submit();

        // Step 4: Click on the first book in the search results
        WebElement firstBook = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//li[contains(@class, 's-item s-item')]//div[@class='s-item__info clearfix']//div[@class='s-item__title'])[3]")));
        firstBook.click();

        // Step 4.1: Switch to the new tab (since the first book opens in a new tab/window)
        String originalWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // Step 5: Click on 'Add to Cart'
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[contains(text(),'Add to cart')]")));
        addToCartButton.click();

        // Step 6: Verify cart count is updated
        WebElement cartCount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='gh-cart__icon']")));
        int cartItems = Integer.parseInt(cartCount.getText());

        // Assertion to verify item was added
        Assert.assertTrue(cartItems > 0, "Cart is not updated!");

        System.out.println("✅ Test Passed: Item successfully added to cart!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
