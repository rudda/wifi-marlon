package rudda.com.br.app.activities.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;



import rudda.com.br.app.R;
import rudda.com.br.app.domain.app.User;

public class HomeActivity extends AppCompatActivity implements HomeView{

    private HomePresenter presenter;
    private Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.tb =  (Toolbar) findViewById(R.id.tb);
        presenter = new HomePresenter();

    }


    @Override
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void setUserOfDrawer(GoogleSignInAccount account) {

        User u = new User();
        u.setUserEmail(account.getEmail());
        u.setUserName(account.getDisplayName());
        u.setUserProfile(account.getPhotoUrl().toString());
        Log.i("HOME", u.getUserProfile());
        Log.i("HOME", account.getPhotoUrl().toString());


        presenter.initDrawerLeft(u, this, this.tb);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.unregisterEventBus();

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerEventBus();
    }

    @Override
    public void registerEventBus() {

        EventBus.getDefault().register(this);

    }

    @Override
    public void unregisterEventBus() {

        EventBus.getDefault().unregister(this);


    }
}
