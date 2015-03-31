import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dmanzelmann on 3/13/2015.
 */
public class AutoSched {
    public static void main(String[] args) throws InterruptedException {
        // weird issue
        // http://stackoverflow.com/questions/7615645/ssl-handshake-alert-unrecognized-name-error-since-upgrade-to-java-1-7-0
        System.setProperty("jsse.enableSNIExtension", "false");

        WebDriver driver = new FirefoxDriver();


        ReadSched readSched = new ReadSched(driver);
        List<Listing> listings = readSched.getListings();
        System.out.println("here");

        listings.stream().forEach(l -> l.getDayOfWeek());

        /*MediasiteSched mediasiteSched = new MediasiteSched(listings.stream()
                    .filter(l -> l.getActivity().equals("Mediasite"))
                    .collect(Collectors.toList()));*/

    }
}
