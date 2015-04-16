import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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
    WebDriver driver;
    WebElement password;
    WebDriverWait wait;
    int year;
    int month;

    public MediasiteSched(WebDriver driver) throws InterruptedException {
        //this needs to be a copy. or rather, I need to pass a copy to protect data.
        //this.listings = listings;
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    /*public void setListings(List<Listing> listings) {
        this.listings = listings;
    } */

    public void setUsername(String userName) {
        driver.findElement(By.id("UserName")).sendKeys(userName);
    }

    public void setPassword(String password) {
        driver.findElement(By.id("Password")).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(By.xpath("html/body/div[1]/div/div[2]/form/div/fieldset/p/input")).click();
        //driver.findElement(By.tagName("input")).click();
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
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(semester)));
        driver.findElement(By.partialLinkText(semester)).click();
    }

    public void navigateToClass(String className) {
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(className)));
        driver.findElement(By.partialLinkText(className)).click();
    }

    public void navigateToTraining() {
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Training")));
        driver.findElement(By.partialLinkText("Training")).click();
    }

    public void navigateToTesting() {
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Testing")));
        driver.findElement(By.partialLinkText("Testing")).click();
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

    //i.e., "PHAR539 Medicinal Chemistry 2 04/01/2015", still missing A, B, C, D
    public void setTitle(String className, String dateInMDYFormat) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Title")));
        driver.findElement(By.id("Title")).sendKeys(className + " " + dateInMDYFormat);
    }

    //i.e., "NSAIDS 1"
    public void setDescription(String classDescription) {
        driver.findElement(By.id("Description")).sendKeys(classDescription);
    }

    //adds existing and new presenters
    //should probably be split into two different sub methods
    public void setPresenters(Queue<String> presenterQueue) throws InterruptedException {
        Queue<String> notExistingPresenters = new ArrayBlockingQueue<String>(presenterQueue.size());
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("var list = document.getElementById('selectedPresentersList'); list.innerHTML = '';");
        js.executeScript("document.getElementById('AddPresenter').style.display='inline-block';");

        WebElement presentationPresenter = driver.findElement(By.id("AddPresenter"));
        Actions actions = new Actions(driver);
        actions.moveToElement(presentationPresenter).sendKeys(Keys.ARROW_DOWN).click().build().perform();
        WebElement addPresenter = driver.findElement(By.id("AddExisting"));
        addPresenter.click();

        // add existing presenters
        // if they do not exist, add them to notExistingPresenters
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
            save.click();
        }
    }

    private void setHour(String hour) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement presentationHour = driver.findElement(By.id("Hour"));
        js.executeScript("arguments[0].value = '" + hour + "';", presentationHour);
    }

    private void setMinute(String minute) {
        WebElement presentationMinute = driver.findElement(By.id("Minute"));
        presentationMinute.clear();
        presentationMinute.sendKeys(minute);
    }

    private void setTimeOfDay(String timeOfDay) {
        WebElement amOrPm = driver.findElement(By.id("AmPm"));
        Select morningOrNight = new Select(amOrPm);
        if (timeOfDay.equals("AM"))
            morningOrNight.selectByValue("AM");
        else
            morningOrNight.selectByValue("PM");
    }

    public void setTime(String hour, String minute, String timeOfDay) {
        setHour(hour);
        setMinute(minute);
        setTimeOfDay(timeOfDay);
    }

    public void setRecordDate(String dateInMDYFormat) {
        WebElement presentationDate = driver.findElement(By.id("RecordDate"));
        presentationDate.clear();
        presentationDate.sendKeys(dateInMDYFormat);

        //necessary? is kind of hacky.
        driver.findElement(By.id("Title")).click();
    }

    public void savePresentation() {
        WebElement save = driver.findElement(By.id("Save"));
        save.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Status")));
        //take screen shot
        //navigateToSemester();
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

    public static List<Listing> updateListingsForMultiples(List<Listing> mediasiteListings) {
        Map<String, HashMap<LocalDate, Character>> multiples = new HashMap<>();

        //this solution is O(n^2). Need a recursive solution, reversing the list ... which is also O(n^2)....
        //need a bottom  up solution? But then the first "multiple" wouldn't be assigned a letter

        /*for (Listing presentation : mediasiteListings) {
            if (multiples.containsKey(presentation.getClassPrefix()) &&
                    multiples.get(presentation.getClassPrefix()).containsKey(presentation.getLocalDate())) {

                // If PHAR559 has multiple recordings in one day, they need a unique ID (A, B, C, D, etc.).
                char mutliVer = multiples.get(presentation.getClassPrefix()).get(presentation.getLocalDate());
                mutliVer++;
                presentation.setMultipleVer(mutliVer);
            }

            else if (multiples.containsKey(presentation.getClassPrefix()) &&
                    !multiples.get(presentation.getClassPrefix()).containsKey(presentation.getLocalDate()))  {
                multiples.get(presentation.getClassPrefix()).put(presentation.getLocalDate(), 'A');
            }*/
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


        //MediasiteSched mediasiteSched = new MediasiteSched(testData);
        //System.out.println(new LocalDate().getMonthOfYear());
        //System.out.println(new LocalDate().getYear() + " " + DateUtils.getCurrentSemester(new LocalDate().getMonthOfYear()));
    }
}
