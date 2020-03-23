package stepdefinitions;

import helpers.LoadProperties;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modules.PageFactory;

public class SignInSignOutContactUsSteps extends PageFactory {

    @When("I open automationpractice website")
    public void i_open_automationpractice_website() {
        LaunchApplicationAction().navigateToApplication(LoadProperties.getProperty("ui.url"));
    }

    @Then("I sign in with credentials {string} and {string}")
    public void I_sign_in_with_credentials(String email, String password) {
        SiginInSignOutAction().signIn(email, password);
    }

    @Then("I sign out")
    public void i_sign_out() {
       SiginInSignOutAction().signout();
    }

    @Then("I perform contact us actions by passing {string} and {string}")
    public void i_perform_contact_us_actions_by_passing_and(String email, String message) {
        ContactUs().sendMessageWithError(email, message);
    }
}
