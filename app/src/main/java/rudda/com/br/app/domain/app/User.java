package rudda.com.br.app.domain.app;

/**
 * Created by Rudda Beltrao on 25/03/2018.
 */

public class User {

    private String userName;
    private String UserEmail;
    private String UserProfile;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserProfile() {
        return UserProfile;
    }

    public void setUserProfile(String userProfile) {
        UserProfile = userProfile;
    }
}
