import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dmanzelmann on 5/8/2015.
 */

public class Profiles {
    public static List<Listing> filter(String name, List<Listing> list) {
        switch (name) {
            case "Douglas Manzelmann":
                List<Listing> mwfMediasite = list.stream().filter(l -> l.getDayOfWeek().equals("Monday")
                        || l.getDayOfWeek().equals("Wednesday")
                        || l.getDayOfWeek().equals("Friday"))
                        .filter(l -> l.getActivity().equals("Mediasite"))
                        .filter(l -> l.getStartTimeInLocalTimeFormat().isAfter(new LocalTime(12, 01)))
                        .collect(Collectors.toList());

                List<Listing> mwfTMS = list.stream().filter(l -> l.getDayOfWeek().equals("Monday")
                        || l.getDayOfWeek().equals("Wednesday")
                        || l.getDayOfWeek().equals("Friday"))
                        .filter(l -> l.getActivity().equals("Videoconference"))
                        .filter(l -> l.getStartTimeInLocalTimeFormat().isBefore(new LocalTime(12, 01)))
                        .collect(Collectors.toList());

                List<Listing> tthMediasite = list.stream().filter(l -> l.getDayOfWeek().equals("Tuesday")
                        || l.getDayOfWeek().equals("Thursday"))
                        .filter(l -> l.getActivity().equals("Mediasite"))
                        .filter(l -> l.getStartTimeInLocalTimeFormat().isBefore(new LocalTime(12, 01)))
                        .collect(Collectors.toList());

                List<Listing> tthTMS = list.stream().filter(l -> l.getDayOfWeek().equals("Tuesday")
                        || l.getDayOfWeek().equals("Thursday"))
                        .filter(l -> l.getActivity().equals("Videoconference"))
                        .filter(l -> l.getStartTimeInLocalTimeFormat().isAfter(new LocalTime(12, 01)))
                        .collect(Collectors.toList());

                return Stream.of(mwfMediasite, tthMediasite, mwfTMS, tthTMS).flatMap(l -> l.stream()).collect(Collectors.toList());
        }

        return list;
    }

    public static void main(String[] args) {
        DateTime one = new DateTime();
        DateTime two = new DateTime().plusMinutes(20);

        if (one.isAfter(two))
            System.out.println("one is after two");
        else
            System.out.println("two is after one");
    }
}
