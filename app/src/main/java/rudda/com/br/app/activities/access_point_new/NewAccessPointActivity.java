package rudda.com.br.app.activities.access_point_new;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rudda.com.br.app.R;
import rudda.com.br.app.domain.AccessPoint;
import rudda.com.br.app.domain.Anuncio;

import static android.os.Environment.DIRECTORY_PICTURES;

public class NewAccessPointActivity extends AppCompatActivity implements NewAccessPointView, NewAccessPointPresenter.AccessPointPresenterSaveListener{

    private NewAccessPointPresenter presenter;
    private ImageView imageView;
    private List<AccessPoint> current_wifi_list;
    private List<String> wifi_names_spinner;


    private Anuncio mAnuncio;


    //ui
    private Spinner wifi_spinner;
    private EditText nome, senha_wifi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_access_point);

        imageView = (ImageView) findViewById(R.id.imageView) ;
        mAnuncio = new Anuncio();


        this.nome = (EditText) findViewById(R.id.nome);
        this.senha_wifi = (EditText)findViewById(R.id.senha_wifi);

        this.wifi_spinner = (Spinner) findViewById(R.id.spinner_wifi);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                startActivityForResult(Intent.createChooser(i, "Selecione uma imagem"), 9);

            }
        });

        requestPermissions();
        presenter = new NewAccessPointPresenter(this, this);

        this.presenter.loadAccessPoints(this, this);

    }

    @Override
    public void requestPermissions() {

        requireWifiPermission(this, 1);
    }

    @Override
    public String uploadFile(File file) {


        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode==9 && resultCode == RESULT_OK) {
            Uri photoData = data.getData();
            Bitmap photoBitmap = null;
            try {
                photoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoData);
                imageView.setImageBitmap(photoBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    public void onCadastro(View v){

        //desconectar tudo
        //conectar ao wifi selecionado
        //quando a conexao estiver ok
        //entao faz o upload da imagem p firebase
        //quando o upload estiver ok entao
        //salva no banco de dados firebase :)

        if(this.mAnuncio!= null){

            this.mAnuncio.getAp().setPreSharedKey(this.senha_wifi.getText().toString());

            this.mAnuncio.setName(this.nome.getText().toString());
            this.mAnuncio.setDescription("criado em "+new Date().getTime() +"@beltrao");
            this.presenter.tryToConnectWifi(this.mAnuncio.getAp(), this);


        }else{


        }


    }


    public int checkWifiPermission(Context context){

        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);



        return  permissionCheck;

    }

    public void requireWifiPermission(Activity activity, int requestCode){

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CHANGE_WIFI_STATE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,  Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        requestCode);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {


                }
                return;
            }


        }
    }


    @Override
    public void onUploadImageSucess(String src) {

        this.mAnuncio.setPhoto(src);
        this.saveInRealDatabase(this.mAnuncio);

    }

    @Override
    public void onWifiChecked(AccessPoint ap) {

        //retirar


    }



    @Override
    public void saveInRealDatabase(Anuncio anuncio) {

        this.presenter.salveInRealDatabase(anuncio);

    }

    @Override
    public void onScanWifiResult(List<AccessPoint> acs) {

        this.current_wifi_list =  acs;
        this.wifi_names_spinner = new ArrayList<>();




        for (AccessPoint accessPoint : this.current_wifi_list) {

            String ssid = accessPoint.getSSID();
            this.wifi_names_spinner.add(ssid);

            Log.i("WIFI", ssid);


        }

        ArrayAdapter<String> karant_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, this.wifi_names_spinner);

        this.wifi_spinner.setAdapter(karant_adapter);

        this.wifi_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(NewAccessPointActivity.this, "selecionei "+current_wifi_list.get(position).getSSID(), Toast.LENGTH_SHORT).show();
                mAnuncio.setAp( current_wifi_list.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                mAnuncio.setAp(null);
                Toast.makeText(NewAccessPointActivity.this, "nenhum wifi selecionado", Toast.LENGTH_SHORT).show();

            }
        });
        
        
    }


    private boolean firstTime = true;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(firstTime) {
                firstTime = false;
                return;
            }

            if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo networkInfo =
                        intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if(networkInfo.isConnected() ) {

                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

                  //  if(wifiManager.getConnectionInfo().getSSID().equals(mAnuncio.getAp().getSSID())   ){

                        //faz upload da imagem p firebase

                        presenter.upload(imageView, getApplicationContext(), "png");


                  //  }else{

                     //   Toast.makeText(context, "informe uma senha valida :(", Toast.LENGTH_SHORT).show();

                   // }

                }

            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(this.receiver, new IntentFilter("android.net.wifi.STATE_CHANGE"));

    }

}
