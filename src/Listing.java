import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
    private String faculty;

    public Listing() {}

    public Listing(DateTime startTime, DateTime endTime, String room, String activity, String faculty) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.activity = activity;
        this.faculty = faculty;
    }

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
        this.faculty = faculty;
    }

    public String getDateInMDYFormat() {
        DateTimeFormatter MDYFmt = DateTimeFormat.forPattern("M/d/y");

        return startTime.toString(MDYFmt);
    }

    public String toString() {
        DateTimeFormatter startTimeFmt = DateTimeFormat.forPattern("M d h:mma");
        DateTimeFormatter endTimeFmt = DateTimeFormat.forPattern("h:mma");
        return startTime.toString(startTimeFmt) + " - " + endTime.toString(endTimeFmt) + " | " + room + " | " + activity
                + " | " + className + " | " + classDescription + " | " + faculty;
    }
}
