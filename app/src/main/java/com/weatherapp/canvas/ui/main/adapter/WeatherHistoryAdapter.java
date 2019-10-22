package com.weatherapp.canvas.ui.main.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.weatherapp.canvas.R;
import com.weatherapp.canvas.callback.OnHistoryItemListener;
import com.weatherapp.canvas.data.local.model.WeatherHistoryItem;
import com.weatherapp.canvas.databinding.DayWeatherListItemBinding;
import com.weatherapp.canvas.util.FileHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WeatherHistoryAdapter extends RecyclerView.Adapter<WeatherHistoryAdapter.WeatherHistoryViewHolder> {
    private List<WeatherHistoryItem> mainHistoryList = new ArrayList<>();
    private DayWeatherListItemBinding binding;
    private OnHistoryItemListener listener;

    public WeatherHistoryAdapter(OnHistoryItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeatherHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.day_weather_list_item,parent,false);
        return new WeatherHistoryViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHistoryViewHolder holder, int position) {
        holder.bind(mainHistoryList.get(position));
    }

    public void add(File file){
        if(file.length()>0)
        mainHistoryList.add(new WeatherHistoryItem(file,"Cairo",file.getName()));
        notifyItemInserted(mainHistoryList.size());
    }


    public void addAll(final File [] files){
        ArrayList<WeatherHistoryItem> weatherHistoryItems = new ArrayList<>();
        for (File file : files){
            if(file.length() >0 )
            weatherHistoryItems.add(new WeatherHistoryItem(file,"Cairo",file.getName()));
        }
        mainHistoryList.addAll(weatherHistoryItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return  mainHistoryList.size();
    }

    class WeatherHistoryViewHolder extends RecyclerView.ViewHolder{

        public WeatherHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void bind(WeatherHistoryItem weatherHistoryItem){
            int h = 100; // height in pixels
            int w = 100; // width in pixels
            Bitmap scaled = Bitmap.createScaledBitmap(FileHelper.createBitmapFromFile(weatherHistoryItem.getFile().getPath()), h, w, true);
            binding.imageView.setImageBitmap(scaled);
            binding.dateCreated.setText(weatherHistoryItem.getDateCreated());
            itemView.setOnClickListener(v -> listener.onClick(weatherHistoryItem.getFile()));
        }
    }

}
