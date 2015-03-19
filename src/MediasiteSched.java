import org.joda.time.LocalDate;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by dmanzelmann on 3/13/2015.
 */
public class MediasiteSched {
    Scanner input;
    List<Listing> listings;
    WebDriver driver;
    WebElement username;
    WebElement password;
    WebElement login;
    WebElement schoolOfPharmacy;
    WebElement pharmD;
    WebElement semester;
    WebDriverWait wait;

    public MediasiteSched(List<Listing> listings) throws InterruptedException {
        //this needs to be a copy. or rather, I need to pass a copy to protect data.
        this.listings = listings;

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

        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("PharmD")));
        pharmD = driver.findElement(By.partialLinkText("PharmD"));
        pharmD.click();

        int year = new LocalDate().getYear();
        int month = new LocalDate().getMonthOfYear();
        String semesterString = year + " " + DateUtils.getCurrentSemester(month);

        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(semesterString)));
        semester = driver.findElement(By.partialLinkText(semesterString));
        semester.click();

        //keep in mind, these are all pharmd recordings. if there was ever a recording for another
        //program, this constructor would have to be generalized.
        for (Listing mediasitePresentation : listings) {
            String classNumber = mediasitePresentation.getClassName().substring(0, mediasitePresentation.getClassName().indexOf(" "));

            //need to get the date of each presentation? no. don't think so.
            String classFolderString = DateUtils.getCurrentSemesterAbbreviation(year, month) + " " + classNumber;
            wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(classFolderString)));
            WebElement classFolder = driver.findElement(By.partialLinkText(classFolderString));
            classFolder.click();


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
            for (String p : mediasitePresentation.getFaculty()) {
                WebElement presentationPresenter = driver.findElement(By.id("AddPresenter"));
                Actions actions = new Actions(driver);
                actions.moveToElement(presentationPresenter).sendKeys(Keys.ARROW_DOWN).click().build().perform();//.moveToElement(driver.findElement(By.xpath("")));
                WebElement addPresenter = driver.findElement(By.id("AddExisting"));
                addPresenter.click();

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("SearchTerm")));
                WebElement presenterSearch = driver.findElement(By.id("SearchTerm"));
                presenterSearch.sendKeys(p);
                WebElement searchButton = driver.findElement(By.partialLinkText("Search"));
                actions.moveToElement(searchButton).click().build().perform();

                Thread.sleep(5000);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ResultsTable")));
                List<WebElement> searchResults = driver.findElements(By.className("ResultsTable"));
                if (searchResults.size() == 1) {
                    WebElement selectPresenter = driver.findElement(By.id("Check"));
                    selectPresenter.click();
                    WebElement addSelected = driver.findElement(By.partialLinkText("Add Selected"));
                    addSelected.click();
                }
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
            break;
        }

    }

    public static void main(String[] args) {
        //MediasiteSched mediasiteSched = new MediasiteSched();
        //System.out.println(new LocalDate().getMonthOfYear());
        System.out.println(new LocalDate().getYear() + " " + DateUtils.getCurrentSemester(new LocalDate().getMonthOfYear()));
    }
}
