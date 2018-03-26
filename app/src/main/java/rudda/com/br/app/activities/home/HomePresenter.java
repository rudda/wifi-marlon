package rudda.com.br.app.activities.home;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rudda.com.br.app.R;
import rudda.com.br.app.domain.AccessPoint;
import rudda.com.br.app.domain.Anuncio;
import rudda.com.br.app.domain.app.User;

/**
 * Created by Rudda Beltrao on 25/03/2018.
 */

public class HomePresenter implements HomeModal.onHomeModal {

    private HomeModal modal;
    private List<AccessPoint> mAccessPointList;
    private Drawer drawer   ;

    private List<Anuncio> anuncioList;
    private HomeView view;

    public HomePresenter(HomeView view) {

        this.mAccessPointList = new ArrayList<>();
        this.anuncioList = new ArrayList<>();

        this.view = view;

        modal = new HomeModal(this);

    }


    public void initDrawerLeft(User u, Activity a, Toolbar tb){

      drawer = new DrawerBuilder()
                .withActivity(a)
                .withToolbar(tb)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home"),
                        new PrimaryDrawerItem().withName("sair")

                )
              .withAccountHeader(this.getAccountHeaderForUser(a, u))
                .build();



    }

    private AccountHeader getAccountHeaderForUser(Activity a, User u){

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(a)
                .withHeaderBackground(R.color.primary)
                .addProfiles(
                        new ProfileDrawerItem().withName(u.getUserName()).withEmail(u.getUserEmail()).withIcon(Uri.parse(u.getUserProfile()))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();


        return headerResult;
    }


    @Override
    public void onLoadAnunciosAdd(Anuncio anuncio) {

        this.anuncioList.add(anuncio);
        view.updateListOfAnuncios(this.anuncioList);

    }
}
