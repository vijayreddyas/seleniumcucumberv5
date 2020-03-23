package runner;

import base.TestBase;
import helpers.LoadProperties;
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
        if (LoadProperties.getProperty("gridBasedExecution").toLowerCase().
                equalsIgnoreCase("true")) {
            System.out.println("$$$$$$$$$$$$$$$$$$$  VERIFYING THE GRID STATUS AS THE EXECUTIONS ARE GRID BASED  $$$$$$$$$$$$$$$$$$$$$$");
            TestBase tb = new TestBase();
            tb.startGrid("localhost", "4444");
        }else{
            System.out.println("$$$$$$$$$$$$$$$$$$$  SKIPPING THE GRID STATUS VERIFICATION AS THE EXECUTIONS ARE NOT GRID BASED  $$$$$$$$$$$$$$$$$$$$$$");
        }
    }
}
