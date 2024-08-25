package playwright.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.IOException;
import java.util.Properties;

public class DisneyPlusPage {
    Properties properties = new java.util.Properties();
    String baseUrl;

    //props.load(this.class.getClassLoader().getResourceAsStream("project.properties"));

    public DisneyPlusPage() {
        try (FileReader reader = new FileReader("playwright.properties")){
            properties.load(reader);
            baseUrl = properties.getProperty("baseUrl");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
