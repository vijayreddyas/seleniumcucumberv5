package runner;

import base.TestBase;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import java.io.IOException;

@CucumberOptions(
        strict = true,
        glue = "stepdefinitions",
        features = {"src/test/resources/features"},
        plugin = {"json:target/cucumber-report.json"}
)
public class RunCucumberTest extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @BeforeClass
    public void setup() throws IOException {
        TestBase tb = new TestBase();
        tb.startGrid("localhost", "4444");
    }
}
