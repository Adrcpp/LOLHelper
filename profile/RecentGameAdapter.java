package com.example.adrien.lolhelper.profile;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adrien.lolhelper.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Adrien CESARO on 22/02/2016.
 */
public class RecentGameAdapter extends ArrayAdapter{

    public RecentGameAdapter(Context context, List<RecentGame> recentGames){
        super(context, 0 ,recentGames);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recentgame_list, viewGroup, false);
        }

        RecentGameViewHolder rgvh = (RecentGameViewHolder) convertView.getTag();

        if(rgvh == null){
            rgvh = new RecentGameViewHolder();

            rgvh.champ = (ImageView) convertView.findViewById(R.id.rg_champ);

            rgvh.item1 = (ImageView) convertView.findViewById(R.id.rg_1);
            rgvh.item2 = (ImageView) convertView.findViewById(R.id.rg_2);
            rgvh.item3 = (ImageView) convertView.findViewById(R.id.rg_3);
            rgvh.item4 = (ImageView) convertView.findViewById(R.id.rg_4);
            rgvh.item5 = (ImageView) convertView.findViewById(R.id.rg_5);
            rgvh.item6 = (ImageView) convertView.findViewById(R.id.rg_6);

            rgvh.trinket = (ImageView) convertView.findViewById(R.id.rg_trinket);

            rgvh.sum1 = (ImageView) convertView.findViewById(R.id.rg_sum1);
            rgvh.sum2 = (ImageView) convertView.findViewById(R.id.rg_sum2);

            rgvh.colorresult = (ImageView) convertView.findViewById(R.id.rg_colorresult);
            rgvh.time = (TextView) convertView.findViewById(R.id.rg_time);
            rgvh.since = (TextView) convertView.findViewById(R.id.rg_since);
            rgvh.cs = (TextView) convertView.findViewById(R.id.rg_creep);
            rgvh.gold = (TextView) convertView.findViewById(R.id.rg_gold);
            rgvh.kda = (TextView) convertView.findViewById(R.id.rg_kda);
            rgvh.gameType = (TextView) convertView.findViewById(R.id.rg_type);
        }

        RecentGame rg = (RecentGame) getItem(position);

        if(rg.getbChamp()!=null) {
            rgvh.champ.setImageBitmap(rg.getbChamp());
        }else{
            rgvh.champ.setImageResource(R.drawable.blank);
        }
        if(rg.getbItem0()!=null) {
            rgvh.item1.setImageBitmap(rg.getbItem0());
        }else{
            rgvh.item1.setImageResource(R.drawable.blank);
        }
        if(rg.getbItem1()!=null) {
            rgvh.item2.setImageBitmap(rg.getbItem1());
        }else{
            rgvh.item2.setImageResource(R.drawable.blank);
        }
        if(rg.getbItem2()!=null) {
            rgvh.item3.setImageBitmap(rg.getbItem2());
        }else{
            rgvh.item3.setImageResource(R.drawable.blank);
        }
        if(rg.getbItem3()!=null) {
            rgvh.item4.setImageBitmap(rg.getbItem3());
        }else{
            rgvh.item4.setImageResource(R.drawable.blank);
        }
        if(rg.getbItem4()!=null) {
            rgvh.item5.setImageBitmap(rg.getbItem4());
        }else{
            rgvh.item5.setImageResource(R.drawable.blank);
        }
        if(rg.getbItem5()!=null) {
            rgvh.item6.setImageBitmap(rg.getbItem5());
        }else{
            rgvh.item6.setImageResource(R.drawable.blank);
        }
        if(rg.getbTrinket()!=null) {
            rgvh.trinket.setImageBitmap(rg.getbTrinket());
        }else{
            rgvh.trinket.setImageResource(R.drawable.blank);
        }

        if(rg.getbSpell0()!=null) {
            rgvh.sum1.setImageBitmap(rg.getbSpell0());
        }else{
            rgvh.sum1.setImageResource(R.drawable.blank);
        }
        if(rg.getbSpell1()!=null) {
            rgvh.sum2.setImageBitmap(rg.getbSpell1());
        }else{
            rgvh.sum2.setImageResource(R.drawable.blank);
        }


        //rgvh.time.setText(rg.createdate);
        rgvh.gameType.setText(rg.subtype);
        rgvh.cs.setText("" + rg.minionsKilled + "cs");

        DecimalFormat df = new DecimalFormat("###.#");

        rgvh.gold.setText(df.format(rg.goldEarned/1000)+"K");

        rgvh.kda.setText(rg.kill + "K/ " + rg.death + "D/ " + rg.assist + "A");
        if(rg.isWin()){
            rgvh.colorresult.setBackgroundColor(Color.rgb(0,200,20));

        }else {
            rgvh.colorresult.setBackgroundColor(Color.RED);
        }
        long epoch = rg.createdate;
        int epoch2 = (int) System.currentTimeMillis()/1000;
        String date = new java.text.SimpleDateFormat("MM/dd/yy HH:mm").format(new java.util.Date (epoch));

        rgvh.since.setText(date);
        rgvh.time.setText(String.format("" + rg.duration / 60, 00)+"min");

        return convertView;

    }


    public  class RecentGameViewHolder{

        public ImageView champ;
        public ImageView sum1;
        public ImageView sum2;

        public ImageView item1;
        public ImageView item2;
        public ImageView item3;
        public ImageView item4;
        public ImageView item5;
        public ImageView item6;
        public ImageView trinket;

        public TextView gameType;
        public TextView kda;
        public TextView cs;
        public TextView gold;
        public TextView time;
        public TextView since;
        public ImageView colorresult;

    }

}
