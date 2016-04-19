package com.example.adrien.lolhelper.profile;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.adrien.lolhelper.util.ConnectionThread;
import com.example.adrien.lolhelper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Adrien CESARO on 14/03/2016.
 */
public class ProfileChampion extends Fragment {
    private final String TAG = "ProfileChampion";

    AsyncChampion aa;
    ArrayList<PChampHeader> mList;
    ExpandableListView mExpList;
    PchampionExpendableList adapter;
    int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getContext(), "Champions @ onCreate", Toast.LENGTH_SHORT).show();
        if(savedInstanceState != null){
            // Reloaded fragment ...
            Toast.makeText(getContext(), "Champions Reloaded", Toast.LENGTH_SHORT).show();
            mList = savedInstanceState.getParcelableArrayList("list");
            adapter = new PchampionExpendableList(getContext(), mList);
        }else {
            mList = new ArrayList<PChampHeader>();
            adapter = new PchampionExpendableList(getContext(), mList);

            //Préparation de l'AsyncTask ...
            Bundle extras = getActivity().getIntent().getExtras();
            id = extras.getInt("id");
            final String jsondata = ConnectionThread.ConnectionThreadStatsChamp(id);
            aa = new AsyncChampion();
            aa.execute(jsondata);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstances){
        Log.i(TAG, "Champion crée");

        View view = inflater.inflate(R.layout.profilechamipion_tab, container, false);
        //La ExpList
        mExpList = (ExpandableListView) view.findViewById(R.id.expandableListView);

        if (savedInstances != null) {
            mExpList.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    class AsyncChampion extends AsyncTask<String, PChampHeader, ArrayList<PChampHeader> >{

        @Override
        protected ArrayList<PChampHeader> doInBackground(String... jsondata) {

                try {
                    JSONObject obj = new JSONObject(jsondata[0]);
                    JSONArray champion = obj.getJSONArray("champions");

                    for (int i = 0; i < champion.length(); ++i) {
                        if(isCancelled()){
                            return null;
                        }else {
                            Log.i(TAG, "Creation des champions en cours ...");
                            JSONObject champstats = champion.optJSONObject(i);
                            JSONObject child = champion.optJSONObject(i).optJSONObject("stats");

                            if (champstats.optInt("id") != 0) {
                                int tot = champstats.optJSONObject("stats").optInt("totalSessionsPlayed");
                                String kda = ""
                                        + champstats.optJSONObject("stats").optInt("totalChampionKills") / tot
                                        + "/" + champstats.optJSONObject("stats").optInt("totalDeathsPerSession") / tot
                                        + "/" + champstats.optJSONObject("stats").optInt("totalAssists") / tot;

                                publishProgress(new PChampHeader(
                                        champstats.optInt("id"),
                                        kda,
                                        champstats.optJSONObject("stats").optInt("totalSessionsWon"),
                                        child));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return mList;
        }

        @Override
        protected void onProgressUpdate(PChampHeader...pChampHeaders) {
            super.onProgressUpdate(pChampHeaders);
            mList.add(pChampHeaders[0]);
            mExpList.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(ArrayList<PChampHeader> mList) {
            mExpList.setAdapter(adapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(getContext(), "Champions saved", Toast.LENGTH_SHORT).show();
        outState.putParcelableArrayList("list", mList);
    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(getContext(), "Champions @ Paused", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(aa != null)
        aa.cancel(true);
        Toast.makeText(getContext(), "Champions @ Stopped", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(), "Champions @ Destroyed", Toast.LENGTH_SHORT).show();
    }

}
