package playwright.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;
import playwright.pages.DisneyPlusHomePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void testHomePageLoad() {
        final float MAX_LOAD_TIME = 10; //seconds

        page.navigate(homePage.getBaseUrl());
        float loadTime = ((float) homePage.measurePageLoad() / 1000);
        String loadTimeStr = String.format("%.2f", loadTime);
        Assertions.assertFalse(loadTime > MAX_LOAD_TIME, "Home page at " + homePage.getBaseUrl()
                + " took more than " + String.valueOf(MAX_LOAD_TIME) + " seconds to load (" + loadTimeStr + "s)");
    }

    @Test
    public void testLazyLoading() {
        page.navigate(homePage.getBaseUrl());
        page.waitForLoadState(LoadState.NETWORKIDLE);
        homePage.scrollAndVerifyElements();
    }
}
