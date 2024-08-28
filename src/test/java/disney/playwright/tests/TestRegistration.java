package disney.playwright.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import disney.playwright.pages.DisneyPlusHomePage;
import disney.playwright.pages.DisneyPlusRegistrationPage;
import disney.playwright.util.EmailHelper;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRegistration {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    DisneyPlusHomePage homePage;
    DisneyPlusRegistrationPage registrationPage;


    @BeforeAll
    static void beforeAll() {
        playwright = Playwright.create();
    }

    @BeforeEach
    void beforeEach() {
        browser = playwright.chromium().launch();
        context = browser.newContext();
        page = context.newPage();
        homePage = new DisneyPlusHomePage(page);
        registrationPage = new DisneyPlusRegistrationPage(page, homePage);
    }

    @AfterEach
    void afterEach() {
        page.close();
        context.close();
        browser.close();
    }

    @Test
    public void testRegistrationLogin() {
        page.navigate(homePage.getBaseUrl());
        page.waitForLoadState(LoadState.NETWORKIDLE);
        registrationPage.registerHuluBundle();

        page.waitForTimeout(10000); // give it some time to let the e-mail come in
        EmailHelper emailHelper = new EmailHelper();
        try {
            assertTrue(emailHelper.isEmailSubjectPresent("New login to Disney+")
                    || emailHelper.isEmailSubjectPresent("Welcome to MyDisney"));
            emailHelper.deleteEmail();
            assertEquals(0, emailHelper.getEmailCount());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
