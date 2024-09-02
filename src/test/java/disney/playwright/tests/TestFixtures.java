package disney.playwright.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import disney.playwright.pages.DisneyPlusHomePage;
import disney.playwright.pages.DisneyPlusRegistrationPage;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestFixtures {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
    DisneyPlusHomePage homePage;
    DisneyPlusRegistrationPage registrationPage;


    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @AfterAll
    void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void beforeEach() {
        context = browser.newContext();
        page = context.newPage();
        homePage = new DisneyPlusHomePage(page);
        registrationPage = new DisneyPlusRegistrationPage(page, homePage);
    }

    @AfterEach
    void afterEach() {
        context.close();
    }
}
