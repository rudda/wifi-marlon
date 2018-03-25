package rudda.com.br.app.activities.home;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by Rudda Beltrao on 25/03/2018.
 */

public interface HomeView {

    public void  setUserOfDrawer(GoogleSignInAccount account);
    public void registerEventBus();
    public void unregisterEventBus();





}
