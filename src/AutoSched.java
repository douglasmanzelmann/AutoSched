import org.joda.time.LocalDate;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

/**
 * Created by dmanzelmann on 3/13/2015.
 */
public class AutoSched {
    private WebDriver driver;
    private ReadSched readSched;
    private MediasiteSched mediasiteSched;
    private TMSSched tmsSched;
    private Map<String, HashMap<LocalDate, Character>> multiples;
    private List<Listing> listings;

    public AutoSched() {
        driver = new FirefoxDriver();
        readSched = new ReadSched(driver);
        mediasiteSched = new MediasiteSched(driver);
    }

    private void loginToPortal(String userName, String password) {
        readSched.setUserName(userName);
        readSched.setPassword(password);
        readSched.clickLogin();
    }

    private void visitPortalWeek(int year, int month, int day) {
        readSched.setWeek(year, month, day);
    }

    public void readSched(String userName, String password, int year, int month, int day) {
        readSched.setWeek(year, month, day);
        readSched.setUserName(userName);
        readSched.setPassword(password);
        readSched.clickLogin();
        readSched.readListings();
        listings = readSched.getListings();
        multiples = MediasiteSched.updateListingsForMultiples(listings);
    }

    public List<Listing> getListings() {
        return readSched.getListings();
    }

    public void loginToMediasite(String userName, String password) {
        driver.get("mediasite.umaryland.edu/mediasite/manage");

        mediasiteSched.loginToMediasite(userName, password);
    }

    public void createMediasitePresentations(List<Listing> listings, Boolean testing) {
        Queue<String> folders = new ArrayBlockingQueue<>(5);
        for (Listing presentation : listings) {

            if (testing) {
                folders.add("School of Pharmacy");
                folders.add("Training");
                folders.add("Testing");
            } else {
                folders.add("School of Pharamcy");
                folders.add("PharmD");
                LocalDate presentationDate = presentation.getLocalDate();
                folders.add(presentationDate.getYear() + " " + DateUtils.getCurrentSemester(presentationDate.getMonthOfYear()));
                folders.add(DateUtils.getCurrentSemesterAbbreviation(presentationDate.getYear(), presentationDate.getMonthOfYear()));
            }

            String title = "";
            if (multiples.isEmpty())
                System.out.println("multiples is empty");

            if (multiples.containsKey(presentation.getClassPrefix()))
                System.out.println("yes");


            System.out.println(presentation.getLocalDate());
            System.out.println(presentation.getMultipleVer());
            System.out.println(presentation.getClassPrefix());
            System.out.println(multiples.get(presentation.getClassPrefix()).get(presentation.getLocalDate()));



            if (presentation.getMultipleVer() == 'A') {
                if (multiples.get(presentation.getClassPrefix()).get(presentation.getLocalDate()) > 'A')
                   title = presentation.getClassName() + " " + presentation.getDateInMDYFormat() + " " + presentation.getMultipleVer();
                else
                    title = presentation.getClassName() + " " + presentation.getDateInMDYFormat();
            }
            else if (presentation.getMultipleVer() > 'A') {
                title = presentation.getClassName() + " " + presentation.getDateInMDYFormat() + " " + presentation.getMultipleVer();
            }


            try {
                mediasiteSched.createMediasitePresentation(folders, title, presentation.getClassDescription(), presentation.getFacultyQueue(),
                        presentation.getStartHour(), presentation.getStartMinute(), presentation.getamOrPm(), presentation.getDateInMDYFormat());
            } catch (org.openqa.selenium.NoSuchElementException e) {
                presentation.setError(e);
            }

        }
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

        System.out.print("Enter Mediasite username: ");
        userName = input.next();
        System.out.print("Enter Mediasite password: ");
        password = input.next();
        schedule.loginToMediasite(userName, password);
        /*schedule.createMediasitePresentations(listings.stream()
                .filter(l -> l.getActivity().equals("Mediasite"))
                .collect(Collectors.toList()), true);*/
        List<Listing> mediasiteListings = listings.stream()
                .filter(l -> l.getActivity().equals("Mediasite"))
                .collect(Collectors.toList());

        // this method mutates mediasiteListings
        schedule.multiples = MediasiteSched.updateListingsForMultiples(mediasiteListings);
        schedule.createMediasitePresentations(mediasiteListings, true);
    }
}
