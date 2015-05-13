import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

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
        //start time
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement startDate;

        try {
            startDate = driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_conferenceTime_dpStartDate_textBox\"]"));
            js.executeScript("arguments[0].value = '" + DateUtils.getDateInMDYFormat(start) + "';", startDate);
        } catch (StaleElementReferenceException | NoSuchElementException e) {
            startDate = driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_conferenceTime_dpStartDate_textBox\"]"));
            js.executeScript("arguments[0].value = '" + DateUtils.getDateInMDYFormat(start) + "';", startDate);
        }


        WebElement startTime = driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_conferenceTime_tbStartTime\"]"));
        js.executeScript("arguments[0].value = '" + DateUtils.getTime(start) + "';", startTime);
    }

    public void selectEndDateAndTime(DateTime end) {
        //select end date and time
        //end date
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement endDate = driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_conferenceTime_dpEndDate_textBox\"]"));
        js.executeScript("arguments[0].value = '" + DateUtils.getDateInMDYFormat(end) + "';", endDate);

        WebElement endTime = driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_conferenceTime_tbEndTime\"]"));
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

    public boolean scheduleVTC(String userName, String password, String baltimoreRoom, String sgRoom, DateTime start, DateTime end, boolean sentinal) {
        driver.get("http://" + userName + ":" + password + "@" + "tms.rx.umaryland.edu/tms/default.aspx?pageId=116");

        selectTemplate(baltimoreRoom, sgRoom);
        selectUser(userName);
        selectStartDateAndTime(start);
        selectEndDateAndTime(end);
        submit();

        if (!checkIfCodecIsInUse())
            return true;
        else if (checkIfCodecIsInUse() && sentinal)
            scheduleVTC(userName, password, "vtc4", sgRoom, start, end, false);
        else
            return false;

        return true;
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
        Scanner input = new Scanner(System.in);
        System.setProperty("webdriver.chrome.driver", "\\\\private\\Home\\Desktop\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        ReadSched readSched = new ReadSched(driver);
        readSched.setWeek(2015, 4, 19);
        System.out.print("username: ");
        readSched.setUserName(input.next());
        System.out.print("password: ");
        readSched.setPassword(input.next());
        readSched.clickLogin();
        readSched.readListings();
        List<Listing> listings = readSched.getListings();

        List<Listing> tmsSlots = listings.stream()
                .filter(l -> l.getActivity().equals("Videoconference"))
                .collect(Collectors.toList());

        TMSSched tmsSched = new TMSSched(driver);
        tmsSched.login("dmanzelmann", "TK421.c3po");
        Listing tmsSlotOne = tmsSlots.get(0);
        String[] rooms = tmsSlotOne.getRooms();
        tmsSched.scheduleVTC("dmanzelmann", "TK421.c3po", rooms[0], rooms[1],
                tmsSlotOne.getStartTime().plusDays(90),
                tmsSlotOne.getEndTime().plusDays(90), true);

    }
}
