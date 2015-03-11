import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by dmanzelmann on 2/23/15.
 */
public final class FormatDate {
    public static DateTime formatDate(String date) {
        String month = date.substring(0, 2);
        int monthNumber = 0;
        int dayOfMonth = Integer.parseInt(date.substring(3));

        switch(month) {
            case "Jan":
                monthNumber = 1;
                break;
            case "Feb":
                monthNumber = 2;
                break;
            case "Mar":
                monthNumber = 3;
                break;
            case "Apr":
                monthNumber = 4;
                break;
            case "May":
                monthNumber = 5;
                break;
            case "Jun":
                monthNumber = 6;
                break;
            case "Jul":
                monthNumber = 7;
                break;
            case "Aug":
                monthNumber = 8;
                break;
            case "Sep":
                monthNumber = 9;
                break;
            case "Oct":
                monthNumber = 10;
                break;
            case "Nov":
                monthNumber = 11;
                break;
            case "Dec":
                monthNumber = 12;
                break;
        }


        int year = new DateTime().getYear();
        //DateTime dateTime = new DateTime(year, monthNumber, dayOfMonth);

        System.out.println(year);
        return new DateTime();
    }

    public static void main(String[] args) {
        FormatDate.formatDate("Feb 3");
    }
}
