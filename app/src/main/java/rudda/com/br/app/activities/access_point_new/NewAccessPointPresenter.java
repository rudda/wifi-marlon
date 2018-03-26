package rudda.com.br.app.activities.access_point_new;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rudda.com.br.app.domain.AccessPoint;
import rudda.com.br.app.domain.Anuncio;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Rudda Beltrao on 25/03/2018.
 */

public class NewAccessPointPresenter {

    private DatabaseReference mDatabase;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imgRef;

    private Context com;

    public interface AccessPointPresenterSaveListener{

        public void onUploadImageSucess(String src);
        public void onWifiChecked(AccessPoint ap);
        public void saveInRealDatabase(Anuncio anuncio);
        public void onScanWifiResult(List<AccessPoint> acs);


    }

    private AccessPointPresenterSaveListener listener;

    public NewAccessPointPresenter(final Context com, AccessPointPresenterSaveListener listener) {

        this.listener = listener;

        FirebaseApp.initializeApp(com);
        this.storage  = FirebaseStorage.getInstance();
        this.storageRef = this.storage.getReference();
        this.imgRef = this.storageRef.child("imgs");
        this.com = com;

        mDatabase = FirebaseDatabase.getInstance().getReference();

       mDatabase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               Toast.makeText(
                       com, "Data Changed "+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

               Toast.makeText(
                       com, "Data  Error"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();


           }
       });
    }


    public void upload(ImageView imageView, final Context com, String fileType){

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] dataa = baos.toByteArray();

        StorageReference imgUploadRef = this.imgRef.child(new Date().getTime()+"."+fileType);

        UploadTask uploadTask = imgUploadRef.putBytes(dataa);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(com, "Deu error "+exception.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("UPLOAD", exception.getMessage());



            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(com, "uploaded "+downloadUrl, Toast.LENGTH_SHORT).show();
                Log.i("UPLOAD", downloadUrl.toString());

                listener.onUploadImageSucess(downloadUrl.toString());

            }
        });

    }


    public void loadAccessPoints(Context context, Activity activity ) {

            final WifiManager mWifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            final List<AccessPoint> Aps = new ArrayList<>();

                if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {

                    // register WiFi scan results receiver
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

                    context.registerReceiver(new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            List<ScanResult> results = mWifiManager.getScanResults();

                            results = mWifiManager.getScanResults();
                            final int N = results.size();

                            String TAG = "MYWIFI";
                            Log.v(TAG, "Wi-Fi Scan Results ... Count:" + N);
                            for (int i = 0; i < N; ++i) {

                                Log.v(TAG, "  BSSID       =" + results.get(i).BSSID);
                                Log.v(TAG, "  SSID        =" + results.get(i).SSID);
                                Log.v(TAG, "  Capabilities=" + results.get(i).capabilities);
                                Log.v(TAG, "  Frequency   =" + results.get(i).frequency);
                                Log.v(TAG, "  Level       =" + results.get(i).level);
                                Log.v(TAG, "---------------");

                                AccessPoint w = new AccessPoint();
                                w.setSSID(results.get(i).SSID);
                                w.setBSSID(results.get(i).BSSID);

                                Aps.add(w);

                            }

                            listener.onScanWifiResult(Aps);
                        }
                    }, filter);

                    // start WiFi Scan
                    mWifiManager.startScan();
                }

    }

    public void tryToConnectWifi(AccessPoint w, Context com){

        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.BSSID = w.getBSSID();
        wifiConfiguration.SSID = "\"" + w.getSSID() + "\"";
        wifiConfiguration.preSharedKey = "\"" +w.getPreSharedKey()+"\"";

        //Conecto na nova rede criada.
        WifiManager wifiManager = (WifiManager) com.getSystemService(WIFI_SERVICE);
        int netId = wifiManager.addNetwork(wifiConfiguration);
        wifiManager.saveConfiguration();
        wifiManager.disconnect();
        wifiManager.reconnect();
        wifiManager.enableNetwork(netId, true);


        ConnectivityManager connManager = (ConnectivityManager) com.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


     }


    public void salveInRealDatabase(Anuncio a){

        Toast.makeText(this.com, "chamei salvar", Toast.LENGTH_SHORT).show();
        mDatabase.child("anuncios").child(a.getId()).setValue(a);


    }





}
