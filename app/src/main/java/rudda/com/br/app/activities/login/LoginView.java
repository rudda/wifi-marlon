package rudda.com.br.app.activities.login;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by Rudda Beltrao on 25/03/2018.
 */

public interface LoginView {

    public void showMessage(String message);
    public void loginSucess(GoogleSignInAccount account);
    public void doLoginWithGoogle();
}
