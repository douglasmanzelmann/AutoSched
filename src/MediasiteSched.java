import org.joda.time.LocalDate;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
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

    public MediasiteSched(List<Listing> listings) {
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
            System.out.println(classFolderString);

            wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(classFolderString)));
            WebElement classFolder = driver.findElement(By.partialLinkText(classFolderString));
            //Actions rightClick = new Actions(driver);
            //rightClick.contextClick(classFolder).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).click().build().perform();
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
            presentationTitle.sendKeys(mediasitePresentation.getClassName()); //needs a date at the end
                                                                              //also needs logic for a, b, c, d...

            //description
            WebElement presentationDescription = driver.findElement(By.id("Description"));
            presentationDescription.sendKeys(mediasitePresentation.getClassDescription());

            //presenters
            //js.executeScript("var list = document.getElementById('selectedPresentersList'); list.innerHTML = '';");
            js.executeScript("document.getElementById('AddPresenter').style.display='inline-block';");

            WebElement presentationPresenter = driver.findElement(By.id("AddPresenter"));
            //Select presenterSelector = new Select(presentationPresenter);
            //presenterSelector.selectByValue("AddExisting");
            Actions actions = new Actions(driver);
            actions.moveToElement(presentationPresenter).build().perform();//.moveToElement(driver.findElement(By.xpath("")));

            //record date
            /*WebElement presentationDate = driver.findElement(By.id("RecordDate"));
            presentationDate.clear();
            presentationDate.sendKeys(mediasitePresentation.getDateInMDYFormat());*/

            break;
        }

    }

    public static void main(String[] args) {
        //MediasiteSched mediasiteSched = new MediasiteSched();
        //System.out.println(new LocalDate().getMonthOfYear());
        System.out.println(new LocalDate().getYear() + " " + DateUtils.getCurrentSemester(new LocalDate().getMonthOfYear()));
    }
}
