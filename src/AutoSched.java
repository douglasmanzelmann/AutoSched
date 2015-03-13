import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dmanzelmann on 3/13/2015.
 */
public class AutoSched {
    public static void main(String[] args) {
        ReadSched readSched = new ReadSched();
        List<Listing> listings = readSched.getListings();

        MediasiteSched mediasiteSched = new MediasiteSched(listings.stream()
                .filter(l -> l.getActivity().contains("Recorded in Mediasite"))
                .collect(Collectors.toList()));
    }
}
