package rudda.com.br.app.activities.home;

import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

import rudda.com.br.app.domain.Anuncio;

/**
 * Created by Rudda Beltrao on 25/03/2018.
 */

public interface HomeView {

    public void  setUserOfDrawer(GoogleSignInAccount account);
    public void registerEventBus();
    public void unregisterEventBus();
    public void addAccessPoint(View view);
    public void updateListOfAnuncios(List<Anuncio> anuncios);





}
