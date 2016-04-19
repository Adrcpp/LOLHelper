package com.example.adrien.lolhelper.ingame;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adrien.lolhelper.util.ConnectionThread;
import com.example.adrien.lolhelper.profile.Profile;
import com.example.adrien.lolhelper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Adrien on 08/02/2016.
 */
public class InGame extends Activity {
    private final String TAG = "Ingame";

    private TextView txt;
    private TextView id;

    private List<IngameInfo> listInfo;
    private ListView mListView;

    private InGameAdapter adapter;

    private long timeStart = System.currentTimeMillis();
    AsyncTask asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingame_layout);

        listInfo = new ArrayList<IngameInfo>();
        mListView = (ListView) findViewById(R.id.Team1);

        adapter = new InGameAdapter(getApplicationContext(), listInfo);

        Bundle extras = getIntent().getExtras();

        id = (TextView) findViewById(R.id.id_igame);

        String x = ""+extras.getInt("id");
        id.setText(x);

        // API CurrentGame
        String jsonData = ConnectionThread.ConnectionThreadInGame(extras.getInt("id"));


        //Creation de chaque participants de façon asynchrone
        new AsyncTask<String, IngameInfo, List<IngameInfo> >(){

            @Override
            protected List<IngameInfo> doInBackground(String... jsonData) {
                JSONObject obj = null;

                try {
                    obj = new JSONObject(jsonData[0]);



                    // String gameMode = obj.optString("gameMode");
                    JSONArray participants = obj.optJSONArray("participants");

                    String sumSearch="";

                    //Recupération de tous les id Summoner
                    for(int i=0; i<participants.length();++i) {
                        JSONObject test = participants.optJSONObject(i);
                        sumSearch += test.optInt("summonerId")+",";
                    }

                    //Requete League sur les 10 participants
                    String summrank = ConnectionThread.ConnectionThreadLeague(sumSearch);
                    JSONObject js = null;
                    try {
                        js = new JSONObject(summrank);
                        //Pour chaque participants, on cherche les runes, son rang,
                        //ses masteries et les stats de son champions

                        for(int i=0; i<participants.length();++i) {
                            JSONObject test = participants.optJSONObject(i);
                            JSONArray runeArray = test.optJSONArray("runes");

                            String runesTotal="";
                            Log.i(TAG, "Array Rune :"+runeArray.length() );
                            for(int a=0; a < runeArray.length(); ++a) {
                                JSONObject jso = null;
                                try {
                                    jso = new JSONObject(ConnectionThread.ConnectionThreadRune(runeArray.optJSONObject(a).optInt("runeId", 0)));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                runesTotal += runeArray.optJSONObject(a).optInt("count", 0) + "X " + jso.optString("description") + "\n";
                            }

                            String division ="Unranked";
                            int wins =0;
                            int losses =0;
                            if(js.optJSONArray(""+test.optInt("summonerId")) != null) {
                                division =
                                        js.optJSONArray("" + test.optInt("summonerId")).optJSONObject(0).optString("tier").toLowerCase() + "_"
                                                + js.optJSONArray("" + test.getInt("summonerId")).optJSONObject(0).optJSONArray("entries").optJSONObject(0).optString("division").toLowerCase(); //ex :"GOLDIII"

                                wins = js.optJSONArray(""+test.getInt("summonerId")).optJSONObject(0).optJSONArray("entries").optJSONObject(0).optInt("wins");
                                losses = js.optJSONArray(""+test.getInt("summonerId")).optJSONObject(0).optJSONArray("entries").optJSONObject(0).optInt("losses");
                            }


                            // MASTERY
                            JSONArray mastArray = test.optJSONArray("masteries");
                            String masterString = "";
                            int fer=0;
                            int cun=0;
                            int res=0;
                            int keystone=0;

                            for(int k=0; k < mastArray.length(); ++k){
                                JSONObject jsd =mastArray.getJSONObject(k) ;

                                String dd = ""+jsd.getInt("masteryId");
                                if (dd.startsWith(""+61)){
                                    ++fer;
                                }else if(dd.startsWith(""+63)){
                                    ++cun;
                                }else{
                                    ++res;
                                }
                                // KEYSTONE
                                if(jsd.getInt("masteryId") == 6161 || jsd.getInt("masteryId") == 6162 || jsd.getInt("masteryId") == 6164 ||
                                        jsd.getInt("masteryId") == 6361 || jsd.getInt("masteryId") == 6362 || jsd.getInt("masteryId") == 6363 ||
                                        jsd.getInt("masteryId") == 6261 || jsd.getInt("masteryId") == 6262 || jsd.getInt("masteryId") == 6263 ){
                                    keystone = jsd.getInt("masteryId");
                                }
                            }
                            if(fer!=0){fer*=3;}
                            if(res!=0){res*=3;}
                            if(cun!=0){cun*=3;}

                            masterString = "Masteries:\n"+fer+"/"+cun+"/"+res;

                            // SPELL
                            int[] spell = new int[2];
                            spell[0] = test.optInt("spell1Id", -1);
                            spell[1] = test.optInt("spell2Id", -1);

                            //Search for champs stats:
                            String champStats = ConnectionThread.ConnectionThreadStatsChamp(test.getInt("summonerId"));

                            int[] tab = new int[7];
                            if(champStats!=null && champStats!=""){
                                JSONObject jschamp = new JSONObject(champStats);
                                JSONArray arraychmp = jschamp.optJSONArray("champions");

                                for(int j =0; j <arraychmp.length(); ++j) {
                                    if (arraychmp.optJSONObject(j).optInt("id") == test.optInt("championId")) {
                                        tab[0] = arraychmp.optJSONObject(j).optJSONObject("stats").optInt("totalSessionsPlayed");
                                        tab[1] = arraychmp.optJSONObject(j).optJSONObject("stats").optInt("totalSessionsWon");
                                        tab[2] = arraychmp.optJSONObject(j).optJSONObject("stats").optInt("totalSessionsLost");

                                        tab[3] = arraychmp.optJSONObject(j).optJSONObject("stats").optInt("totalChampionKills");
                                        tab[4] = arraychmp.optJSONObject(j).optJSONObject("stats").optInt("totalDeathsPerSession");
                                        tab[5] = arraychmp.optJSONObject(j).optJSONObject("stats").optInt("totalAssists");
                                        tab[6] = arraychmp.optJSONObject(j).optJSONObject("stats").optInt("totalMinionKills");
                                        break;
                                    }
                                }
                            }

                            // Creation de l'objet IngameInfo pour
                            //listInfo.add(
                            publishProgress(new IngameInfo(
                                    test.getString("summonerName"),
                                    test.getInt("championId"),
                                    test.getInt("teamId"),
                                    test.getInt("summonerId"),
                                    spell,
                                    runesTotal,
                                    masterString,
                                    keystone,
                                    division,
                                    wins,
                                    losses,
                                    tab)); // tab = API Stats champs des summoners(win du champ, kda, cs)
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return listInfo;
            }


            @Override
            protected void onProgressUpdate(IngameInfo ... ingameInfos){
                super.onProgressUpdate(ingameInfos);
                listInfo.add(ingameInfos[0]);

                mListView.setAdapter(adapter);
            }


            @Override
            protected void onPostExecute(List<IngameInfo> listInfo) {
                //mListView.setAdapter(adapter);
                long endtime = System.currentTimeMillis();
                Log.i(TAG, "Liste chargé dans la vue. Temps: " + (endtime - timeStart) + " ms.");
                Toast.makeText(getApplicationContext(),"Temps de chargement : " + (endtime - timeStart)+ " ms",Toast.LENGTH_LONG).show();
            }

        }.execute(jsonData);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IngameInfo igi = (IngameInfo) adapter.getItem(position);
                Intent intent = new Intent(InGame.this, Profile.class);
                intent.putExtra("id",igi.getIdSum());
                intent.putExtra("summonername", igi.getSummName());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}