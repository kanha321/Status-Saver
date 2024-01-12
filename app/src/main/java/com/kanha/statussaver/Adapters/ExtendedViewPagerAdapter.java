package com.kanha.statussaver.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.kanha.statussaver.Customs.TouchImageView;
import com.kanha.statussaver.PlayerActivity;
import com.kanha.statussaver.R;
import com.kanha.statussaver.Util.MediaFile;
import com.kanha.statussaver.Util.MyToast;
import com.kanha.statussaver.Util.Resources;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExtendedViewPagerAdapter extends PagerAdapter {

    private static final String TAG = "ExtendedViewPagerAdapter";

    private Context context;
    public static ArrayList<File> files;
    private int activity;

    public ExtendedViewPagerAdapter(Context context, ArrayList<File> files, int activity) {
        this.context = context;
        ExtendedViewPagerAdapter.files = files;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_viewpager_image, container, false);

        TouchImageView imageView = view.findViewById(R.id.imageView);
        Toolbar toolbar = view.findViewById(R.id.toolbar2);
        ImageView playButton = view.findViewById(R.id.playbutton2);

        Log.d(TAG, "instantiateItem: position = " + position);
        Glide.with(context).load(Uri.fromFile(files.get(position))).into(imageView);
        imageView.setViewPager(((Activity) context).findViewById(R.id.extendedViewPager));
        toolbar.setTitle(files.get(position).getName());

        if (files.get(position).getName().endsWith(".mp4")) {
            playButton.setVisibility(View.VISIBLE);
        } else {
            playButton.setVisibility(View.GONE);
        }

        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("path", files.get(position).getPath());
            context.startActivity(intent);
        });

        Log.d(TAG, "instantiateItem: title " + files.get(position).getName());




//        download.setOnClickListener(view1 -> download(position - 1));

//        share.setOnClickListener(v -> {
//            Uri path = Uri.parse(files.get(position - 1).getPath());
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            if (files.get(position - 1).getName().endsWith(".mp4"))
//                intent.setType("video/*");
//            else if (files.get(position - 1).getName().endsWith(".jpg"))
//                intent.setType("image/*");
//            intent.putExtra(Intent.EXTRA_STREAM, path);
//            intent.putExtra(Intent.EXTRA_TEXT, "custom text");
//            context.startActivity(Intent.createChooser(intent, "Share Status Using"));
//        });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
    }

//    private void delete(int position) {
//        File file = files.get(position);
//        if (file.delete()) {
//            MyToast.showToast(context, "File deleted successfully");
//            files.remove(position);
//        } else {
//            MyToast.showToast(context, "Fail to delete file");
//        }
//    }

//    public void download(int position) {
//        Log.d(TAG, "download: position " + position);
//        Log.d(TAG, "download: file size " + files.size());
//        File src = new File(files.get(position).toString());
//        try {
//            @SuppressLint("SimpleDateFormat")
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss_SSS");
//            String date = simpleDateFormat.format(System.currentTimeMillis());
//            Log.d(TAG, "onBindViewHolder: date " + date);
//            if (files.get(position).getName().endsWith(".jpg")) {
//                if (MediaFile.copy(src, date + ".jpg") == -1) {
//                    MyToast.showToast(context, "Image download failed");
//                } else {
//                    MyToast.showToast(context, "Image downloaded successfully");
//                }
//            } else if (files.get(position).getName().endsWith(".mp4")) {
//                if (MediaFile.copy(src, date + ".mp4") == -1) {
//                    MyToast.showToast(context, "Video download failed");
//                } else {
//                    MyToast.showToast(context, "Video downloaded successfully");
//                }
//            }
//        } catch (IOException e) {
//            Log.d(TAG, "onBindViewHolder: copy Error " + e);
//        }
//    }
}
