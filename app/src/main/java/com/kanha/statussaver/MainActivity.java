package com.kanha.statussaver;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kanha.statussaver.Adapters.TabsPagerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;

    public static int tabCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);

        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(adapter);

        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("Statuses");
        tabs.add("Saved");

        tabCount = tabLayout.getTabCount();

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            for (int i = 0; i < tabs.size(); i++)
                if (position == i)
                    tab.setText(tabs.get(i));
        }).attach();

    }

    public boolean isPermissionGranted() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int readExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            return readExternalStoragePermission == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!isPermissionGranted()){
            startActivity(new Intent(MainActivity.this,SplashScreen.class));
        }
    }
}