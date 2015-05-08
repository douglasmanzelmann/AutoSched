import org.joda.time.DateTime;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    public void selectTemplate(String baltimoreRoom, String sgRoom) {
        WebElement template;

        try {
            template = driver.findElement(By.partialLinkText(chooseCodec(baltimoreRoom) + " - " + sgRoom));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            template = driver.findElement(By.partialLinkText(chooseCodec(baltimoreRoom) + "- " + sgRoom));
        }

        //select the template and use as a conference
        Actions action = new Actions(driver);
        action.moveToElement(template).moveByOffset(50,0).click().build().perform();
        driver.findElement(By.partialLinkText("Use As Conference")).click();
    }


    public void selectUser(String userName) {
        driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_uxUserSelector_uxSelectUsersButton\"]")).click();
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> iterator = handles.iterator();
        String mainWindow = iterator.next();
        String userWindow = iterator.next();
        driver.switchTo().window(userWindow);
        driver.findElement(By.partialLinkText("Manzelmann")).click();
        driver.switchTo().window(mainWindow);
    }

    public void selectStartDateAndTime(DateTime start) {
        WebElement startDate;
        try {
            startDate = driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_conferenceTime_dpStartDate_textBox\"]"));
            startDate.clear();
        }  catch (org.openqa.selenium.StaleElementReferenceException e) {
            startDate = driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_conferenceTime_dpStartDate_textBox\"]"));
            startDate.clear();
            startDate.sendKeys(DateUtils.getDateInMDYFormat(start));
        }

        //start time
        WebElement startTime = driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_conferenceTime_tbStartTime\"]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = '" + DateUtils.getTime(start) + "';", startTime);
    }

    public void selectEndDateAndTime(DateTime end) {
        //select end date and time
        //end date
        WebElement endDate = driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_conferenceTime_dpEndDate_textBox\"]"));
        try {
            endDate.clear();
        }  catch (org.openqa.selenium.StaleElementReferenceException e) {
            endDate= driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_conferenceTime_dpStartDate_textBox\"]"));
            endDate.clear();
            endDate.sendKeys(DateUtils.getDateInMDYFormat(end));
        }
        //end time
        WebElement endTime = driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_conferenceTime_tbEndTime\"]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = '" + DateUtils.getTime(end) + "';", endTime);
    }

    public void submit() {
        driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_saveButton\"]")).click();
    }

    public boolean checkIfCodecIsInUse() {
        String errorMessage = "The system is already scheduled to be used at this time";

        try {
            driver.findElement(By.xpath("//*[contains(text(),'" + errorMessage + "') ]"));
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {  }

        return false;
    }

    public void scheduleVTC(String userName, String password, String baltimoreRoom, String sgRoom, DateTime start, DateTime end) {
        driver.get("http://" + userName + ":" + password + "@" + "tms.rx.umaryland.edu/tms/default.aspx?pageId=116");

        selectTemplate(baltimoreRoom, sgRoom);
        selectUser(userName);
        selectStartDateAndTime(start);
        selectEndDateAndTime(end);
        submit();
        if (checkIfCodecIsInUse())
            scheduleVTC(userName, password, "vtc4", sgRoom, start, end);

    }

    private static String chooseCodec(String baltimoreRoom) {
        if (baltimoreRoom.contains("PH N1"))
            return "VTC1";
        else if (baltimoreRoom.contains("PH N2"))
            return "VTC2";
        else if (baltimoreRoom.contains("PH N3"))
            return "VTC3";
        else if (baltimoreRoom.contains("PH S201"))
            return "UMB 201";
        else if (baltimoreRoom.contains("PH N208"))
            return "VTC-CART1";

        return "VTC4";
    }

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "\\\\private\\Home\\Desktop\\chromedriver.exe");
        TMSSched test = new TMSSched(new ChromeDriver());
        //TMSSched test = new TMSSched(new FirefoxDriver());
        test.login("dmanzelmann", "TK421.c3po");

        Listing vtc = new Listing();
        vtc.setRoom("PH N203 SGIII 2125");
        vtc.setClassName("SMdPHA General Body Meetings");
        vtc.setStartTime(new DateTime().plusDays(30));
        vtc.setEndTime(new DateTime().plusDays(30).plusMinutes(70));
        vtc.setFaculty("Me");
        String[] roomParts = vtc.getRoom().split(" ");
        String baltiomreRoom = roomParts[0] + " " + roomParts[1];
        String sgRoom = "USG " + roomParts[3];
        test.scheduleVTC("dmanzelmann", "TK421@c3po", baltiomreRoom, sgRoom, vtc.getStartTime(), vtc.getEndTime());

        Listing vtc2 = new Listing();
        vtc2.setRoom("PH N203 SGIII 2125");
        vtc2.setClassName("SMdPHA General Body Meetings");
        vtc2.setStartTime(new DateTime().plusDays(30).plusMinutes(10));
        vtc2.setEndTime(new DateTime().plusDays(30).plusMinutes(70));
        vtc2.setFaculty("Me");
        roomParts = vtc2.getRoom().split(" ");
        baltiomreRoom = roomParts[0] + " " + roomParts[1];
        sgRoom = "USG " + roomParts[3];
        test.scheduleVTC("dmanzelmann", "TK421@c3po", baltiomreRoom, sgRoom, vtc.getStartTime(), vtc.getEndTime());

    }
}
