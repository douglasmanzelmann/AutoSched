import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dmanzelmann on 5/8/2015.
 */

public class Profiles {
    public static String[] getProfiles() {
        return new String[]{"dmanzelmann", "bhall"};
    }

    public static List<Listing> filter(String name, List<Listing> list) {
        List<Listing> mwfMediasite;
        List<Listing> mwfTMS;
        List<Listing> tthMediasite;
        List<Listing> tthTMS;

        switch (name) {
            case "dmanzelmann":
                // Douglas' schedule
                // Mon, Wed, Fri 8:30am-12:50am VTC; 1:00pm-End of Day Mediasite
                // Tues, Thur 8:30am-12:50am Mediasite; 1:00pm-End of Day VTC
                mwfMediasite = list.stream().filter(l -> l.getDayOfWeek().equals("Monday")
                        || l.getDayOfWeek().equals("Wednesday")
                        || l.getDayOfWeek().equals("Friday"))
                        .filter(l -> l.getActivity().equals("Mediasite"))
                        .filter(l -> l.getStartTimeInLocalTimeFormat().isAfter(new LocalTime(12, 01)))
                        .collect(Collectors.toList());

                mwfTMS = list.stream().filter(l -> l.getDayOfWeek().equals("Monday")
                        || l.getDayOfWeek().equals("Wednesday")
                        || l.getDayOfWeek().equals("Friday"))
                        .filter(l -> l.getActivity().equals("Videoconference"))
                        .filter(l -> l.getStartTimeInLocalTimeFormat().isBefore(new LocalTime(12, 01)))
                        .collect(Collectors.toList());

                tthMediasite = list.stream().filter(l -> l.getDayOfWeek().equals("Tuesday")
                        || l.getDayOfWeek().equals("Thursday"))
                        .filter(l -> l.getActivity().equals("Mediasite"))
                        .filter(l -> l.getStartTimeInLocalTimeFormat().isBefore(new LocalTime(12, 01)))
                        .collect(Collectors.toList());

                tthTMS = list.stream().filter(l -> l.getDayOfWeek().equals("Tuesday")
                        || l.getDayOfWeek().equals("Thursday"))
                        .filter(l -> l.getActivity().equals("Videoconference"))
                        .filter(l -> l.getStartTimeInLocalTimeFormat().isAfter(new LocalTime(12, 01)))
                        .collect(Collectors.toList());

                return Stream.of(mwfMediasite, tthMediasite, mwfTMS, tthTMS).flatMap(l -> l.stream()).collect(Collectors.toList());
            case "bhall":
                // Brian's schedule
                // Mon, Wed, Fri 8:30am-12:50am Mediasite; 1:00pm-End of Day VTC
                // Tues, Thur 8:30am-12:50am VTC; 1:00pm-End of Day Mediasite
                mwfMediasite = list.stream().filter(l -> l.getDayOfWeek().equals("Monday")
                        || l.getDayOfWeek().equals("Wednesday")
                        || l.getDayOfWeek().equals("Friday"))
                        .filter(l -> l.getActivity().equals("Mediasite"))
                        .filter(l -> l.getStartTimeInLocalTimeFormat().isBefore(new LocalTime(12, 01)))
                        .collect(Collectors.toList());

                 mwfTMS = list.stream().filter(l -> l.getDayOfWeek().equals("Monday")
                        || l.getDayOfWeek().equals("Wednesday")
                        || l.getDayOfWeek().equals("Friday"))
                        .filter(l -> l.getActivity().equals("Videoconference"))
                        .filter(l -> l.getStartTimeInLocalTimeFormat().isAfter(new LocalTime(12, 01)))
                        .collect(Collectors.toList());

                tthMediasite = list.stream().filter(l -> l.getDayOfWeek().equals("Tuesday")
                        || l.getDayOfWeek().equals("Thursday"))
                        .filter(l -> l.getActivity().equals("Mediasite"))
                        .filter(l -> l.getStartTimeInLocalTimeFormat().isAfter(new LocalTime(12, 01)))
                        .collect(Collectors.toList());

                tthTMS = list.stream().filter(l -> l.getDayOfWeek().equals("Tuesday")
                        || l.getDayOfWeek().equals("Thursday"))
                        .filter(l -> l.getActivity().equals("Videoconference"))
                        .filter(l -> l.getStartTimeInLocalTimeFormat().isBefore(new LocalTime(12, 01)))
                        .collect(Collectors.toList());

                return Stream.of(mwfMediasite, tthMediasite, mwfTMS, tthTMS).flatMap(l -> l.stream()).collect(Collectors.toList());
        }

        return list;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.setProperty("webdriver.chrome.driver", "\\\\private\\Home\\Desktop\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        ReadSched test = new ReadSched(driver);

        test.setWeek(2015, 4, 19);
        System.out.print("username: ");
        test.setUserName(input.next());
        System.out.print("password: ");
        test.setPassword(input.next());
        test.clickLogin();
        test.readListings();
        List<Listing> listings = test.getListings();
        List<Listing> dougsListings = Profiles.filter("Douglas Manzelmann", listings);

        for (Listing l : dougsListings)
            System.out.println(l);
    }
}
