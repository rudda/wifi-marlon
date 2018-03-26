package rudda.com.br.app.activities.home;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rudda.com.br.app.domain.Anuncio;

/**
 * Created by Rudda Beltrao on 25/03/2018.
 */

public class HomeModal {

    private DatabaseReference mDatabase;
    private List<Anuncio> anuncioList;

    //addListenerForSingleValueEvent()
    public interface onHomeModal{

        public  void onLoadAnunciosAdd(Anuncio anuncio);

    }


    private onHomeModal listener;

    public HomeModal(final onHomeModal listener) {

        this.listener = listener;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        this.anuncioList = new ArrayList<>();

        Query myTopPostsQuery = mDatabase.child("anuncios")
                .orderByChild("starCount");
        myTopPostsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                try {
                    Anuncio a = dataSnapshot.getValue(Anuncio.class);
                    Log.i("HOME", "onChildAdded " + a.getAp().getSSID());

                    if(a!= null)
                    listener.onLoadAnunciosAdd(a);


                }catch (Exception e){

                    Log.i("HOME", "erro ao adicionar anuncio a lista");

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("HOME", "onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i("HOME", "onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.i("HOME", "onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("HOME", "onCancelled");
            }

        });

    }


}
