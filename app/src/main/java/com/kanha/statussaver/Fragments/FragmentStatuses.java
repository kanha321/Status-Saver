package com.kanha.statussaver.Fragments;

import static android.os.Build.VERSION.SDK_INT;

import static com.kanha.statussaver.Util.Resources.*;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kanha.statussaver.Adapters.StatusesAdapter;
import com.kanha.statussaver.R;
import com.kanha.statussaver.SplashScreen;

import java.io.File;
import java.util.ArrayList;

public class FragmentStatuses extends Fragment {

    private static final String TAG = "FragmentStatuses";

    View view;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView images;
    ArrayList<File> media;
    File dir;

    public FragmentStatuses() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_statuses, container, false);

        images = view.findViewById(R.id.saved_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getMedia();
            swipeRefreshLayout.setRefreshing(false);
        });

        if (SDK_INT >= Build.VERSION_CODES.R)
            dir = STATUS_DIR_11;
        else
            dir = STATUS_DIR_10;

        if (SplashScreen.permissionGranted) {
            getMedia();
        } else {
            Log.d(TAG, "onCreateView: Permission not granted");
        }

        return view;
    }


    public void getMedia() {
        media = imageReader(dir);
        GridLayoutManager layoutManager;
        if (this.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getContext(), 3);
        } else {
            layoutManager = new GridLayoutManager(getContext(), 6);
        }
        images.setLayoutManager(layoutManager);
        images.setAdapter(new StatusesAdapter(getContext(), imageReader(dir)));
    }

    private ArrayList<File> imageReader(File externalStorageDirectory) {
        ArrayList<File> media = new ArrayList<>();
        File[] files = externalStorageDirectory.listFiles();
        ArrayList<String> fileName = new ArrayList<>();
//        assert files != null;
        if (files != null) {
            for (File value : files) {
                if (value.getName().endsWith(".jpg") && !value.getName().equals(".nomedia"))
                    media.add(value);
            }
            Log.d(TAG, "imageReader: size after adding images " + media.size());
            for (File file : files) {
                if (file.getName().endsWith(".mp4") && !file.getName().equals(".nomedia"))
                    media.add(file);
            }
            Log.d(TAG, "imageReader: size after adding videos " + media.size());
            for (File file : files) {
                fileName.add(file.getName());
            }
//            for (int i = 0; i < media.size(); i++) {
//                Log.d(TAG, "imageReader: b " + media.get(i) + "\n");
//            }
        }
        Log.d(TAG, "imageReader: filenames " + fileName);
        Log.d(TAG, "imageReader: b size " + media.size());
        try {
//            assert files != null;
            Log.d(TAG, "imageReader: files.length " + files.length);
        } catch (Exception e) {
            Log.e(TAG, "imageReader: ", e);
        }
        return media;
    }
}