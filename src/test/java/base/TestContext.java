package base;

import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

public class TestContext {
    private WebDriver driver = null;
    public WebDriver dr = null;
    public Scenario scenario = null;

    // Thread local variable containing each thread's WebDriver object
    private static final ThreadLocal<TestContext> thread = new ThreadLocal<TestContext>() {
        protected TestContext initialValue() {
            return new TestContext();
        }
    };

    protected WebDriver getDriver() {
        return getWebDriver();
    }

    protected TestContext get() {
        return thread.get();
    }

    public WebDriver getWebDriver() {
        return thread.get().driver;
    }

    public void setWebDriver(WebDriver driver) {
        thread.get().driver = driver;
    }

    public void setScenario(Scenario scenario){
        thread.get().scenario = scenario;
    }

    public Scenario getScenario(){
        return thread.get().scenario;
    }
}
