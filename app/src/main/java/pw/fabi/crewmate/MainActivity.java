package pw.fabi.crewmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    boolean hasPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final TextView desc = (TextView) findViewById(R.id.textView4);
        desc.setText(Html.fromHtml("Skeld.net is the world&apos;s first custom Among Us server. It has custom features such as Discord integration, custom gamemodes, a proper anticheat, and more. Come join the <a href=\"https://skeld.net/discord\">Discord</a> server and if you love it, support it on <a href=\"https://www.patreon.com/skeld_net\">Patreon"));
        desc.setMovementMethod(LinkMovementMethod.getInstance());
        Button btn = (Button)findViewById(R.id.Swbutton);
        hasPermission = requestFilePermission();

        btn.setOnClickListener(v -> {
            String addr = "44.238.242.123"; //((EditText) findViewById(R.id.editTextTextAddr)).getText().toString();
            if(!addr.startsWith("http://") & !addr.startsWith("https://")){ // make sure address starts with http://
                addr = "http://" + addr;
            }
            String ipAddr = new IPHandler().getIp(addr,MainActivity.this);
            final String port = "22023"; //((EditText) findViewById(R.id.editTextTextPort)).getText().toString();
            if(port.length() < 2) {
                Toast.makeText(MainActivity.this,
                        "Please type a Port",
                        Toast.LENGTH_LONG)
                        .show();
                return;
            }
            if(ipAddr != null){
                switchIpAddr(ipAddr.split("/")[1], port);
            }
        });
    }


    public void switchIpAddr(String addr, String port){

        short shortPort = Short.parseShort(port);

        boolean result = new FileHandler().openFile(Environment.getExternalStorageDirectory().toString() + "/Android/data/com.innersloth.prophunt/files/regionInfo.dat")
                .replaceFile("skeld.net", addr, shortPort);

        if(result){
            Toast.makeText(MainActivity.this,
                    "Server changed successfully, (re)start the game",
                    Toast.LENGTH_LONG)
                    .show();
        }
        else{
            Toast.makeText(MainActivity.this,
                    "Error, try to grant permissions (this app doesn't work in android 11)",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    public boolean requestFilePermission(){

        boolean r = false;
        boolean c = false;
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            r = false;
        }
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            c = false;
        }

        String[] permissionArray = new String[2];
        if(!r || !c){
            permissionArray[0] =  Manifest.permission.WRITE_EXTERNAL_STORAGE;
            permissionArray[1] =  Manifest.permission.READ_EXTERNAL_STORAGE;
            ActivityCompat.requestPermissions(MainActivity.this,
                    permissionArray,
                    0);
            return false;
        }
        else{
            return true;
        }
    }
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPermission = true;
            }
            else {
                Toast.makeText(MainActivity.this,
                        "App requires file permission to access Among Us files",
                        Toast.LENGTH_SHORT)
                        .show();
                hasPermission = false;
            }
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
