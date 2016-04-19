package com.example.adrien.lolhelper.ingame;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adrien.lolhelper.R;

import java.util.List;

/**
 * Created by Adrien CESARO on 13/02/2016.
 */
public class InGameAdapter extends ArrayAdapter{

    public InGameAdapter(Context context, List<IngameInfo> summoners){
        super(context, 0, summoners);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ingame_listview, parent, false);
        }

        IngameViewHolder svh = (IngameViewHolder) convertView.getTag();

        if(svh == null){
            svh = new IngameViewHolder();
            svh.icon = (ImageView) convertView.findViewById(R.id.championIngame);
            svh.summoner = (TextView) convertView.findViewById(R.id.summonerIngame);
            svh.champStats = (TextView) convertView.findViewById(R.id.champStats);
            svh.ig_rank = (ImageView) convertView.findViewById(R.id.ig_rank);
            svh.ig_ranktxt = (TextView) convertView.findViewById(R.id.ig_ranktxt);
            svh.ig_win = (TextView) convertView.findViewById(R.id.ig_win);
            svh.couleur = (RelativeLayout) convertView.findViewById(R.id.couleur);
            svh.runes = (TextView) convertView.findViewById(R.id.runes);
            svh.spell1 = (ImageView) convertView.findViewById(R.id.ig_spell1);
            svh.spell2 = (ImageView) convertView.findViewById(R.id.ig_spell2);
            svh.keystone = (ImageView) convertView.findViewById(R.id.ig_keystone);
            svh.masteries = (TextView) convertView.findViewById(R.id.ig_mastery);
        }

        IngameInfo summ = (IngameInfo) getItem(position);

        svh.spell1.setImageBitmap(summ.getbSpell0());
        svh.spell2.setImageBitmap(summ.getbSpell1());

        svh.runes.setText(summ.getRunes());
        svh.masteries.setText(summ.getKeystoneDes()+"\n"+summ.getMasteries());
        svh.keystone.setImageBitmap(summ.getbKeystone());

        int[] tab = summ.getChampStat();

        svh.icon.setImageBitmap(summ.getImageChampion());
        svh.summoner.setText(summ.getSummName() +"("+tab[0]+")");
        if(tab[0]!=0) {
            svh.champStats.setText("KDA: " + tab[3] / tab[0] + "/" + tab[4] / tab[0] + "/" + tab[5] / tab[0] + "\nCS: " + tab[6] / tab[0] + "  W: " + tab[1]);
        }else{
            svh.champStats.setText("KDA: " + tab[3]  + "/" + tab[4] + "/" + tab[5] + "\n CS: " + tab[6] + "  W: " + tab[1]);
        }
        svh.ig_ranktxt.setText(summ.getDivision().replace("_"," ").toUpperCase());
        svh.ig_win.setText("W:"+summ.getWins()+"\nL:"+summ.getLosses());
        if(summ.getIdTeam()==100){
            svh.couleur.setBackgroundColor(Color.BLUE);
        }else{
            svh.couleur.setBackgroundColor(Color.RED);
        }


        if(summ.getDivision().equals("bronze_v")){
            svh.ig_rank.setImageResource(R.drawable.bronze_v);
        }else
        if(summ.getDivision().equals("bronze_iv")){
            svh.ig_rank.setImageResource(R.drawable.bronze_iv);
        }else
        if(summ.getDivision().equals("bronze_iii")){
            svh.ig_rank.setImageResource(R.drawable.bronze_iii);
        }else
        if(summ.getDivision().equals("bronze_ii")){
            svh.ig_rank.setImageResource(R.drawable.bronze_ii);
        }else
        if(summ.getDivision().equals("bronze_i")){
            svh.ig_rank.setImageResource(R.drawable.bronze_i);
        }else
        if(summ.getDivision().equals("silver_v")){
            svh.ig_rank.setImageResource(R.drawable.silver_v);
        }else
        if(summ.getDivision().equals("silver_iv")){
            svh.ig_rank.setImageResource(R.drawable.silver_iv);
        }else
        if(summ.getDivision().equals("silver_iii")){
            svh.ig_rank.setImageResource(R.drawable.silver_iii);
        }else
        if(summ.getDivision().equals("silver_ii")){
            svh.ig_rank.setImageResource(R.drawable.silver_ii);
        }else
        if(summ.getDivision().equals("silver_i")){
            svh.ig_rank.setImageResource(R.drawable.silver_i);
        }else
        if(summ.getDivision().equals("gold_v")){
            svh.ig_rank.setImageResource(R.drawable.gold_v);
        }else
        if(summ.getDivision().equals("gold_iv")){
            svh.ig_rank.setImageResource(R.drawable.gold_iv);
        }else
        if(summ.getDivision().equals("gold_iii")){
            svh.ig_rank.setImageResource(R.drawable.gold_iii);
        }else
        if(summ.getDivision().equals("gold_ii")){
            svh.ig_rank.setImageResource(R.drawable.gold_ii);
        }else
        if(summ.getDivision().equals("gold_i")){
            svh.ig_rank.setImageResource(R.drawable.gold_i);
        }else
        if(summ.getDivision().equals("platinum_v")){
            svh.ig_rank.setImageResource(R.drawable.platinum_v);
        }else
        if(summ.getDivision().equals("platinum_iv")){
            svh.ig_rank.setImageResource(R.drawable.platinum_iv);
        }else
        if(summ.getDivision().equals("platinum_iii")){
            svh.ig_rank.setImageResource(R.drawable.platinum_iii);
        }else
        if(summ.getDivision().equals("platinum_ii")){
            svh.ig_rank.setImageResource(R.drawable.platinum_ii);
        }else
        if(summ.getDivision().equals("platinum_i")){
            svh.ig_rank.setImageResource(R.drawable.platinum_i);
        }else
        if(summ.getDivision().equals("diamond_v")){
            svh.ig_rank.setImageResource(R.drawable.diamond_v);
        }else
        if(summ.getDivision().equals("diamond_iv")){
            svh.ig_rank.setImageResource(R.drawable.diamond_iv);
        }else
        if(summ.getDivision().equals("diamond_iii")){
            svh.ig_rank.setImageResource(R.drawable.diamond_iii);
        }else
        if(summ.getDivision().equals("diamond_ii")){
            svh.ig_rank.setImageResource(R.drawable.diamond_ii);
        }else
        if(summ.getDivision().equals("diamond_i")){
            svh.ig_rank.setImageResource(R.drawable.diamond_i);
        }else
        if(summ.getDivision().equals("master_i")){
            svh.ig_rank.setImageResource(R.drawable.master);
        }else
        if(summ.getDivision().equals("challenger_i")){
            svh.ig_rank.setImageResource(R.drawable.challenger);
        }else{
            svh.ig_rank.setImageResource(R.drawable.provisional);
        }


        return convertView;
    }


    private  class IngameViewHolder{
        public TextView summoner;
        public TextView champStats;
        public ImageView icon;
        public ImageView ig_rank;
        public TextView ig_ranktxt;
        public TextView ig_win;
        public RelativeLayout couleur;
        public TextView runes;
        public ImageView spell1;
        public ImageView spell2;
        public ImageView keystone;
        public TextView masteries;
    }
}
