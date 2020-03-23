package stepdefinitions;

import base.TestBase;

import helpers.Logfile;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks extends TestBase {
    @Before
    public void setUp(Scenario scenario) throws Exception{
        testName.set(scenario.getName() + "");
        setScenario(scenario);
        Logfile.writeToTestLog(testName.get());
        launchNewBrowserInstance();
    }

    @After
    public void tearDown(Scenario scenario){
        if(scenario.isFailed()){
            try{
                captureScreenshotAndAttachToReport();
            }catch (Exception e){
                scenario.write(e.getMessage());
            }
        }
        browserClose();
    }

}
