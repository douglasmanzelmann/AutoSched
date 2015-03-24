import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dmanzelmann on 2/11/15.
 */
public class Listing {
    private DateTime startTime;
    private DateTime endTime;
    private String room;
    private String className;
    private String classDescription;
    private String activity;
    private List<String> faculty = new ArrayList<>();

    public Listing() {}

    public void setClassName(String className) {
        this.className = className;
    }

    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

    public String getClassName() {
        return className;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public String getStartHour() {
        DateTimeFormatter hourFmt = DateTimeFormat.forPattern("h");

        return startTime.toString(hourFmt);
    }

    public String getStartMinute() {
        DateTimeFormatter minuteFmt = DateTimeFormat.forPattern("mm");
        return startTime.toString(minuteFmt);
    }


    public String getamOrPm() {
        DateTimeFormatter AMOrPM = DateTimeFormat.forPattern("a");
        return startTime.toString(AMOrPM);
    }
    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setFaculty(String faculty) {
        if (faculty.indexOf("PHARMD") == 0) {
            this.faculty.add("PHARMD");
            return;
        }

        List<String> tempFaculty = Arrays.asList(faculty.split("\n"));

        for (String temp : tempFaculty) {
            if (!temp.contains("PHARMD")) {
                this.faculty.add(temp);
                //this.faculty.add("Test, Test");
                //this.faculty.add("Morgan, Jill");
            }
        }
    }

    public List<String> getFaculty() {
        return faculty;
    }

    public String getDateInMDYFormat() {
        DateTimeFormatter MDYFmt = DateTimeFormat.forPattern("MM/dd/y");

        return startTime.toString(MDYFmt);
    }

    public String toString() {
        DateTimeFormatter startTimeFmt = DateTimeFormat.forPattern("M d h:mma");
        DateTimeFormatter endTimeFmt = DateTimeFormat.forPattern("h:mma");
        return startTime.toString(startTimeFmt) + " - " + endTime.toString(endTimeFmt) + " | " + room + " | " + activity
                + " | " + className + " | " + classDescription + " | " + String.join(", ", faculty);
    }

    public static void main(String[] args) {

    }
}
