package com.kanha.statussaver.Fragments;

import static com.kanha.statussaver.Adapters.StatusesAdapter.*;
import static com.kanha.statussaver.Util.Resources.*;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kanha.statussaver.Adapters.SavedAdapter;
import com.kanha.statussaver.MediaViewerActivity;
import com.kanha.statussaver.R;

import java.io.File;
import java.util.ArrayList;

public class FragmentSaved extends Fragment {

    private static final String TAG = "FragmentSaved";
    View view;
    RecyclerView images;
    SwipeRefreshLayout swipeRefreshLayout;


    ArrayList<File> savedMedia;
    SavedAdapter savedAdapter;
    public static int initialImageCount = 0;
    public static int initialVideoCount = 0;
    public static int totalMediaCount = 0;

    static boolean isFirstTime = true;
    static boolean isFirstTimeResumed = true;

    public FragmentSaved() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_statuses, container, false);
        images = view.findViewById(R.id.saved_recycler_view);

        if (isFirstTime) {
            loadEverything();
        }

        swipeRefreshLayout = view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadEverything();
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    private void loadEverything() {
        savedMedia = new ArrayList<>(imageReader());
        savedAdapter = new SavedAdapter(getContext(), savedMedia);
        if (STATUS_SAVER_DIR.exists()) getMedia();
        else if (STATUS_SAVER_DIR.mkdirs()) getMedia();
        else Log.e(TAG, "onCreateView: failed to create directory");
    }

    private void getMedia() {
        Log.d(TAG, "getMedia: called");
        GridLayoutManager layoutManager;
        if (this.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            layoutManager = new GridLayoutManager(getContext(), 3);
        else
            layoutManager = new GridLayoutManager(getContext(), 6);
        images.setLayoutManager(layoutManager);
        images.setAdapter(savedAdapter);
    }

    private ArrayList<File> imageReader(){
        ArrayList<File> b = new ArrayList<>();
        File[] files = STATUS_SAVER_DIR.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().endsWith(".jpg") && !file.getName().equals(".nomedia"))
                b.add(file);
        }
        initialImageCount = b.size();
        for (File file : files) {
            if (file.getName().endsWith(".mp4") && !file.getName().equals(".nomedia"))
                b.add(file);
        }
        totalMediaCount = b.size();
        initialVideoCount = totalMediaCount - initialImageCount;
        ArrayList<String> fileName = new ArrayList<>();
        for (File file : files) {
            fileName.add(file.getName());
        }
        for (int i = 0; i < b.size(); i++) {
            Log.d(TAG, "imageReader: b " + b.get(i) + "\n");
        }
        Log.d(TAG, "imageReader: filenames " + fileName);
        Log.d(TAG, "imageReader: b size " + b.size());
        Log.d(TAG, "imageReader: files.length " + files.length);
        return b;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstTime) {
            Log.d(TAG, "onResume: called");

            Log.d(TAG, "onResume: selectedImages " + selectedImages.size());
            Log.d(TAG, "onResume: selectedVideos " + selectedVideos.size());
            try {
                Log.d(TAG, "onResume: savedMedia " + savedMedia.size());
            } catch (Exception e) {
                Log.e(TAG, "onResume: savedMedia Error: " + e.getMessage());
            }
            Log.d(TAG, "onResume: initialImageCount " + initialImageCount);
            Log.d(TAG, "onResume: initialVideoCount " + initialVideoCount);
            Log.d(TAG, "onResume: totalMediaCount " + totalMediaCount);

            if (selectedVideos.size() > 0) {
                Log.d(TAG, "onResume: number of new videos found " + selectedVideos.size());
                savedMedia.addAll(selectedVideos);
                savedAdapter.notifyItemRangeInserted(totalMediaCount, selectedVideos.size());
                totalMediaCount += selectedVideos.size();
                selectedVideos.clear();
                Log.d(TAG, "onResume: selectedVideos cleared");
            }

            if (selectedImages.size() > 0) {
                Log.d(TAG, "onResume: number of new images found " + selectedImages.size());
                savedMedia.addAll(initialImageCount, selectedImages);
                savedAdapter.notifyItemRangeInserted(initialImageCount, selectedImages.size());
                initialImageCount += selectedImages.size();
                selectedImages.clear();
                Log.d(TAG, "onResume: selectedImages cleared");
            }
            totalMediaCount = savedMedia.size();
            Log.d(TAG, "onResume: new totalMediaCount " + totalMediaCount);

            Log.d(TAG, "onResume: itemCount " + savedAdapter.getItemCount());

            if (savedAdapter.getItemCount() == 0) {
                Log.d(TAG, "onResume: no media found");
                if (STATUS_SAVER_DIR.exists()) {
                    Log.d(TAG, "onResume: directory exists");
                    loadEverything();
                } else if (STATUS_SAVER_DIR.mkdirs()) {
                    Log.d(TAG, "onResume: directory created");
                    loadEverything();
                } else {
                    Log.e(TAG, "onResume: failed to create directory");
                }
            }
            if (MediaViewerActivity.fileDeleted) {
                MediaViewerActivity.fileDeleted = false;
                loadEverything();
            }
            if (MediaViewerActivity.fileDownloaded) {
                MediaViewerActivity.fileDownloaded = false;
                loadEverything();
            }
        }
        isFirstTime = false;
        if (isFirstTimeResumed) {
            loadEverything();
            isFirstTimeResumed = false;
        }
    }


}