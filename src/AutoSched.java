import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by dmanzelmann on 3/13/2015.
 */
public class AutoSched {
    private WebDriver driver;
    private ReadSched readSched;

    public AutoSched() {
        Scanner input = new Scanner(System.in);
        driver = new FirefoxDriver();
        readSched = new ReadSched(driver);

    }

    private void setPortalUserName(String userName) {
        readSched.setUserName(userName);
    }

    private void setPortalPassword(String password) {
        readSched.setPassword(password);
    }

    public void loginToPortal(String userName, String password) {
        setPortalUserName(userName);
        setPortalPassword(password);

        readSched.clickLogin();
    }

    public void visitPortalWeek(int year, int month, int day) {
        readSched.setWeek(year, month, day);
    }



    public static void main(String[] args) throws InterruptedException {
        // weird issue
        // http://stackoverflow.com/questions/7615645/ssl-handshake-alert-unrecognized-name-error-since-upgrade-to-java-1-7-0
        System.setProperty("jsse.enableSNIExtension", "false");
        AutoSched schedule = new AutoSched();
        Scanner input = new Scanner(System.in);

        System.out.println("Enter year: ");
        int year = input.nextInt();
        System.out.println("Enter month: ");
        int month = input.nextInt();
        System.out.println("Enter day: ");
        int day = input.nextInt();

        schedule.visitPortalWeek(year, month, day);

        System.out.println("Enter username: ");
        String userName = input.next();
        System.out.println("Enter password: ");
        String password = input.next();
        schedule.loginToPortal(userName, password);

        schedule.readSched.readListings();
        List<Listing> listings = schedule.readSched.getListings();

        listings.stream().forEach(l -> System.out.println(l.getDayOfWeek()));



        /*MediasiteSched mediasiteSched = new MediasiteSched(listings.stream()
                    .filter(l -> l.getActivity().equals("Mediasite"))
                    .collect(Collectors.toList()));*/

    }
}
