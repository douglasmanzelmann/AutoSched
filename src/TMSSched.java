import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.DriverManager;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by dmanzelmann on 4/20/2015.
 */
public class TMSSched {
    WebDriver driver;
    WebDriverWait wait;

    public TMSSched(WebDriver driver) {
        //this.driver = driver;
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void login(String userName, String password) {
        driver.get("http://" + userName + ":" + password + "@" + "tms.rx.umaryland.edu/tms/default.aspx?pageId=116");
        driver.switchTo().activeElement().click();
    }

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "\\\\private\\Home\\Desktop\\chromedriver.exe");
        TMSSched test = new TMSSched(new ChromeDriver());
        test.login("dmanzelmann", "TK421@c3po");
    }
}
