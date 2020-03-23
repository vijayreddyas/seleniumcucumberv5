package modules;

import pages.HomePage;
import pages.LoginPage;

public class SiginInSignOutAction extends PageFactory{

    /**
     *
     * @param userName
     * @param password
     */
    public void signIn(String userName, String password){
        click(LoginPage.signin_link);
        type(LoginPage.email, userName);
        type(LoginPage.password, password);
        click(LoginPage.signin_button);
    }

    /**
     *
     */
    public void signout(){
        click(HomePage.signOut);
    }

}
