import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by dmanzelmann on 3/13/2015.
 */
public class MediasiteSched {
    Scanner input;
    WebDriver driver;
    WebElement username;
    WebElement password;
    WebElement login;
    WebElement schoolOfPharmacy;
    WebElement pharmD;
    WebDriverWait wait;

    public MediasiteSched(List<Listing> listings) {
        for (Listing l : listings)
            System.out.println(l);

        input = new Scanner(System.in);
        // weird issue
        // http://stackoverflow.com/questions/7615645/ssl-handshake-alert-unrecognized-name-error-since-upgrade-to-java-1-7-0
        System.setProperty("jsse.enableSNIExtension", "false");
        driver = new FirefoxDriver();
        driver.get("https://mediasite.umaryland.edu/mediasite/manage/");

        username = driver.findElement(By.id("UserName"));
        System.out.println("Enter username: ");
        username.sendKeys(input.next());

        password = driver.findElement(By.id("Password"));
        System.out.println("Enter password: ");
        password.sendKeys(input.next());

        login = driver.findElement(By.tagName("input"));
        login.submit();

        wait = new WebDriverWait(driver, 30);

        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("School of Pharmacy")));
        schoolOfPharmacy = driver.findElement(By.partialLinkText("School of Pharmacy"));
        schoolOfPharmacy.click();

        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("PharmD")));
        pharmD = driver.findElement(By.partialLinkText("PharmD"));
        pharmD.click();
    }

    public static void main(String[] args) {
        //MediasiteSched mediasiteSched = new MediasiteSched();
    }
}
