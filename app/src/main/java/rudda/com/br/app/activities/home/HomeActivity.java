package rudda.com.br.app.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.util.ArrayList;
import java.util.List;

import rudda.com.br.app.R;
import rudda.com.br.app.activities.access_point_new.NewAccessPointActivity;
import rudda.com.br.app.adapter.AnunciosAdapter;
import rudda.com.br.app.domain.AccessPoint;
import rudda.com.br.app.domain.Anuncio;
import rudda.com.br.app.domain.app.User;

public class HomeActivity extends AppCompatActivity implements HomeView{

    private HomePresenter presenter;
    private HomeModal modal;
    private Toolbar tb;

    private List<Anuncio> anuncioList;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager llm;
    private AnunciosAdapter adp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.tb =  (Toolbar) findViewById(R.id.tb);
        presenter = new HomePresenter(this);

        mRecyclerView  = (RecyclerView) findViewById(R.id.recyclerView);
        this.llm = new LinearLayoutManager(this);
        this.llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        this.anuncioList = new ArrayList<>();

        adp = new AnunciosAdapter(this.anuncioList, this, new AnunciosAdapter.onWifiClick() {
            @Override
            public void wifiConnect(AccessPoint w) {

                Toast.makeText(HomeActivity.this, "aqui voce pode chamar para conectar wifi "+w.getSSID()+" com a senha "+w.getPreSharedKey(), Toast.LENGTH_SHORT).show();

            }
        });




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

        try {
            EventBus.getDefault().register(this);
        }catch (Exception Err){


        }

    }

    @Override
    public void unregisterEventBus() {

        try {
            EventBus.getDefault().unregister(this);
        }catch (Exception e){

        }

    }

    @Override
    public void addAccessPoint(View view) {

        Intent it = new Intent(this, NewAccessPointActivity.class);
        startActivity(it);

    }

    @Override
    public void updateListOfAnuncios(List<Anuncio> anuncios) {
        this.anuncioList = new ArrayList<>();
        this.anuncioList = anuncios;
        Log.i("HOME", "anuncios "+this.anuncioList.size());
        this.llm = new LinearLayoutManager(this);
        this.llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);


        adp = new AnunciosAdapter(this.anuncioList, this, new AnunciosAdapter.onWifiClick() {
            @Override
            public void wifiConnect(AccessPoint w) {

                Toast.makeText(HomeActivity.this, "aqui voce pode chamar para conectar wifi "+w.getSSID()+" com a senha "+w.getPreSharedKey(), Toast.LENGTH_SHORT).show();

            }
        });
        mRecyclerView.setAdapter(adp);

        adp.notifyDataSetChanged();

    }
}
