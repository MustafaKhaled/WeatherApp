package com.weatherapp.canvas.ui.main.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.weatherapp.canvas.R;
import com.weatherapp.canvas.callback.OnHistoryItemListener;
import com.weatherapp.canvas.data.local.model.WeatherHistoryItem;
import com.weatherapp.canvas.util.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherHistoryAdapter extends RecyclerView.Adapter<WeatherHistoryAdapter.WeatherHistoryViewHolder> {
    private List<WeatherHistoryItem> mainHistoryList = new ArrayList<>();
    private OnHistoryItemListener listener;
    public WeatherHistoryAdapter(OnHistoryItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeatherHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.day_weather_list_item,parent,false);
        return new WeatherHistoryViewHolder(view);
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
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.date_created)
        TextView dateCreated;
        public WeatherHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        private void bind(WeatherHistoryItem weatherHistoryItem){
            ((MyApplication)itemView.getContext().getApplicationContext())
                    .getImageLoader().displayImage(Uri.fromFile(weatherHistoryItem.getFile()).toString(),imageView);
            dateCreated.setText(weatherHistoryItem.getDateCreated());
            itemView.setOnClickListener(v -> listener.onClick(FileProvider.getUriForFile(itemView.getContext(),
                    "com.weather.canvas.fileprovider",weatherHistoryItem.getFile()
                    )));
        }
    }

}
