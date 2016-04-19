package com.example.adrien.lolhelper.profile;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adrien.lolhelper.util.ConnectionThread;
import com.example.adrien.lolhelper.R;
import com.example.adrien.lolhelper.summoner.Summoner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by Adrien CESARO on 15/02/2016.
 */
public class ProfileRanked extends Fragment  {
    private final String TAG = "ProfileRanked";

    TextView test;
    TextView level;
    ImageView icon;

    ImageView rang;
    TextView rang_txt;
    TextView winlose;

    TextView kda;

    // TOP 5 CHAMP
    TextView txt1;
    TextView txt2;
    TextView txt3;
    TextView txt4;
    TextView txt5;
    TextView LP;

    ImageView topChamp1;
    ImageView topChamp2;
    ImageView topChamp3;
    ImageView topChamp4;
    ImageView topChamp5;


    // Test onSavedInstanceState
    String result;
    String result2;
    String result3;
    String result4;

    Summoner sum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getContext(), "Ranked @ onCreate", Toast.LENGTH_SHORT).show();
        //Recuperation des info Extra de l'intent, dans un Bundle
        Bundle extras = getActivity().getIntent().getExtras();
        Log.i(TAG, "ProfileRanked summoner:" + extras.getInt("id"));
        sum = new Summoner(extras.getString("summonername"), extras.getInt("icon"), extras.getInt("level"), extras.getInt("id"));

        if(savedInstanceState != null){
            //Reloaded fragment
            Toast.makeText(getContext(), "Ranked Reloaded", Toast.LENGTH_SHORT).show();

            result = savedInstanceState.getString("r");
            result2 = savedInstanceState.getString("r2");
            result3 = savedInstanceState.getString("r3");
            result4 = savedInstanceState.getString("r4");

        }else{
             result = ConnectionThread.ConnectionThreadLeague(sum.getId());
             result2 = ConnectionThread.ConnectionThreadStatsSummary(sum.getId());
             result3 = ConnectionThread.ConnectionThreadChampMastery(sum.getId());
             result4 = ConnectionThread.ConnectionThreadStatsChamp(sum.getId());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstances){
        Log.i(TAG, "ProfileRanked");
        View view = inflater.inflate(R.layout.profile_layout, container, false);

        // API Summoner
        test = (TextView) view.findViewById(R.id.test);
        level =(TextView) view.findViewById(R.id.level);
        icon = (ImageView)view.findViewById(R.id.icon);

        // API League:
        rang = (ImageView) view.findViewById(R.id.rang);
        rang_txt = (TextView) view.findViewById(R.id.rang_txt);
        winlose = (TextView) view.findViewById(R.id.winlose);
        LP = (TextView) view.findViewById(R.id.LP);

        kda = (TextView) view.findViewById(R.id.kda);

        // TOP 5 CHAMP
        txt1 = (TextView) view.findViewById(R.id.txt1);
        txt2 = (TextView) view.findViewById(R.id.txt2);
        txt3 = (TextView) view.findViewById(R.id.txt3);
        txt4 = (TextView) view.findViewById(R.id.txt4);
        txt5 = (TextView) view.findViewById(R.id.txt5);

        topChamp1 = (ImageView) view.findViewById(R.id.topChamp1);
        topChamp2 = (ImageView) view.findViewById(R.id.topChamp2);
        topChamp3 = (ImageView) view.findViewById(R.id.topChamp3);
        topChamp4 = (ImageView) view.findViewById(R.id.topChamp4);
        topChamp5 = (ImageView) view.findViewById(R.id.topChamp5);

        test.setText(sum.getSummName());
        level.setText("Level: " + sum.getLevelString());
        icon.setImageBitmap(sum.getIconImage());

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(getContext(), "Ranked @ Saved", Toast.LENGTH_SHORT).show();
        outState.putString("r", result);
        outState.putString("r2", result2);
        outState.putString("r3", result3);
        outState.putString("r4", result4);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        JSONObject obj = null;
        try {
            JSONArray topChamp = new JSONArray(result3);
            JSONObject obj2 = new JSONObject(result4);

            obj = new JSONObject(result);
            JSONArray jsonArray = (JSONArray) obj.get("" + sum.getId());  //
            JSONObject js2 = jsonArray.getJSONObject(0);                //
            JSONArray entries = js2.getJSONArray("entries");            //
            JSONObject element = (JSONObject) entries.get(0);           //

            double totalplay = element.getInt("wins") + element.getInt("losses");

            rang_txt.setText(js2.getString("tier") + " " + element.getString("division"));
            winlose.setText("Wins: " + element.getInt("wins") + " Losses:" + element.getInt("losses"));
            LP.setText(element.getInt("leaguePoints") + " LP");
            String iconRank = js2.getString("tier").toLowerCase() + "_" + element.getString("division").toLowerCase();
            setIconRank(iconRank);

            // Traitement Json

            obj = new JSONObject(result2);
            JSONArray jsonArray2 = obj.getJSONArray("playerStatSummaries");  // Ensemble
            // = Objet  ranked solo 5x5 dont win lose et totale assit kill mais pas death :/

            JSONObject aggregatedStats = null; // L'objet aggregatedStts du Ranked 5v5

            for (int i = 0; i < jsonArray2.length(); ++i) {
                JSONObject js3 = (JSONObject) jsonArray2.get(i);
                if (js3.getString("playerStatSummaryType").equals("RankedSolo5x5")) {
                    aggregatedStats = js3.getJSONObject("aggregatedStats");
                }
            }

            double kill = aggregatedStats.getInt("totalChampionKills") / totalplay;
            double assist = aggregatedStats.getInt("totalAssists") / totalplay;
            DecimalFormat df = new DecimalFormat("########.0");

            kda.setText(df.format(kill) + " K/" + 0 + " D/ " + df.format(assist) + " A");

            /****************************************************************
             **      Champion Stats et champion mastery / TOP 5 Champions   **
             *****************************************************************/

            JSONArray allChampStats = obj2.getJSONArray("champions");

            for (int i = 0; i < topChamp.length(); ++i) {

                int id = topChamp.getJSONObject(i).optInt("championId");
                int[] cStats = new int[6];

                for (int j = 0; j < allChampStats.length(); ++j) {
                    if (id == allChampStats.getJSONObject(j).getInt("id")) {
                        //Champion trouvé dans toutes les stats des champions
                        //Recupére kda W/L cs ...

                        cStats[0] = allChampStats.getJSONObject(j).getJSONObject("stats").getInt("totalSessionsPlayed");            //Total played
                        cStats[1] = allChampStats.getJSONObject(j).getJSONObject("stats").getInt("totalSessionsWon");               //Won
                        cStats[2] = allChampStats.getJSONObject(j).getJSONObject("stats").getInt("totalChampionKills") / cStats[0];   //K
                        cStats[3] = allChampStats.getJSONObject(j).getJSONObject("stats").getInt("totalDeathsPerSession") / cStats[0];//D
                        cStats[4] = allChampStats.getJSONObject(j).getJSONObject("stats").getInt("totalAssists") / cStats[0];         //A
                        cStats[5] = allChampStats.getJSONObject(j).getJSONObject("stats").getInt("totalMinionKills") / cStats[0];     //cs

                        if (i == 0) {
                            topChamp1.setImageBitmap(ConnectionThread.ConnectionThreadChamp(id));
                            txt1.setText("Total played: " + cStats[0] + "\nW: " + cStats[1] + "\nKDA:" + cStats[2] + "/" + cStats[3] + "/" + cStats[4] + "\nCS: " + cStats[5]);
                        }
                        if (i == 1) {
                            topChamp2.setImageBitmap(ConnectionThread.ConnectionThreadChamp(id));
                            txt2.setText("Total played: " + cStats[0] + "\nW: " + cStats[1] + "\nKDA:" + cStats[2] + "/" + cStats[3] + "/" + cStats[4] + "\nCS: " + cStats[5]);
                        }
                        if (i == 2) {
                            topChamp3.setImageBitmap(ConnectionThread.ConnectionThreadChamp(id));
                            txt3.setText("Total played: " + cStats[0] + "\nW: " + cStats[1] + "\nKDA:" + cStats[2] + "/" + cStats[3] + "/" + cStats[4] + "\nCS: " + cStats[5]);
                        }
                        if (i == 3) {
                            topChamp4.setImageBitmap(ConnectionThread.ConnectionThreadChamp(id));
                            txt4.setText("Total played: " + cStats[0] + "\nW: " + cStats[1] + "\nKDA:" + cStats[2] + "/" + cStats[3] + "/" + cStats[4] + "\nCS: " + cStats[5]);
                        }
                        if (i == 4) {
                            topChamp5.setImageBitmap(ConnectionThread.ConnectionThreadChamp(id));
                            txt5.setText("Total played: " + cStats[0] + "\nW: " + cStats[1] + "\nKDA:" + cStats[2] + "/" + cStats[3] + "/" + cStats[4] + "\nCS: " + cStats[5]);
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(getContext(), "Ranked @ Paused", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getContext(), "Ranked @ Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(), "Ranked @ Destroyed", Toast.LENGTH_SHORT).show();
    }

    public void setIconRank(String iconRank){

        if(iconRank.equals("bronze_v")){
            rang.setImageResource(R.drawable.bronze_v);
        }else
        if(iconRank.equals("bronze_iv")){
            rang.setImageResource(R.drawable.bronze_iv);
        }else
        if(iconRank.equals("bronze_iii")){
            rang.setImageResource(R.drawable.bronze_iii);
        }else
        if(iconRank.equals("bronze_ii")){
            rang.setImageResource(R.drawable.bronze_ii);
        }else
        if(iconRank.equals("bronze_i")){
            rang.setImageResource(R.drawable.bronze_i);
        }else
        if(iconRank.equals("silver_v")){
            rang.setImageResource(R.drawable.silver_v);
        }else
        if(iconRank.equals("silver_iv")){
            rang.setImageResource(R.drawable.silver_iv);
        }else
        if(iconRank.equals("silver_iii")){
            rang.setImageResource(R.drawable.silver_iii);
        }else
        if(iconRank.equals("silver_ii")){
            rang.setImageResource(R.drawable.silver_ii);
        }else
        if(iconRank.equals("silver_i")){
            rang.setImageResource(R.drawable.silver_i);
        }else
        if(iconRank.equals("gold_v")){
            rang.setImageResource(R.drawable.gold_v);
        }else
        if(iconRank.equals("gold_iv")){
            rang.setImageResource(R.drawable.gold_iv);
        }else
        if(iconRank.equals("gold_iii")){
            rang.setImageResource(R.drawable.gold_iii);
        }else
        if(iconRank.equals("gold_ii")){
            rang.setImageResource(R.drawable.gold_ii);
        }else
        if(iconRank.equals("gold_i")){
            rang.setImageResource(R.drawable.gold_i);
        }else
        if(iconRank.equals("platinum_v")){
            rang.setImageResource(R.drawable.platinum_v);
        }else
        if(iconRank.equals("platinum_iv")){
            rang.setImageResource(R.drawable.platinum_iv);
        }else
        if(iconRank.equals("platinum_iii")){
            rang.setImageResource(R.drawable.platinum_iii);
        }else
        if(iconRank.equals("platinum_ii")){
            rang.setImageResource(R.drawable.platinum_ii);
        }else
        if(iconRank.equals("platinum_i")){
            rang.setImageResource(R.drawable.platinum_i);
        }else
        if(iconRank.equals("diamond_v")){
            rang.setImageResource(R.drawable.diamond_v);
        }else
        if(iconRank.equals("diamond_iv")){
            rang.setImageResource(R.drawable.diamond_iv);
        }else
        if(iconRank.equals("diamond_iii")){
            rang.setImageResource(R.drawable.diamond_iii);
        }else
        if(iconRank.equals("diamond_ii")){
            rang.setImageResource(R.drawable.diamond_ii);
        }else
        if(iconRank.equals("diamond_i")){
            rang.setImageResource(R.drawable.diamond_i);
        }else
        if(iconRank.equals("master_i")){
            rang.setImageResource(R.drawable.master);
        }else
        if(iconRank.equals("challenger_i")){
            rang.setImageResource(R.drawable.challenger);
        }else{
            rang.setImageResource(R.drawable.provisional);
        }
    }
}






