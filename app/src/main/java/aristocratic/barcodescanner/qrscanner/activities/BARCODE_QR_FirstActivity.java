package aristocratic.barcodescanner.qrscanner.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import aristocratic.barcodescanner.qrscanner.R;

public class BARCODE_QR_FirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_qr_activity_first);

        if (BARCODE_QR_SplashActivity.mInterstitialAd!= null){
            BARCODE_QR_SplashActivity.mInterstitialAd.show(this);
        }

        loadBannerAd();

        findViewById(R.id.img_start_scanning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                    return;
                }

                Intent intent = new Intent(BARCODE_QR_FirstActivity.this, BARCODE_QR_MainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.img_scanned_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                    return;
                }
                Intent intent = new Intent(BARCODE_QR_FirstActivity.this, BARCODE_QR_MainActivity.class);
                intent.putExtra("history", true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void loadBannerAd() {
        LinearLayout linearAd = findViewById(R.id.linearAd);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(getResources().getString(R.string.bannerAdId));

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        linearAd.addView(adView);
    }
}
