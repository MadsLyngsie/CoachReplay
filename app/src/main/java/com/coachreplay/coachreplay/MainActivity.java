package com.coachreplay.coachreplay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static int CAMERA_PERMISSION_CODE = 100;
    private static int VIDEO_RECORD_CODE = 101;
    BottomNavigationView navigationView;

    private Uri videoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this line line hide actionbar
        //getSupportActionBar().hide();

        //this line hide status bar
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        navigationView = findViewById(R.id.bottom_navigator);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container_menu,new HomeFragment()).commit();
        navigationView.setSelectedItemId(R.id.nav_home);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.nav_settings:
                        fragment = new SettingsFragment();
                        break;

                    case R.id.nav_TNC:
                        fragment = new TNCFragment();
                        break;

                    case R.id.nav_smile:
                        fragment = new xDFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.body_container_menu,fragment).commit();

                return true;
            }
        });

        if(isCameraPresent()){
            Log.i("VIDEO_RECORD_TAG","camera is detected");
            getCameraPermission();
        }
        else{
            Log.i("VIDEO_RECORD_TAG", "No camera is not detected");
        }
    }

    public void recordVideoButtonPress(View view){
        recordVideo();
    }

    private boolean isCameraPresent(){
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        }
        else{
            return false;
        }
    }

    private void getCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE );
        }
    }

    private void recordVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO_RECORD_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO_RECORD_CODE) {

            if (resultCode == RESULT_OK) {

                videoPath = data.getData();

                Log.i("VIDEO_RECORD_TAG","video is recorded" + videoPath);
            }
            else if (resultCode == RESULT_CANCELED) {

                Log.i("VIDEO_RECORD_TAG", "video recording was canceled");
            }
            else{
                Log.i("VIDEO_RECORD_TAG", "Error 1");
            }
        }

    }


}
