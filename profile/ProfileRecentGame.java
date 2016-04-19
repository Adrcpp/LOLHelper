package com.example.adrien.lolhelper.profile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adrien.lolhelper.util.ConnectionThread;
import com.example.adrien.lolhelper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Adrien CESARO on 15/02/2016.
 */
public class ProfileRecentGame extends Fragment {

    private final String TAG = "ProfileRecentGame";
    ArrayList<RecentGame> mList;
    ListView mListView;
    RecentGameAdapter adapter;
    long start = System.currentTimeMillis();
    AsyncSpeed asyncSpeed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getContext(), "RecentGame @ onCreate", Toast.LENGTH_SHORT).show();
        if(savedInstanceState != null){
            Toast.makeText(getContext(), "Recent game Reloaded", Toast.LENGTH_SHORT).show();
            mList = savedInstanceState.getParcelableArrayList("list");
            adapter = new RecentGameAdapter(getContext(), mList);
        }else{
            mList = new ArrayList<RecentGame>();
            adapter = new RecentGameAdapter(getContext(), mList);

            // Starting new Thread / AsyncTask on the background
            Bundle extras = getActivity().getIntent().getExtras();
            final int id = extras.getInt("id");
            String jsondata = ConnectionThread.ConnectionThreadRencentGame(id);
            asyncSpeed = new AsyncSpeed();
            asyncSpeed.execute(jsondata);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstances) {
        Log.i(TAG, "Recent game");

        View view = inflater.inflate(R.layout.profilerencentgame_layout, container, false);
        mListView = (ListView) view.findViewById(R.id.listView2);
        Log.i(TAG, "Recent Game // connection ");

        if (savedInstances != null){
            mListView.setAdapter(adapter);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Yo", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(getContext(), "Recent Game saved", Toast.LENGTH_SHORT).show();
        outState.putParcelableArrayList("list", mList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(getContext(), "Recent game @ Paused", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(asyncSpeed != null)
        asyncSpeed.cancel(true);
        Toast.makeText(getContext(), "Recent game @ Stopped", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(), "Recent game @ Destroyed", Toast.LENGTH_SHORT).show();

    }

    class AsyncSpeed extends AsyncTask<String, RecentGame, List<RecentGame>>{

         @Override
         protected List<RecentGame> doInBackground(String... jsondata) {

                 try {
                     JSONObject obj = new JSONObject(jsondata[0]);
                     JSONArray jsonArray = obj.getJSONArray("games");

                     for (int i = 0; i < jsonArray.length(); ++i) {
                         if (isCancelled()) {
                             return null;
                         } else {
                             //TODO: Gerer les problemes JSONException:No value for ... utilisation de opt
                             JSONObject obj2 = jsonArray.getJSONObject(i);

                             RecentGame rg = new RecentGame(obj2.optInt("championId", -1),
                                     obj2.optLong("createDate", -1),
                                     obj2.getJSONObject("stats").optInt("timePlayed"),
                                     obj2.optInt("mapId", -1),
                                     obj2.getJSONObject("stats").getBoolean("win"),
                                     obj2.optInt("spell1", -1),
                                     obj2.optInt("spell2", -1),
                                     obj2.getJSONObject("stats").optInt("item0", -1),
                                     obj2.getJSONObject("stats").optInt("item1", -1),
                                     obj2.getJSONObject("stats").optInt("item2", -1),
                                     obj2.getJSONObject("stats").optInt("item3", -1),
                                     obj2.getJSONObject("stats").optInt("item4", -1),
                                     obj2.getJSONObject("stats").optInt("item5", -1),
                                     obj2.getJSONObject("stats").optInt("item6", -1),
                                     obj2.getJSONObject("stats").optInt("goldEarned", 0),
                                     obj2.getString("subType").replaceAll("_", " ").toLowerCase(),
                                     obj2.getJSONObject("stats").optInt("playerRole", -1),
                                     obj2.getJSONObject("stats").optInt("championsKilled", 0),
                                     obj2.getJSONObject("stats").optInt("numDeaths", 0),
                                     obj2.getJSONObject("stats").optInt("assists", 0),
                                     obj2.getJSONObject("stats").optInt("minionsKilled", 0) + obj2.getJSONObject("stats").optInt("neutralMinionsKilled", 0));
                             publishProgress(rg);
                         }
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             return mList;
         }


         @Override
         protected void onProgressUpdate(RecentGame...RecentGame){
             super.onProgressUpdate(RecentGame);
             mList.add(RecentGame[0]);
             mListView.setAdapter(adapter);
         }


         @Override
         protected void onPostExecute(List<RecentGame> mList) {
             long end = System.currentTimeMillis();
             Toast.makeText(getContext(),"Temps de chargement : " + (end - start)+ " ms",Toast.LENGTH_LONG).show();
             //RecentGameAdapter adapter = new RecentGameAdapter(getContext(), mList);
             mListView.setAdapter(adapter);
         }
     }


}
