package io.github.mstahv.sliders;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import in.virit.mopo.Mopo;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MopoSmokeIT {

    @LocalServerPort
    private int port;

    private String rootUrl;

    static Playwright playwright = Playwright.create();


    private Browser browser;
    private Page page;
    private Mopo mopo;

    @PostConstruct
    public void init() {
        rootUrl = "http://localhost:" + port + "/";
    }

    @BeforeEach
    public void setup() {
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions()
//                        .setHeadless(false)
//                        .setDevtools(true)
                );

        BrowserContext context = browser.newContext(new Browser.NewContextOptions());
        page = context.newPage();
        mopo = new Mopo(page);
    }

    @AfterEach
    public  void closePlaywright() {
        page.close();
        browser.close();
    }

    @Test
    public void smokeTest() {
        mopo.getViewsReportedByDevMode(browser, rootUrl).forEach(viewName -> {
            String url = rootUrl + viewName;
            page.navigate(url);
            mopo.assertNoJsErrors();
            System.out.println("Checked %s and it contained no JS errors.".formatted(viewName));
        });
    }

}
