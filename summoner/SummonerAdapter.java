package com.example.adrien.lolhelper.summoner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adrien.lolhelper.R;

import java.util.List;

/**
 * Created by Adrien on 03/02/2016.
 */
public class SummonerAdapter extends ArrayAdapter<Summoner> {

    public SummonerAdapter(Context context, List<Summoner> summoners){
        super(context,0, summoners);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.summoner_listview, parent, false);
        }

        SummonerViewHolder svh = (SummonerViewHolder) convertView.getTag();

        if(svh == null){
            svh = new SummonerViewHolder();
            svh.icon = (ImageView) convertView.findViewById(R.id.icon);
            svh.summoner = (TextView) convertView.findViewById(R.id.summoner1);
            svh.level = (TextView) convertView.findViewById(R.id.level1);
        }

        Summoner summ = getItem(position);

        svh.icon.setImageBitmap(summ.getIconImage());
        svh.summoner.setText(summ.getSummName());
        svh.level.setText("Level: " + summ.getLevelString());

        return convertView;
    }

    private  class SummonerViewHolder{
        public TextView summoner;
        public TextView level;
        public ImageView icon;
    }

}
