import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
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

    public void setCategory(String category) {
        WebElement categories = driver.findElement(By.id("cat"));
        Actions action = new Actions(driver);
        action.moveToElement(categories).click().build().perform();

        //WebElement categoriesDiv = driver.findElement(By.id("divCatM"));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("document.getElementById('divCatM').style.display='inline-block';");
        WebElement mediasiteCat = driver.findElement(By.xpath("//*[@cmd='cat:MediaSite']"));
        action.moveToElement(mediasiteCat).click().build().perform();
    }

    public void setSubject(String subject) {
        driver.findElement(By.id("txtSubj")).sendKeys(subject);
    }

    public void setLocation(String location) {
        driver.findElement(By.id("txtLoc")).sendKeys(location);
    }

    public void setStartDate(int date) {
        WebElement startDate = driver.findElement(By.xpath("html/body/div[15]/div[2]/div[1]/div[1]/div[7]/div[2]/div/div/div/span"));
        //JavascriptExecutor js = (JavascriptExecutor) driver;
        //js.executeScript("arguments[0].text = '" + date + "';", startDate);
        //new Actions(driver).moveToElement(startDate).sendKeys(Keys.ENTER);

        WebElement today = driver.findElement(By.id("s"));
        WebElement todaysParent = today.findElement(By.xpath(".."));
        WebElement nextWeek = todaysParent.findElement(By.xpath("div/following-sibling::*"));

    }

    public void setStartTime(String time) {
        WebElement startTime = driver.findElement(By.xpath("html/body/div[15]/div[2]/div[1]/div[1]/div[7]/div[3]/div/div/div/div/span/input"));
        //startTime.clear();
        //startTime.sendKeys();
        //new Actions(driver).moveToElement(startTime).sendKeys(time, Keys.ENTER);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].text = '" + time + "';", startTime);
    }


    public void setEndDate(int date) {
        WebElement endDate = driver.findElement(By.xpath("html/body/div[15]/div[2]/div[1]/div[1]/div[8]/div[2]/div/div/div/span"));
        //JavascriptExecutor js = (JavascriptExecutor) driver;
        //js.executeScript("arguments[0].text = '" + date + "';", endDate);
        //new Actions(driver).moveToElement(endDate).sendKeys(Keys.ENTER);
    }

    public void setEndTime(String time) {
        WebElement endTime = driver.findElement(By.xpath("html/body/div[15]/div[2]/div[1]/div[1]/div[8]/div[3]/div/div/div/div/span/input"));
        //endTime.clear();
        //endTime.sendKeys(time);
        //new Actions(driver).moveToElement(endTime).sendKeys(time, Keys.ENTER);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].text = '" + time + "';", endTime);
    }

    public void pasteScreenShot(File screenshot) {
        driver.findElement(By.id("insertimage")).click();
        driver.switchTo().frame("iFrameModalDlg");
        driver.switchTo().frame(0);
        driver.findElement(By.id("file1")).sendKeys(screenshot.getAbsolutePath());
        driver.findElement(By.id("btnAttch")).click();
        driver.switchTo().defaultContent();
    }


    public void saveAndClose() {
        driver.findElement(By.id("saveclose")).click();
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
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        driver.switchTo().frame(0);
        test.scheduleNewAppointment();

        //switch to active window
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> iterator = handles.iterator();
        String mainWindow = iterator.next();
        String appointmentWindow = iterator.next();
        driver.switchTo().window(appointmentWindow);

        test.setSubject("Test Subject");
        test.setLocation("N103");

        String startTime = "3:00 PM";
        String endTime = "4:00 PM";
        test.setStartTime(startTime);
        test.setEndTime(endTime);

        //String date = "Tue 5/19/2015";
        int date = 19;
        test.setStartDate(date);
        test.setEndDate(date);

        test.setCategory("Mediasite");

        test.pasteScreenShot(scrFile);

        //test.saveAndClose();
    }
}
