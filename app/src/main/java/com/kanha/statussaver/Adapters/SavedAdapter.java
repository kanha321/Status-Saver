package com.kanha.statussaver.Adapters;

import static com.kanha.statussaver.Util.Resources.SAVED;
import static com.kanha.statussaver.Util.Resources.STATUSES;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kanha.statussaver.Fragments.FragmentSaved;
import com.kanha.statussaver.MediaViewerActivity;
import com.kanha.statussaver.R;
import com.kanha.statussaver.Util.MediaFile;
import com.kanha.statussaver.Util.MyToast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ViewHolder> {

    private static final String TAG = "MediaAdapter";
    private Context context;
    ArrayList<File> files;
    RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.dummy_image);

    public SavedAdapter(Context context, ArrayList<File> files) {
        this.context = context;
        this.files = files;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, playButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.grid_image);
            playButton = itemView.findViewById(R.id.playbutton);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.image.setImageURI(Uri.parse(files.get(position).toString()));
        if (files.get(position).getName().endsWith(".mp4")) {
            holder.playButton.setVisibility(View.VISIBLE);
        } else {
            holder.playButton.setVisibility(View.GONE);
        }
        Glide.with(context).load(files.get(position))
                .apply(requestOptions)
                .into(holder.image);
        holder.itemView.setOnClickListener(v -> {

            File file = new File(files.get(holder.getBindingAdapterPosition()).getPath());
            long length = file.length();
//            Toast.makeText(context, "file size = " + length, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onBindViewHolder: file size = " + length);
//            MediaViewerActivity.activity = SAVED;
//            MediaViewerActivity.media = files;
            Intent intent = new Intent(context, MediaViewerActivity.class);
            intent.putExtra("type", SAVED);
            intent.putExtra("files", files);
            intent.putExtra("index", holder.getBindingAdapterPosition());
            Log.d(TAG, "onBindViewHolder: arraylist " + files);
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            delete(holder);
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    private void delete(ViewHolder holder) {
        File file = files.get(holder.getBindingAdapterPosition());
        String extension = files.get(holder.getBindingAdapterPosition()).getName().substring(files.get(holder.getBindingAdapterPosition()).getName().lastIndexOf("."));
        Log.d(TAG, "delete: extension = " + extension);
        if (file.delete()) {
            if (extension.equals(".jpg")) {
                FragmentSaved.initialImageCount--;
                MyToast.showToast(context, "Image deleted successfully");
            } else {
                FragmentSaved.initialVideoCount--;
                MyToast.showToast(context, "Video deleted successfully");
            }
            files.remove(holder.getBindingAdapterPosition());
            notifyItemRemoved(holder.getBindingAdapterPosition());
        } else {
            MyToast.showToast(context, "Fail to delete file, Refresh and try again.");
        }
    }

    private void download(ViewHolder holder) {
        File src = new File(files.get(holder.getBindingAdapterPosition()).toString());
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss_SSS");
            String date = simpleDateFormat.format(System.currentTimeMillis());
            Log.d(TAG, "onBindViewHolder: date " + date);
            if (files.get(holder.getBindingAdapterPosition()).getName().endsWith(".jpg")) {
                if (MediaFile.copy(src, date + ".jpg") == -1) {
                    MyToast.showToast(context, "Image download failed");
                } else {
                    MyToast.showToast(context, "Image downloaded successfully");
                }
            } else if (files.get(holder.getBindingAdapterPosition()).getName().endsWith(".mp4")) {
                if (MediaFile.copy(src, date + ".mp4") == -1) {
                    MyToast.showToast(context, "Video download failed");
                } else {
                    MyToast.showToast(context, "Video downloaded successfully");
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "onBindViewHolder: copy Error " + e);
        }
    }
}
