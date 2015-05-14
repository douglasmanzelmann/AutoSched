import org.joda.time.LocalDate;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
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
    private Map<String, List<HashMap<LocalDate, Character>>> multiples;
    private List<Listing> listings;

    public AutoSched() {
        System.setProperty("webdriver.chrome.driver", "\\\\private\\Home\\Desktop\\chromedriver.exe");
        driver = new ChromeDriver();
        readSched = new ReadSched(driver);
        mediasiteSched = new MediasiteSched(driver);
        tmsSched = new TMSSched(driver);
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
    }

    public List<Listing> getListings() {
        return readSched.getListings();
    }

    public void loginToMediasite(String userName, String password) {
        driver.get("http://mediasite.umaryland.edu/mediasite/manage");

        mediasiteSched.loginToMediasite(userName, password);
    }

    public void createMediasitePresentations(List<Listing> listings, Boolean testing) {
        multiples = MediasiteSched.updateListingsForMultiples(listings);
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

            if (presentation.getMultipleVer() == 'A') {
                char multiVer;
                int index = MediasiteSched.findIndexOfMap(multiples.get(presentation.getClassPrefix()), presentation.getLocalDate());
                if (multiples.get(presentation.getClassPrefix()).get(index).get(presentation.getLocalDate()) > 'A')
                    title = presentation.getClassName() + " " + presentation.getDateInMDYFormat() + " " + presentation.getMultipleVer();
                else
                    title = presentation.getClassName() + " " + presentation.getDateInMDYFormat();

            }
            else if (presentation.getMultipleVer() > 'A') {
                title = presentation.getClassName() + " " + presentation.getDateInMDYFormat() + " " + presentation.getMultipleVer();
            }


            try {
                File screenshot = mediasiteSched.createMediasitePresentation(folders, title, presentation.getClassDescription(), presentation.getFacultyQueue(),
                        presentation.getStartHour(), presentation.getStartMinute(), presentation.getamOrPm(), presentation.getDateInMDYFormat());
                presentation.setScreenshot(screenshot);
            } catch (org.openqa.selenium.NoSuchElementException e) {
                presentation.setError(e);
            }

            presentation.setScheduled();
        }
    }

    public void scheduleTMSSlots(List<Listing> listings, String username, String password) {
        tmsSched.login(username, password);

        for (Listing tmsSlot : listings) {
            String[] rooms = tmsSlot.getRooms();

            if (!tmsSched.scheduleVTC(username, password, rooms[0], rooms[1], tmsSlot.getStartTime().plusDays(90), tmsSlot.getEndTime().plusDays(90), true)) {
                System.out.println("break");
                break;
            }

            else
                tmsSlot.setScheduled();
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


        /*schedule.createMediasitePresentations(listings.stream()
                .filter(l -> l.getActivity().equals("Mediasite"))
                .collect(Collectors.toList()), true);*/
        List<Listing> mediasiteListings = listings.stream()
                .filter(l -> l.getActivity().equals("Mediasite"))
                .collect(Collectors.toList());

        // this method mutates mediasiteListings
        schedule.multiples = MediasiteSched.updateListingsForMultiples(mediasiteListings);

        System.out.print("Enter Mediasite username: ");
        userName = input.next();
        System.out.print("Enter Mediasite password: ");
        password = input.next();
        schedule.loginToMediasite(userName, password);
        schedule.createMediasitePresentations(mediasiteListings, true);
    }
}
