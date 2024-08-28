package disney.playwright.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DisneyPlusRegistrationPage extends DisneyPlusPage {
    Properties suiteProperties = new Properties();
    private String testEmailAddress, testEmailFirstPart, testEmailSecondPart, testEmailPassword;
    private final Page page;
    private final DisneyPlusHomePage homePage;
    private final Page.WaitForSelectorOptions isVisible, isAttached;

    public static class Locators {
        public static String headingTitle = ".heading-title";
        public static String emailField = "input#email";
        public static String continueButton = "button[data-testid='continue-btn']";
        public static String passwordField = "input#password";
        public static String marketingCheckbox = "[data-testid*='marketing-input-box'] input";
        public static String agreeAndContinueButton = "[data-testid*='submit-btn']";
        public static String birthdateField = "input#birthdate";

    }

    public static class RandomHelper {
        static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        static SecureRandom rnd = new SecureRandom();

        static String randomString(int len){
            StringBuilder sb = new StringBuilder(len);
            for(int i = 0; i < len; i++)
                sb.append(AB.charAt(rnd.nextInt(AB.length())));
            return sb.toString();
        }
    }

    public DisneyPlusRegistrationPage(Page page, DisneyPlusHomePage homePage) {
        super(page);
        this.page = page;
        this.homePage = homePage;
        isVisible = new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000);
        isAttached = new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(10000);

        try {
            FileReader reader = new FileReader("playwright.properties");
            suiteProperties.load(reader);
            testEmailAddress = suiteProperties.getProperty("testEmailAddress");
            testEmailFirstPart = suiteProperties.getProperty("testEmailFirstPart");
            testEmailSecondPart = suiteProperties.getProperty("testEmailSecondPart");
            testEmailPassword = suiteProperties.getProperty("testEmailPassword");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerHuluBundle() {
        scrollVisible(DisneyPlusHomePage.Locators.planCompSection);
        assertTrue(page.locator(DisneyPlusHomePage.Locators.planSelect0).isVisible());
        page.locator(DisneyPlusHomePage.Locators.centerBundle1).click();

        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForSelector(Locators.headingTitle, isVisible);
        page.waitForSelector(Locators.emailField, isVisible);
        page.waitForSelector(Locators.continueButton, isVisible);

        page.locator(Locators.emailField).fill(testEmailFirstPart
                + "+"
                + RandomHelper.randomString(6)
                + testEmailSecondPart);
        page.locator(Locators.continueButton).click();
        page.waitForSelector(Locators.passwordField, isVisible);
        page.locator(Locators.passwordField).fill(testEmailPassword);
        page.locator(Locators.marketingCheckbox).click();
        page.locator(Locators.agreeAndContinueButton).click();
        page.waitForSelector(Locators.birthdateField, isVisible);
        page.locator(Locators.birthdateField).fill("01/01/2000");
        page.locator(Locators.continueButton).click();
    }
}
