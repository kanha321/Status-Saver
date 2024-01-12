package com.kanha.statussaver.Customs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.kanha.statussaver.PlayerActivity;
import com.kanha.statussaver.R;

import java.io.File;
import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

    private static final String TAG = "CustomPagerAdapter";
    private final Context mContext;
    private ArrayList<File> files;
    private ViewPager mViewPager;

    private int activity;

    public CustomPagerAdapter(Context context, ArrayList<File> imageUrls, ViewPager viewPager, int activity) {
        mContext = context;
        files = imageUrls;
        mViewPager = viewPager;
        this.activity = activity;
        mViewPager.addOnPageChangeListener(this);
    }

    public void setItems(ArrayList<File> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate your layout for the ViewPager item here
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.single_viewpager_image, container, false);

        // Set up the contents of the ViewPager item here
        TouchImageView imageView = view.findViewById(R.id.imageView);
        Toolbar toolbar = view.findViewById(R.id.toolbar2);
        ImageView playButton = view.findViewById(R.id.playbutton2);

        Glide.with(mContext).load(files.get(position)).into(imageView);

        Log.d(TAG, "instantiateItem: position = " + position);
        Glide.with(mContext).load(Uri.fromFile(files.get(position))).into(imageView);
        imageView.setViewPager(((Activity) mContext).findViewById(R.id.extendedViewPager));
        toolbar.setTitle(files.get(position).getName());

        if (files.get(position).getName().endsWith(".mp4")) {
            playButton.setVisibility(View.VISIBLE);
        } else {
            playButton.setVisibility(View.GONE);
        }

        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PlayerActivity.class);
            intent.putExtra("path", files.get(position).getPath());
            mContext.startActivity(intent);
        });

        Log.d(TAG, "instantiateItem: title " + files.get(position).getName());
        

        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View)object;
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.delete_item_animation);
        view.startAnimation(animation);
        new Handler().postDelayed(() -> {
            container.removeView(view);
            files.remove(position);
            notifyDataSetChanged();
        }, animation.getDuration());
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {}
}

