package pw.fabi.crewmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
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
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


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

            class ReplaceIt implements Runnable {
                private boolean result;
                @Override
                public void run() {
                    try  {
                        result =  new FileHandler().openFile(Environment.getExternalStorageDirectory().toString() + "/Android/data/com.innersloth.spacemafia/files/regionInfo.dat")
                                .replaceFile("https://skeld.net/setup/regionInfo.dat");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public boolean getResult() {
                    return result;
                }
            }

            ReplaceIt replaceit = new ReplaceIt();
            Thread thread = new Thread(replaceit);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean result = replaceit.getResult();


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
        });
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
