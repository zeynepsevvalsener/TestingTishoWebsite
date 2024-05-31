import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class LoginTests {
    private final String wrongInput =
            "E-mail veya şifre bilgileriniz hatalı.";
    private final String correctInput =
            "Başarıyla giriş yaptınız.";
    private final String accountMail = "jnk65264@fosiq.com";
    private final String accountPassword = "MW2gmnTaTH:h:5k!";
    private final String space = " ";
    private static WebDriver driver;
    private static Map<String, Object> vars;
    private static JavascriptExecutor js;

    @BeforeAll
    public static void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();

        WebElement element = null;
        driver.get("https://tisho.com/");
        driver.manage().window().setSize(new Dimension(1440, 900));

        WebElement wheel = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div/div[1]"));
        if (wheel != null && wheel.isDisplayed()) {
            wheel.click();
        } else {
            System.out.println("Wheel did not appear.");
        }

        driver.findElement(By.linkText("Giriş")).click();
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }

    @ParameterizedTest
    @CsvSource({

            "aaa@gmail.com, 12345678," + wrongInput,
            "aa,12345678," + wrongInput,
            "Madonna@wbr.com, aa," + wrongInput,
            "jnk65264@fosiq.com, MW2gmnTaTH:h:5k!," + correctInput
    })
    public void loginTest(String email, String password, String expectedOutcome) {

        driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div/div/div/div/div/div/div/div[2]/div[1]/div/ul/li/div/span")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div/div/div/div/div/div/div/div[2]/div[1]/div/ul/li/div/input")).sendKeys(email);

        driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div/div/div/div/div/div/div/div[2]/div[2]/div/span")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div/div/div/div/div/div/div/div[2]/div[2]/div/input")).sendKeys(password);
        driver.findElement(By.id("member-login-btn")).click();

        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div[1]/div/div/div/div/div/div/div/div/div/div/div[2]/div[1]/div/ul/li/div/span[2]")));
        }

        assertThat(driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/div/div/div/div/div/div/div/div/div[2]/div[1]/div/ul/li/div/span[2]")).getText(), is(expectedOutcome));
        driver.navigate().refresh();
        MyUtils.sleepFor(3);
    }


}
