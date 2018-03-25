package rudda.com.br.app.activities.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.EventBus;

import rudda.com.br.app.R;
import rudda.com.br.app.activities.home.HomeActivity;

public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener{


    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(this, this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LoginPresenter.GOOGLE_SIGIN_REQUEST_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            presenter.handleSignInResult(task);
        }
    }

    @Override
    public void showMessage(String message) {


        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void loginSucess(GoogleSignInAccount account) {

        EventBus.getDefault().postSticky(account);
        Intent it = new Intent(this, HomeActivity.class);
        startActivity(it);
        finish();

    }

    @Override
    public void doLoginWithGoogle() {

        presenter.dologinWithGoogle(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_in_button:

               this.doLoginWithGoogle();

               break;

            default:
                  break;
        }

    }
}
