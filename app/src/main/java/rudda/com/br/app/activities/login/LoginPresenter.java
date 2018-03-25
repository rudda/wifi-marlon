package rudda.com.br.app.activities.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * Created by Rudda Beltrao on 25/03/2018.
 */

public class LoginPresenter {

    private  GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    public static final int GOOGLE_SIGIN_REQUEST_CODE = 111;
    private LoginView view;
    private Context context;

    public LoginPresenter(LoginView view, Context context) {
        this.view = view;
        this.context = context;
        this.init(context);
    }

    private void init(Context context){

        this.gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public boolean checkUserIsLogedFromGoogle(){

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.context);

        return account.getAccount()== null ? false : true;

    }

    public void dologinWithGoogle(Activity activity){

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, GOOGLE_SIGIN_REQUEST_CODE);

    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            view.loginSucess(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
           view.showMessage("Erro ao efetuar login");
        }
    }


    public GoogleSignInOptions getGso() {
        return gso;
    }

    public void setGso(GoogleSignInOptions gso) {
        this.gso = gso;
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public void setmGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
    }

    public LoginView getView() {
        return view;
    }

    public void setView(LoginView view) {
        this.view = view;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
