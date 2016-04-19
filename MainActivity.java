package com.example.adrien.lolhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;

import android.widget.Toast;

import org.json.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import com.example.adrien.lolhelper.ingame.InGame;
import com.example.adrien.lolhelper.profile.Profile;
import com.example.adrien.lolhelper.summoner.Summoner;
import com.example.adrien.lolhelper.summoner.SummonerAdapter;
import com.example.adrien.lolhelper.util.ConnectionThread;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private EditText getSumm;
    private ImageView imageView3;
    private ArrayList<Summoner> mList;
    private ListView mListView;

    private Switch aSwitch;
    private boolean profileboole = true;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);
        mList = new ArrayList<Summoner>();

        getSumm = (EditText) findViewById(R.id.getSumm);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView3.setImageResource(R.drawable.diamond_i);

        /***********************************
        ** Gestion du Navigation Drawer
        ************************************/

        final String[] mStringlist = {"Main","Build","LCS","About"} ;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mStringlist));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "vvdvdvdvdvdvdvvvvvvvvv", Toast.LENGTH_SHORT).show();
            }
        });

        mTitle = mDrawerTitle = "LolHelper";

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        //C'est le boutton dans l'action bar qui ouvre le nav drawer.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //Recupe des sum sauvegarder:
        try {
            FileInputStream fois = openFileInput("summonersave");
            ObjectInputStream ois = new ObjectInputStream(
                    new BufferedInputStream(fois));
                if(fois.read() != 0) {

                   try {
                        ArrayList<Summoner> saved = (ArrayList<Summoner>)ois.readObject();
                        if(saved!=null) {
                            for (Summoner summoner : saved) {
                                mList.add(new Summoner(summoner));
                            }
                        }
                   }catch(EOFException e){
                       e.printStackTrace();
                       ois.close();
                       Toast.makeText(getApplicationContext(), "Loading done", Toast.LENGTH_LONG).show();
                   }

                    SummonerAdapter sumAd = new SummonerAdapter(MainActivity.this, mList);
                    mListView.setAdapter(sumAd);
                }else{
                    Toast.makeText(getApplicationContext(),"No file found",Toast.LENGTH_LONG).show();
                }
            } catch (ClassNotFoundException e1) {
                 e1.printStackTrace();
            } catch (OptionalDataException e1) {
                e1.printStackTrace();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (StreamCorruptedException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
        }

        /////////////////////////////////////////
        getSumm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSumm.setText("");
            }
        });

        // SWITCH : Ingame or Profile
        aSwitch = (Switch) findViewById(R.id.switch1);
        aSwitch.setTextOff("Ingame");
        aSwitch.setTextOn("Profile");
        aSwitch.setChecked(true);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Switch on = Profile
                    profileboole = true;
                } else {
                    // Switch off = Ingame
                    profileboole = false;
                }
            }
        });

        // SEARCH SUMMONER :
        Button buttonsearch = (Button) findViewById(R.id.button);
        buttonsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://euw.api.pvp.net/api/lol/euw/v1.4/summoner/by-name/";

                ConnectivityManager connectivityManager = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo n = connectivityManager.getActiveNetworkInfo();
                String s = getSumm.getText().toString().trim().replaceAll("\\s", "");
                Log.i(TAG, s);

                if (n != null && n.isConnected()) {

                    //Verification le summoner existe sinon on se connecte a l'API via asyncTask

                    getSumm.clearFocus();
                    boolean bool = true;

                    if (ConnectionThread.ConnectionThreadSumm(s).equals("")) {
                        Toast.makeText(getApplicationContext(), getSumm.getText().toString() + " not found", Toast.LENGTH_LONG).show();
                    } else {
                        for (Summoner smn : mList) {
                            if (smn.getSummName().toLowerCase().replaceAll("\\s", "").equals(getSumm.getText().toString().toLowerCase().trim().replaceAll("\\s", ""))) {
                                bool = false;
                                Toast.makeText(getApplicationContext(), "Summoner already searched", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (bool) {
                            Log.i(TAG, s);
                            setSumm(s);
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_LONG).show();
                }
                getSumm.setText("");
            }
        });


        /******************************************
         ** Summoner Listview : onClickListener
         *****************************************/
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplication(), "Clicked on: " + position, Toast.LENGTH_LONG).show();

                SummonerAdapter sum = new SummonerAdapter(MainActivity.this, mList);
                Summoner summ = sum.getItem(position);

                if (profileboole) {
                    // Lance profile
                    if (ConnectionThread.ConnectionThreadLeague(summ.getId()) != null) {
                        Intent intent = new Intent(MainActivity.this, Profile.class);
                        intent.putExtra("id", summ.getId());
                        intent.putExtra("level", summ.getLevel());
                        intent.putExtra("icon", summ.getIcon());
                        intent.putExtra("summonername", summ.getSummName());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Ranked Game for " + summ.getSummName(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Avant de lancer l'activity faut checke si status 200
                    if (ConnectionThread.ConnectionThreadforResult(summ.getId()) != null) {
                        Intent intent = new Intent(MainActivity.this, InGame.class);
                        intent.putExtra("id", summ.getId());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), summ.getSummName() + " is not ingame.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        /*******************************************
        **  LONG CLICK ON ITEM TO REMOVE IT
        ******************************************/
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder mDialog = new AlertDialog.Builder(MainActivity.this);
                mDialog.setTitle("Remove item").setMessage("Do you want to remove this item ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mList.remove(position);
                        SummonerAdapter sumAd = new SummonerAdapter(MainActivity.this, mList);
                        mListView.setAdapter(sumAd);
                    }
                });

                mDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = mDialog.create();
                alertDialog.show();
                return true;
            }
        });
    }

    /***********************************************************************************************
    **    Creation of the Summoner Object + add to the ArrayList mList then added to the ListView
    ************************************************************************************************/
    public void setSumm(String url){

        try {
            // A partir du Json recu, on crée objet Summoner
            JSONObject obj = new JSONObject(ConnectionThread.ConnectionThreadSumm(url));
            String g = getSumm.getText().toString().toLowerCase().trim().replaceAll("\\s", "");
            Summoner sum =
                    new Summoner(obj.getJSONObject(g).get("name").toString(),  // Summoner name
                            obj.getJSONObject(g).getInt("profileIconId"),      // Profile icon
                            obj.getJSONObject(g).getInt("summonerLevel"),      //Level
                            obj.getJSONObject(g).getInt("id"));                //id


            //Utilisation de l'adapter / ListView
            mList.add(sum);
            SummonerAdapter sumAd = new SummonerAdapter(MainActivity.this, mList);
            mListView.setAdapter(sumAd);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause(){
        super.onPause();
        try {
            FileOutputStream fos = openFileOutput("summonersave", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos));
            oos.writeObject(mList);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        try {
            FileOutputStream fos = openFileOutput("summonersave", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos));
            oos.writeObject(mList);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
}