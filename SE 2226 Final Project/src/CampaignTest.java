import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class CampaignTest {
    private static WebDriver driver;
    private static JavascriptExecutor js;
    private static Map<String, Object> vars;
    private WebElement element;

    @BeforeAll
    public static void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
        driver.manage().window().setSize(new Dimension(1440, 900));
        driver.get("https://tisho.com/");

        WebElement wheel = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div[1]"));
        if (wheel != null && wheel.isDisplayed()) {
            wheel.click();
        } else {
            System.out.println("Wheel did not appear.");
        }

        driver.get("https://www.tisho.com/uye-girisi-sayfasi");
        driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div/div/div/div/div/div/div/div[2]/div[1]/div/ul/li/div/span")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div/div/div/div/div/div/div/div[2]/div[1]/div/ul/li/div/input")).sendKeys("jnk65264@fosiq.com");

        driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div/div/div/div/div/div/div/div[2]/div[2]/div/span")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div/div/div/div/div/div/div/div[2]/div[2]/div/input")).sendKeys("MW2gmnTaTH:h:5k!");
        driver.findElement(By.id("member-login-btn")).click();
        driver.get("https://tisho.com/");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
        @Test
    public void campaignFreeShipping(){
        String priceString;
        double priceDouble = 0;
        String shippingFee;
            driver.findElement(By.id("live-search")).click();
            driver.findElement(By.id("live-search")).sendKeys("yastık");
            driver.findElement(By.id("live-search")).sendKeys(Keys.ENTER);
            driver.findElement(By.cssSelector("#\\31 003-product-detail-asik-kuslar-siyah-kare-yastik .active")).click();
            driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div/div/div[2]/div[1]/div/div/div/div[2]/div[4]/div/div/div/div/div[2]/div/a")).click();

            {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[5]/div/div[2]/div[3]/button[2]")));
            }

            driver.findElement(By.xpath("/html/body/div[5]/div/div[2]/div[3]/button[2]")).click();

            driver.findElement(By.id("Adet0")).click();
            driver.findElement(By.id("Adet0")).sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
            driver.findElement(By.id("Adet0")).sendKeys("5");
            driver.findElement(By.id("Adet0")).sendKeys(Keys.ENTER);

            priceString = driver.findElement(By.cssSelector(".mb > .a-right")).getText();
            {
                WebElement element = driver.findElement(By.id("addCartBtn"));
                Actions builder = new Actions(driver);
                builder.moveToElement(element).perform();
            }
            driver.findElement(By.id("addCartBtn")).click();
            shippingFee = driver.findElement(By.id("priceCargo")).getText();
            priceDouble =
                    Double.parseDouble(priceString.split(" ")[0].replace(".","").replace(",","."));
            assertTrue( priceDouble > 1000.00);
            assertEquals("0,00",shippingFee);
            driver.get("https://www.tisho.com/sepet");
            MyUtils.sleepFor(2);
            driver.findElement(By.linkText("Sepeti Temizle")).click();
            MyUtils.sleepFor(4);
        }
}
