import org.joda.time.DateTime;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by dmanzelmann on 3/13/2015.
 */
public class MediasiteSched {
    Scanner input;
    List<Listing> listings;
    HashMap<String, HashMap<DateTime, Integer>> duplicatesByDay;
    WebDriver driver;
    WebElement username;
    WebElement password;
    WebElement login;
    WebElement schoolOfPharmacy;
    WebElement pharmD;
    WebElement semester;
    WebDriverWait wait;
    int year;
    int month;

    public void setUsername(String userName) {
        driver.findElement(By.id("UserName")).sendKeys(userName);
    }

    public void setPassword(String password) {
        driver.findElement(By.id("Password")).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(By.tagName("input")).click();
    }

    public void navigateToSchoolOfPharmacy() {
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("School of Pharmacy")));
        driver.findElement(By.partialLinkText("School of Pharmacy")).click();
    }

    public void navigateToPharmD() {
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("PharmD")));
        driver.findElement(By.partialLinkText("PharmD")).click();
    }

    public void navigateToSemester(String semester) {
        driver.findElement(By.partialLinkText(semester)).click();
    }

    public void navigateToClass(String className) {
        driver.findElement(By.partialLinkText(className)).click();
    }

    public void addNewPresentation() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("AddNewEntity_Element")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('AddNewEntity').style.display='inline-block';");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("PresentationAdd")));
        WebElement addPresentation = driver.findElement(By.id("PresentationAdd"));
        addPresentation.click();
    }

    public void selectTemplate(String template) {
        //wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("SOP Standard Template (2014)")));
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(template)));
        driver.findElement(By.partialLinkText(template)).click();
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    //i.e., "2015 Spring" or "2014 Fall"
    public String getSemesterString(int year, int month) {
        return year + " " + DateUtils.getCurrentSemester(month);
    }

    //i.e., Transform "PHAR5002 AST 2: Nutrition/Pain/Oncology" to "PHAR5002"
    public String getClassString(String className) {
        return className.substring(0, className.indexOf(" "));
    }

    //i.e., "SP15 PHAR5001"
    public String getClassFolderString(int year, int month, String classString) {
        return DateUtils.getCurrentSemesterAbbreviation(year, month) + " " + classString;
    }



    public MediasiteSched(List<Listing> listings) throws InterruptedException {
        //this needs to be a copy. or rather, I need to pass a copy to protect data.
        this.listings = listings;
        duplicatesByDay = new HashMap<>();

        input = new Scanner(System.in);
        // weird issue
        // http://stackoverflow.com/questions/7615645/ssl-handshake-alert-unrecognized-name-error-since-upgrade-to-java-1-7-0
        System.setProperty("jsse.enableSNIExtension", "false");
        driver = new FirefoxDriver();
        driver.get("https://mediasite.umaryland.edu/mediasite/manage/");

        username = driver.findElement(By.id("UserName"));
        System.out.println("Enter username: ");
        username.sendKeys(input.next());

        password = driver.findElement(By.id("Password"));
        System.out.println("Enter password: ");
        password.sendKeys(input.next());

        login = driver.findElement(By.tagName("input"));
        login.submit();

        wait = new WebDriverWait(driver, 30);

        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("School of Pharmacy")));
        schoolOfPharmacy = driver.findElement(By.partialLinkText("School of Pharmacy"));
        schoolOfPharmacy.click();

        //this is for testing only
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Training")));
        WebElement trainingFolder = driver.findElement(By.partialLinkText("Training"));
        trainingFolder.click();

        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Testing")));
        WebElement testingFolder = driver.findElement(By.partialLinkText("Testing"));
        testingFolder.click();

        //commented out this section for testing purposes.
        //this section goes from SOP->PharmD->2015 Spring
        /*wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("PharmD")));
        pharmD = driver.findElement(By.partialLinkText("PharmD"));
        pharmD.click();

        int year = new LocalDate().getYear();
        int month = new LocalDate().getMonthOfYear();
        String semesterString = year + " " + DateUtils.getCurrentSemester(month);

        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(semesterString)));
        semester = driver.findElement(By.partialLinkText(semesterString));
        semester.click();*/

        //keep in mind, these are all pharmd recordings. if there was ever a recording for another
        //program, this constructor would have to be generalized.

        for (Listing mediasitePresentation : listings) {
            //As above, this section is commented out for testing
            //will drive to training->testing instead
            /*
            String classNumber = mediasitePresentation.getClassName().substring(0, mediasitePresentation.getClassName().indexOf(" "));

            //need to get the date of each presentation? no. don't think so.
            String classFolderString = DateUtils.getCurrentSemesterAbbreviation(year, month) + " " + classNumber;
            wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(classFolderString)));
            WebElement classFolder = driver.findElement(By.partialLinkText(classFolderString));
            classFolder.click();*/


            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("AddNewEntity_Element")));
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("document.getElementById('AddNewEntity').style.display='inline-block';");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("PresentationAdd")));
            WebElement addPresentation = driver.findElement(By.id("PresentationAdd"));
            addPresentation.click();

            wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("SOP Standard Template (2014)")));
            WebElement template = driver.findElement(By.partialLinkText("SOP Standard Template (2014)"));
            template.click();

            //name
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Title")));
            WebElement presentationTitle = driver.findElement(By.id("Title"));
            presentationTitle.sendKeys(mediasitePresentation.getClassName() +
                                        " " + mediasitePresentation.getDateInMDYFormat()); //needs a date at the end
                                                                                           //also needs logic for a, b, c, d...

            //description
            WebElement presentationDescription = driver.findElement(By.id("Description"));
            presentationDescription.sendKeys(mediasitePresentation.getClassDescription());

            //presenters
            js.executeScript("var list = document.getElementById('selectedPresentersList'); list.innerHTML = '';");
            js.executeScript("document.getElementById('AddPresenter').style.display='inline-block';");

            //this will only add existing presenters. need logic for:
            //-not finding an existing presenter
            //-adding a new presenter
            //needs to be abstracted into methods

            //copy faculty into a queue
            Queue<String> presenterQueue = new ArrayBlockingQueue<String>(mediasitePresentation.getFaculty().size());
            Queue<String> notExistingPresenters = new ArrayBlockingQueue<String>(mediasitePresentation.getFaculty().size());
            for (String p : mediasitePresentation.getFaculty())
                presenterQueue.add(p);

            WebElement presentationPresenter = driver.findElement(By.id("AddPresenter"));
            Actions actions = new Actions(driver);
            actions.moveToElement(presentationPresenter).sendKeys(Keys.ARROW_DOWN).click().build().perform();//.moveToElement(driver.findElement(By.xpath("")));
            WebElement addPresenter = driver.findElement(By.id("AddExisting"));
            addPresenter.click();
            while (presenterQueue.peek() != null) {
                try {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("SearchTerm")));
                    WebElement presenterSearch = driver.findElement(By.id("SearchTerm"));
                    presenterSearch.clear();
                    presenterSearch.sendKeys(presenterQueue.peek());
                    WebElement searchButton = driver.findElement(By.partialLinkText("Search"));
                    actions.moveToElement(searchButton).click().build().perform();

                    Thread.sleep(5000);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ResultsTable")));
                    List<WebElement> searchResults = driver.findElements(By.className("ResultsTable"));
                    if (searchResults.size() == 1) {
                        WebElement selectPresenter = driver.findElement(By.id("Check"));
                        selectPresenter.click();
                    }
                    presenterQueue.poll();
                } catch (TimeoutException e) {
                    System.out.println("In catch.");
                    notExistingPresenters.add(presenterQueue.poll());
                }
            }
            WebElement addSelected = driver.findElement(By.partialLinkText("Add Selected"));
            addSelected.click();

            // add new presenters
            if (notExistingPresenters.size() > 0) {
                Select select = new Select(presentationPresenter);
                select.selectByValue("AddNew");

                while (notExistingPresenters.peek() != null) {
                    String newPresenter = notExistingPresenters.poll();
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("presenterAddEditForm")));
                    WebElement lastName = driver.findElement(By.id("LastName"));
                    lastName.sendKeys(newPresenter.substring(0, newPresenter.indexOf(",")));

                    WebElement firstName = driver.findElement(By.id("AdditionalInfo"));
                    firstName.sendKeys(newPresenter.substring(newPresenter.indexOf(",") + 1));
                }

                WebElement addNewDialog = driver.findElement(By.id("DialogPresenterSelector"));
                WebElement save = addNewDialog.findElement(By.id("Save"));
                //actions.moveToElement(save).click().build().perform();
                save.click();
            }

            //time
            //hour
            //requires funky javascript hack
            WebElement presentationHour = driver.findElement(By.id("Hour"));
            js.executeScript("arguments[0].value = '" + mediasitePresentation.getStartHour() + "';", presentationHour);
            //minute
            WebElement presentationMinute = driver.findElement(By.id("Minute"));
            presentationMinute.clear();
            presentationMinute.sendKeys(mediasitePresentation.getStartMinute());

            WebElement amOrPm = driver.findElement(By.id("AmPm"));
            Select morningOrNight = new Select(amOrPm);
            if (mediasitePresentation.getamOrPm().equals("AM"))
                morningOrNight.selectByValue("AM");
            else
                morningOrNight.selectByValue("PM");

            //record date
            WebElement presentationDate = driver.findElement(By.id("RecordDate"));
            presentationDate.clear();
            presentationDate.sendKeys(mediasitePresentation.getDateInMDYFormat());
            presentationTitle.click();

            //save
            WebElement save = driver.findElement(By.id("Save"));
            save.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Status")));
            //take screen shot

            testingFolder.click();
        }

    }

    private void markMultipleLecturesInADay() {
        //listings.stream().()
    }

    public static void main(String[] args) throws InterruptedException {
        List<Listing> testData = new ArrayList<>();
        Listing test = new Listing();
        test.setRoom("N103");
        test.setClassName("PHAR580 Pharmacy Law");
        test.setClassDescription("Substitution and Medical Errors ");
                test.setActivity("Mediasite");
        test.setStartTime(new DateTime());
        test.setEndTime(new DateTime());
        test.setFaculty("Test2, Test2\nFletcher, Steven\nTest, Test\nPalumbo, Frank");
        testData.add(test);


        MediasiteSched mediasiteSched = new MediasiteSched(testData);
        //System.out.println(new LocalDate().getMonthOfYear());
        //System.out.println(new LocalDate().getYear() + " " + DateUtils.getCurrentSemester(new LocalDate().getMonthOfYear()));
    }
}
