import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by dmanzelmann on 2/11/15.
 */
public class ReadSched {
    Scanner input;
    WebDriver driver;
    WebElement username;
    WebElement password;
    WebElement login;
    WebElement tableAgenda;
    WebDriverWait wait;
    List<WebElement> tableAgendaRows;
    List<Listing> listings;

    public ReadSched() {
        input = new Scanner(System.in);
        // weird issue
        // http://stackoverflow.com/questions/7615645/ssl-handshake-alert-unrecognized-name-error-since-upgrade-to-java-1-7-0
        System.setProperty("jsse.enableSNIExtension", "false");
        driver = new FirefoxDriver();
        driver.get("https://rxsecure.umaryland.edu/apps/schedules/view/?type=search&searchtype=resource&id=100&start=2015-02-11&scope=week");

        username = driver.findElement(By.name("j_username"));
        System.out.println("Enter username: ");
        username.sendKeys(input.next());

        password = driver.findElement(By.name("j_password"));
        System.out.println("Enter password: ");
        password.sendKeys(input.next());

        login = driver.findElement(By.name("Login"));
        login.submit();

        wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("agenda")));
        tableAgenda = driver.findElement(By.id("agenda"));
        tableAgendaRows = tableAgenda.findElements(By.tagName("tr"));
        listings = new ArrayList<>();

        for (WebElement row : tableAgendaRows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            Iterator<WebElement> column = columns.iterator();
            Listing temp = new Listing();

            String tempDate = column.next().getAttribute("title"); // title attribute is the date, i.e., Feb 21
            String tempTime = column.next().getText().trim(); // entire duration of event, i.e., 4:00pm - 5:00pm

            String startTime = tempTime.substring(0, tempTime.indexOf('—')).trim();
            String endTime = tempTime.substring(tempTime.indexOf('—')+1).trim();

            temp.setStartTime(DateUtils.getDateTimeObject(tempDate, startTime));
            temp.setEndTime(DateUtils.getDateTimeObject(tempDate, endTime));

            temp.setRoom(column.next().getText().replace("\n", " ").trim());


            //temp.setActivity(column.next().getText().replace("\n", " ").trim());
            //String[] classDetails;
            List<String> classDetails = Arrays.asList(column.next().getText().trim().split("\n"));
            if (classDetails.get(classDetails.size() -1).contains("Recorded in Mediasite")) {
                temp.setClassName(classDetails.get(0));
                temp.setClassDescription(classDetails.get(2)); // multiple white lines
                temp.setActivity("Mediasite");
            }
            else if (classDetails.get(classDetails.size() -1).contains("Videoconference")) {
                temp.setClassName(classDetails.get(0));
                temp.setActivity("Videoconference");
            }
            // else if pre-record
            // also need to change autosched
            else if (classDetails.get(0).contains("Pre-record")) {
                temp.setClassName(classDetails.get(0).substring(0, classDetails.get(0).indexOf("Pre-record")));
                temp.setClassDescription(classDetails.get(1));
                temp.setActivity("Pre-record");
            }
            else {
                String totalActivity = "";
                for (String s : classDetails)
                    totalActivity += s;

                temp.setActivity(totalActivity);
            }

            temp.setFaculty(column.next().getText().trim());

            listings.add(temp);
            //System.out.print("Listing: ");
            //System.out.println(temp);
        }
    }

    public List<Listing> getListings() {
        return listings;
    }


    public static void main(String[] args) {
        ReadSched readSched = new ReadSched();
    }

}
