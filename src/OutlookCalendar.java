import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by dmanzelmann on 5/15/2015.
 */
public class OutlookCalendar {
    WebDriver driver;
    WebDriverWait wait;

    public OutlookCalendar(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void login(String username, String password) {
        driver.get("https://umail.umaryland.edu");

        driver.findElement(By.id("username")).sendKeys("rx\\" + username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.xpath("//*[@id=\"tblMid\"]/tbody/tr[7]/td/table/tbody/tr[3]/td/input[1]")).click();
    }

    public void navigateToCalendar() {
        driver.findElement(By.id("lnkCal")).click();
    }

    public void scheduleNewAppointment() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newapptc")));
        driver.findElement(By.xpath("//*[@id=\"newapptc\"]/span")).click();
    }

    public void setSubject(String subject) {
        driver.findElement(By.id("txtSubj")).sendKeys(subject);
    }

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "\\\\private\\Home\\Desktop\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        OutlookCalendar test = new OutlookCalendar(driver);

        Scanner input = new Scanner(System.in);
        System.out.print("username: ");
        String username = input.next();

        System.out.print("password: ");
        String password = input.next();

        test.login(username, password);
        test.navigateToCalendar();
        driver.switchTo().frame(0);
        test.scheduleNewAppointment();

        //switch to active window
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> iterator = handles.iterator();
        String mainWindow = iterator.next();
        String appointmentWindow = iterator.next();
        driver.switchTo().window(appointmentWindow);

        test.setSubject("Test Subject");
    }
}
