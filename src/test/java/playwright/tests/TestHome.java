package playwright.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;
import playwright.pages.DisneyPlusHomePage;

public class TestHome {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    DisneyPlusHomePage homePage;


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
    }

    @AfterEach
    void afterEach() {
        page.close();
        context.close();
        browser.close();
    }

    @Test
    public void testHomePage() {
        page.navigate(homePage.getBaseUrl());
    }
}
