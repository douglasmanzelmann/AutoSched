import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


/**
 * Created by dmanzelmann on 2/11/15.
 */
public class ReadSched {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        // weird issue
        // http://stackoverflow.com/questions/7615645/ssl-handshake-alert-unrecognized-name-error-since-upgrade-to-java-1-7-0
        System.setProperty("jsse.enableSNIExtension", "false");
        WebDriver driver = new FirefoxDriver();

        driver.get("https://rxsecure.umaryland.edu/apps/schedules/view/?type=search&searchtype=resource&id=100&start=2015-02-11&scope=week");

        WebElement username = driver.findElement(By.name("j_username"));
        System.out.println("Enter username: ");
        username.sendKeys(input.next());

        WebElement password = driver.findElement(By.name("j_password"));
        System.out.println("Enter password: ");
        password.sendKeys(input.next());

        WebElement login = driver.findElement(By.name("Login"));
        login.submit();

        WebElement tableAgenda = driver.findElement(By.id("agenda"));

        List<WebElement> tableAgendaRows = tableAgenda.findElements(By.tagName("tr"));
        List<Listing> listings = new ArrayList<Listing>();


        for (WebElement row : tableAgendaRows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            Iterator<WebElement> column = columns.iterator();
            Listing temp = new Listing();

            //temp.setDate(column.next().getText().replace("\n", " ").trim());
            temp.setDate(column.next().getAttribute("title"));
            temp.setStringTime(column.next().getText().trim());
            temp.setRoom(column.next().getText().replace("\n", " ").trim());
            temp.setActivity(column.next().getText().replace("\n", " ").trim());
            temp.setFaculty(column.next().getText().replace("\n", " ").trim());
            listings.add(temp);
            System.out.print("Listing: ");
            System.out.println(temp);
        }

    }

}
