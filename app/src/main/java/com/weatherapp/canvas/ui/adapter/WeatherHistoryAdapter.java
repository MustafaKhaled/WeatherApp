package com.weatherapp.canvas.ui.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.weatherapp.canvas.R;
import com.weatherapp.canvas.data.local.model.WeatherHistoryItem;
import com.weatherapp.canvas.databinding.DayWeatherListItemBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WeatherHistoryAdapter extends RecyclerView.Adapter<WeatherHistoryAdapter.WeatherHistoryViewHolder> {
    private List<WeatherHistoryItem> mainHistoryList = new ArrayList<>();
    private DayWeatherListItemBinding binding;
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
        mainHistoryList.add(new WeatherHistoryItem(file,"Cairo",file.getName()));
        notifyDataSetChanged();
    }


    public void addAll(final File [] files){
        ArrayList<WeatherHistoryItem> weatherHistoryItems = new ArrayList<>();
        for (File file : files){
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
            binding.imageView.setImageURI(Uri.fromFile(weatherHistoryItem.getImageUri()));
            binding.dateCreated.setText(weatherHistoryItem.getDateCreated());
        }
    }

}
