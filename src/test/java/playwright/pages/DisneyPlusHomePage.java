package playwright.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

public class DisneyPlusHomePage extends DisneyPlusPage {
    private Page page;
    private Page.WaitForSelectorOptions isVisible;

    private final DisneyPlusPage plusPage = new DisneyPlusPage();
    private DisneyPlusHomePage homePage;

    public static class Locators {
        public static String someElement = "";
    }

    public DisneyPlusHomePage(Page page) {
        super();
        isVisible = new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000);
    }

    public void validateHomePage() {
        page.navigate(plusPage.getBaseUrl());
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.pause();
    }
}
