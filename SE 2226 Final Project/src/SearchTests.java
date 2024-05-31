import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.Math;
import java.time.Duration;
import java.util.*;

public class SearchTests {
    private static WebDriver driver;
    private static JavascriptExecutor js;
    private static Map<String, Object> vars;
    private WebElement element;
    private final String firstProductXPath = "//div[2]/div/div[2]/div/div/div/div";
    private final int waitTime = 7;

    @BeforeAll
    public static void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
        driver.get("https://tisho.com/");
        driver.manage().window().setSize(new Dimension(1440, 900));

        MyUtils.sleepFor(5);
        WebElement wheel = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div[1]"));
        if (wheel != null && wheel.isDisplayed()) {
            wheel.click();
        } else {
            System.out.println("Wheel did not appear.");
        }
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void searchNoItems() {
        driver.findElement(By.id("live-search")).click();
        driver.findElement(By.id("live-search")).sendKeys("kedi");
        driver.findElement(By.id("live-search")).sendKeys(Keys.ENTER);
        vars.put("pathSaved", driver.findElements(By.xpath(firstProductXPath)).size());
        vars.put("elementSaved", "class=\"col col-12 mb\"");
        driver.findElement(By.id("live-search")).click();
        driver.findElement(By.id("live-search")).sendKeys("xxxxxxxxxxxxxx");
        driver.findElement(By.id("live-search")).sendKeys(Keys.ENTER);
        {
            List<WebElement> elements = driver.findElements(By.id("pathSaved"));
            assert (elements.size() == 0);
        }
        driver.get("https://tisho.com/");
    }

    @ParameterizedTest
    @CsvSource({
            "Anne", "Kedi", "Göktürk", "Limonata", "Çiçek", "Kupa"
    })
    public void searchMatchWithName(String key) {
        String productName = "";
        MyUtils.sleepFor(3);
        element = driver.findElement(By.id("live-search"));
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);

        element.sendKeys(key);
        element.sendKeys(Keys.ENTER);
        driver.findElement(By.xpath("//span/img")).click();
        productName = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div/div/div[2]/div[1]/div/div/div/div[2]/div[1]/div/div[1]/h1")).getText();
        assertTrue(productName.contains(key));
        driver.get("https://tisho.com/");
    }

    @ParameterizedTest
    @CsvSource({"Lila-Siyah ,686",
            "Sarı ,544",
            "Turkuaz ,559",
            "Pembe ,548",
            "Kırmızı ,546",
            "Beyaz/Kırmızı ,553",
            "Gri ,551",
            "Fuşya ,567",
            "Sarı/Siyah ,565",
            "Siyah/Beyaz ,581",
            "Fıstık Yeşili ,573",
            "Kırmızı/Sarı ,582",
            "Füme/Siyah ,583",
            "Krem Kahve,685",
            "Haki Yeşili ,555",
            "Turuncu ,564",
            "Mor ,554",
            "Koyu Mor,563",
            "Buz Mavisi,552",
            "Lila,562",
            "Lacivert ,549",
            "Krem,561",
            "Gri/Füme ,547",
            "Yavru Ağzı ,560",
            "Siyah ,545",
            "Bordo ,557",
            "Camel,556",
            "Beyaz ,199",
            "Füme ,347",
            "Çimen Yeşili ,346",
            "Beyaz ,336"})
    public void searchMatchWithFilter(String colourName, String colourCode) {
        String productName = "tişört";
        productName = MyUtils.makeLinkFriendly(productName);
        driver.get("https://www.tisho.com/arama?q=" + productName + "&type1=" + colourCode);
        MyUtils.sleepFor(3);
        driver.findElement(By.xpath(firstProductXPath)).click();
        assertEquals(colourName, driver.findElement(By.cssSelector(".secilenOne")).getText());
    }

    @ParameterizedTest
    @CsvSource({
            "Kupa",
            "Tişört",
            "Sweatshirt",
            "Yastık",
            "Eşofman Altı",
            "Şort",
            "Çanta"})
    public void searchMatchWithCategory(String productType){
        driver.findElement(By.id("live-search")).click();
        driver.findElement(By.id("live-search")).sendKeys(productType);
        driver.findElement(By.id("live-search")).sendKeys(Keys.ENTER);
        driver.findElement(By.xpath(firstProductXPath)).click();
        MyUtils.sleepFor(2);

        try {
            element = driver.findElement(By.cssSelector("h1 > strong"));
        } catch (NoSuchElementException e) {
            element = driver.findElement(By.cssSelector("h1:nth-child(1)"));
        }

        assertTrue(element.getText().contains(productType));

        driver.findElement(By.id("live-search")).click();
        driver.findElement(By.id("live-search")).sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
    }

    @ParameterizedTest
    @CsvSource({
            "Sevgili Kombinleri, 2",
            "Anne-Kız Kombinleri, 2",
            "Anne-Erkek Çocuk Kombinleri, 2",
            "Baba-Kız Kombinleri, 2",
            "Üçlü Aile Kombinler, 3"
    })
    public void searchMatchComboItemAmount(String comboName, String amount){
        double total = 0;
        driver.get("https://www.tisho.com/");
        {
            WebElement element = driver.findElement(By.linkText("Kombin Ürünler"));
            Actions builder = new Actions(driver);
            builder.moveToElement(element).perform();
            MyUtils.sleepFor(1);
        }
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(comboName)));
        }
        driver.findElement(By.linkText(comboName)).click();

        MyUtils.sleepFor(1);
        driver.findElement(By.cssSelector(".box:nth-child(1) .ndImage")).click();
        driver.findElement(By.xpath("//*[@id=\"pageContent\"]/div/div/div[3]/a")).click();
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[3]/div/div[2]/div[3]/button[2]")));
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[3]/div/div[2]/div[3]/button[2]")));
        }
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/div[3]/button[2]")).click();
        driver.findElement(By.cssSelector(".search-placeholder")).click();

        total += (
                Double.parseDouble(driver.findElement(By.cssSelector(".box:nth-child(2) .fr > .fl")).getText().split(" ")[0].replace(",","."))
                + Double.parseDouble(driver.findElement(By.cssSelector(".box:nth-child(3) > .fl > .fr > .fl")).getText().split(" ")[0].replace(",","."))
            );
        if (amount.equals("3")){
            total +=
                    Double.parseDouble(driver.findElement(By.cssSelector(".box:nth-child(4) .fr > .fl")).getText().split(" ")[0].replace(",","."));
        }
        double actualPrice =
                Double.parseDouble(driver.findElement(By.cssSelector(".mb > .a-right")).getText().split(" ")[0].replace(".","").replace(",","."));

        actualPrice *= 10;
        actualPrice = Math.floor(actualPrice);
        actualPrice /= 10;

        assertEquals(total ,actualPrice);
        MyUtils.sleepFor(2);
        driver.findElement(By.linkText("Sepeti Temizle")).click();
        MyUtils.sleepFor(4);

    }
}
