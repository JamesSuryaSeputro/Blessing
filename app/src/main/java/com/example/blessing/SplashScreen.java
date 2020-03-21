package com.example.blessing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {
//        implements EasyPermissions.PermissionCallbacks {
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        openStorage();
        lottieAnimationView = findViewById(R.id.animation_view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //langsung berpindah ke login
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2400);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lottieAnimationView.pauseAnimation();
    }
}
//    private void handlerpost(){


//    @AfterPermissionGranted(123)
//    private void openStorage() {
//        String perms = Manifest.permission.READ_EXTERNAL_STORAGE;
//        if (EasyPermissions.hasPermissions(this, perms)) {
//            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
//            handlerpost();
//        } else {
//            EasyPermissions.requestPermissions(this, "Permission needed to run the App", 123, perms);
//            //
//        }
//    }
//
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 123){
//
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//        handlerpost();
//        }else {
//            handlerpost();
//        }
//    }
//
//    @Override
//    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
//        if(requestCode == 123 ){
//            handlerpost();
//        }
//    }
//
//    @Override
//    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, Collections.singletonList(Manifest.permission.READ_EXTERNAL_STORAGE))) {
//            new AppSettingsDialog.Builder(this).build().show();
//            handlerpost();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
//
//        }
//    }
