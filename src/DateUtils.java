import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;

/**
 * Created by dmanzelmann on 2/23/15.
 */
public final class DateUtils {
    public static DateTime getDateTimeObject(String date, String time) {
        int year = new DateTime().getYear();
        String month = date.substring(0, 3);
        int monthNumber = 0;
        int dayOfMonth = Integer.parseInt(date.substring(3).trim());
        int hourOfDay = DateUtils.getHour(time);
        int minuteOfHour = DateUtils.getMinutes(time);
        int SECOND_OF_MINUTE = 0;


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

        DateTime dateTime = new DateTime(year, monthNumber, dayOfMonth, hourOfDay,
                minuteOfHour, SECOND_OF_MINUTE, DateTimeZone.forID("America/New_York"));

        return dateTime;
    }

    public static int getHour(String time) {
        int result = -1;
        int split = time.indexOf(' ');
        String timeField = time.substring(0, split);
        String amOrPm = time.substring(time.indexOf(' ')).trim();

        int hours = Integer.parseInt(timeField.substring(0, timeField.indexOf(':')));

        if (amOrPm.equals("AM") || hours == 12) {
            result = hours;
        }
        else {
            result = hours + 12;
        }

        return result;
    }

    public static int getMinutes(String time) {
        int result;
        String timeField = time.substring(0, time.indexOf(' '));

        result = Integer.parseInt(timeField.substring(timeField.indexOf(':')+1));

        return result;
    }

    public static String getCurrentSemester(int month) {
        if (month >= 1 && month <= 5)
            return "Spring";
        else if (month >= 8 && month <= 12)
            return "Fall";

        return "";
    }

    public static String getCurrentSemesterAbbreviation(int year, int month) {
        String semester = DateUtils.getCurrentSemester(month);
        int yearAbbreviation = year % 100;

        if (semester.equals("Spring"))
            return "SP" + yearAbbreviation;
        else if (semester.equals("Fall"))
            return "FA" + yearAbbreviation;

        return "";
    }

    public static void main(String[] args) {

        //DateUtils.getDateTimeObject("Feb 3");
        System.out.println("10:00 AM: " + getHour("10:00 AM"));
        System.out.println("4:00 PM " + getHour("4:00 PM"));

        System.out.println("minutes 10:00 AM " + getMinutes("10:00 AM"));

        System.out.println(DateUtils.getDateTimeObject("Mar 12", "4:00 PM"));

        System.out.println(2015 % 100);
        System.out.println(DateUtils.getCurrentSemesterAbbreviation(2015, 3));
    }
}
