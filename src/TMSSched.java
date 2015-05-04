import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    public void scheduleVTC(String userName, String password, String baltimoreRoom, String sgRoom, DateTime start, DateTime end) {
        driver.get("http://" + userName + ":" + password + "@" + "tms.rx.umaryland.edu/tms/default.aspx?pageId=116");

        WebElement template;

        try {
            template = driver.findElement(By.partialLinkText(chooseCodec(baltimoreRoom) + " - " + sgRoom));
        }
        catch (org.openqa.selenium.NoSuchElementException e) {
            template = driver.findElement(By.partialLinkText(chooseCodec(baltimoreRoom) + "- " + sgRoom));
        }

        Actions action = new Actions(driver);
        action.moveToElement(template).moveByOffset(50,0).click().build().perform();
        driver.findElement(By.partialLinkText("Use As Conference")).click();

        driver.findElement(By.xpath("//*[@id=\"ctl00_uxContent_ctl01_uxUserSelector_uxSelectUsersButton\"]")).click();

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
        test.login("dmanzelmann", "TK421@c3po");

        Listing vtc = new Listing();
        vtc.setRoom("PH N103 SGIII 2202");
        vtc.setClassName("SMdPHA General Body Meetings");
        vtc.setStartTime(new DateTime().plusDays(30));
        vtc.setEndTime(new DateTime().plusDays(30).plusMinutes(60));
        vtc.setFaculty("Me");
        String[] roomParts = vtc.getRoom().split(" ");
        String baltiomreRoom = roomParts[0] + " " + roomParts[1];
        String sgRoom = "USG " + roomParts[3];
        System.out.println(sgRoom);

        test.scheduleVTC("dmanzelmann", "TK421@c3po", baltiomreRoom, sgRoom, vtc.getStartTime(), vtc.getEndTime());
    }
}
