import org.joda.time.LocalDate;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by dmanzelmann on 3/13/2015.
 */
public class AutoSched {
    private WebDriver driver;
    private ReadSched readSched;
    private  MediasiteSched mediasiteSched;

    public AutoSched() throws InterruptedException {
        Scanner input = new Scanner(System.in);
        driver = new FirefoxDriver();
        readSched = new ReadSched(driver);
        mediasiteSched = new MediasiteSched(driver);
    }

    public void loginToPortal(String userName, String password) {
        readSched.setUserName(userName);
        readSched.setPassword(password);
        readSched.clickLogin();
    }

    public void visitPortalWeek(int year, int month, int day) {
        readSched.setWeek(year, month, day);
    }


    public void loginToMediasite(String userName, String password) {
        driver.get("mediasite.umaryland.edu/mediasite/manage");

        mediasiteSched.setUsername(userName);
        mediasiteSched.setPassword(password);
        mediasiteSched.clickLogin();

        mediasiteSched.navigateToSchoolOfPharmacy();
        mediasiteSched.navigateToPharmD();

        LocalDate current = new LocalDate();
        String currentSemester = current.getYear() + " " + DateUtils.getCurrentSemester(current.getMonthOfYear());
        mediasiteSched.navigateToSemester(currentSemester);
    }

    public void setMediasiteListings(List<Listing> listings) {
        mediasiteSched.setListings(listings);
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

        System.out.print("Enter UMB Portal username: ");
        String userName = input.next();
        System.out.print("Enter UMB Portal password: ");
        String password = input.next();
        schedule.loginToPortal(userName, password);

        schedule.readSched.readListings();
        List<Listing> listings = schedule.readSched.getListings();

        //listings.stream().forEach(l -> System.out.println(l.getDayOfWeek()));

        schedule.setMediasiteListings(listings.stream()
                .filter(l -> l.getActivity().equals("Mediaiste"))
                .collect(Collectors.toList()));

        System.out.print("Enter Mediasite username: ");
        userName = input.next();
        System.out.print("Enter Mediasite password: ");
        password = input.next();
        schedule.loginToMediasite(userName, password);
    }
}
