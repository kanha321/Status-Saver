package com.kanha.statussaver;

import static com.kanha.statussaver.Adapters.StatusesAdapter.selectedImages;
import static com.kanha.statussaver.Adapters.StatusesAdapter.selectedVideos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import com.kanha.statussaver.Adapters.ExtendedViewPagerAdapter;
import com.kanha.statussaver.Adapters.StatusesAdapter;
import com.kanha.statussaver.Customs.CustomPagerAdapter;
import com.kanha.statussaver.Customs.ExtendedViewPager;
import com.kanha.statussaver.Fragments.FragmentSaved;
import com.kanha.statussaver.Util.MediaFile;
import com.kanha.statussaver.Util.MyToast;
import com.kanha.statussaver.Util.Resources;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MediaViewerActivity extends AppCompatActivity {

    private static final String TAG = "MediaViewerActivity";
    ViewPager viewPager;
    ImageView share, download;
    int itemIndex;
    ArrayList<File> files;
    int activity;

    ExtendedViewPager extendedViewPager;
    public static boolean fileDeleted = false;
    public static boolean fileDownloaded = false;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_viewer);

        extendedViewPager = findViewById(R.id.extendedViewPager);

        share = findViewById(R.id.share);
        download = findViewById(R.id.download);

        getExtras();
        Log.d(TAG, "getExtras: activity " + activity);
        Log.d(TAG, "getExtras: arraylist index 0" + files.get(itemIndex).getName());

        extendedViewPager.setAdapter(new ExtendedViewPagerAdapter(this, files, activity));
        extendedViewPager.setCurrentItem(itemIndex, false);

        share.setOnClickListener(view ->
                share(extendedViewPager.getCurrentItem()));

        if (activity == Resources.SAVED) {
            download.setImageDrawable(getResources().getDrawable(R.drawable.ic_round_delete_outline_24));
            download.setOnClickListener(view ->
                    delete(extendedViewPager.getCurrentItem()));
        } else {
            download.setImageDrawable(getResources().getDrawable(R.drawable.ic_outline_file_download_24));
            download.setOnClickListener(view ->
                    download(extendedViewPager.getCurrentItem()));
        }
    }

    private void getExtras() {
        if (getIntent().getExtras() != null) {
            files = (ArrayList<File>) getIntent().getSerializableExtra("files");
            activity = getIntent().getIntExtra("type", 0);
            itemIndex = getIntent().getIntExtra("index", 0);

            Log.d(TAG, "getExtras: activity " + activity);
        }
    }


    private void share(int position) {
        Uri path = Uri.parse(files.get(position).getPath());
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (files.get(position).getName().endsWith(".mp4"))
            intent.setType("video/*");
        else if (files.get(position).getName().endsWith(".jpg"))
            intent.setType("image/*");
        else MyToast.showToast(this, "Unsupported file type");
        intent.putExtra(Intent.EXTRA_STREAM, path);
        intent.putExtra(Intent.EXTRA_TEXT, "custom text");
        startActivity(Intent.createChooser(intent, "Share Status Using"));
    }

    private void download(int position) {
        fileDownloaded = true;
        Log.d(TAG, "download: position " + position);
        Log.d(TAG, "download: file size " + files.size());
        File src = new File(files.get(position).toString());
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss_SSS");
            String date = simpleDateFormat.format(System.currentTimeMillis());
            Log.d(TAG, "onBindViewHolder: date " + date);
            if (files.get(position).getName().endsWith(".jpg")) {
                if (MediaFile.copy(src, date + ".jpg") == -1) {
                    MyToast.showToast(this, "Image download failed");
                } else {
                    MyToast.showToast(this, "Image downloaded successfully");
                }
            } else if (files.get(position).getName().endsWith(".mp4")) {
                if (MediaFile.copy(src, date + ".mp4") == -1) {
                    MyToast.showToast(this, "Video download failed");
                } else {
                    MyToast.showToast(this, "Video downloaded successfully");
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "onBindViewHolder: copy Error " + e);
        }
    }


//    private void delete(int position) {
//        File file = files.get(position);
//        if (file.delete()) {
//            MyToast.showToast(this, "File deleted successfully");
//            files.remove(position);
//        } else {
//            MyToast.showToast(this, "Fail to delete file");
//        }
//    }

//    private void download(int position) {
//        File src = new File(files.get(position).toString());
//        try {
//            @SuppressLint("SimpleDateFormat")
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss_SSS");
//            String date = simpleDateFormat.format(System.currentTimeMillis());
//            Log.d(TAG, "onBindViewHolder: date " + date);
//            if (files.get(position).getName().endsWith(".jpg")) {
//                if (MediaFile.copy(src, date + ".jpg") == -1) {
//                    MyToast.showToast(this, "Image download failed");
//                } else {
//                    MyToast.showToast(this, "Image downloaded successfully");
//                }
//            } else if (files.get(position).getName().endsWith(".mp4")) {
//                if (MediaFile.copy(src, date + ".mp4") == -1) {
//                    MyToast.showToast(this, "Video download failed");
//                } else {
//                    MyToast.showToast(this, "Video downloaded successfully");
//                }
//            }
//        } catch (IOException e) {
//            Log.d(TAG, "onBindViewHolder: copy Error " + e);
//        }
//
//        if (files.get(position).getName().endsWith(".jpg")) {
//            selectedImages.add(files.get(position));
////            FragmentSaved.initialImageCount++;
////            FragmentSaved.totalMediaCount++;
//            Log.d(TAG, "download: selected images " + selectedImages.size());
//            for (int i = 0; i < selectedImages.size(); i++) {
//                Log.d(TAG, "download: selected images no: " + i + " " + selectedImages.get(i).getName());
//            }
//        } else if (files.get(position).getName().endsWith(".mp4")) {
//            selectedVideos.add(files.get(position));
////            FragmentSaved.initialVideoCount++;
////            FragmentSaved.totalMediaCount++;
//            Log.d(TAG, "download: selected videos " + selectedVideos.size());
//            for (int i = 0; i < selectedVideos.size(); i++) {
//                Log.d(TAG, "download: selected videos no: " + i + " " + selectedVideos.get(i).getName());
//            }
//        }
//    }

    private void delete(int position) {
        File file = files.get(position);
        String extension = files.get(position).getName().substring(files.get(position).getName().lastIndexOf("."));
        Log.d(TAG, "delete: extension = " + extension);
        if (file.delete()) {
            if (extension.equals(".jpg")) {
                FragmentSaved.initialImageCount--;
                MyToast.showToast(this, "Image deleted successfully");
            } else {
                FragmentSaved.initialVideoCount--;
                MyToast.showToast(this, "Video deleted successfully");
            }
            files.remove(position);
//            ExtendedViewPagerAdapter extendedViewPagerAdapter = new ExtendedViewPagerAdapter(this, files, activity);
            extendedViewPager.setAdapter(new ExtendedViewPagerAdapter(this, files, activity));


            if (position != files.size() - 1) {
                extendedViewPager.setCurrentItem(position, true);
            } else {
                extendedViewPager.setCurrentItem(position - 1, true);
            }

            fileDeleted = true;
        } else {
            MyToast.showToast(this, "Fail to delete file, try again after refreshing");
        }
    }
}