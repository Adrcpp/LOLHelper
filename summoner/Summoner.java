package com.example.adrien.lolhelper.summoner;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.adrien.lolhelper.util.ConnectionThread;

import java.io.Serializable;

/**
 * Created by Adrien on 03/02/2016.
 */
public class Summoner implements Serializable{
    private final String TAG = "Summoner";

    private String summName;
    private int icon;
    private int level;
    private int id;

    private transient Bitmap iconImage;

    public Summoner(){
        summName ="";
        icon = 0;
        level = 0;
        id =0;
        iconImage = null;
    }

    /*
        Summoner object cloner
     */
    public Summoner(Summoner sum){
        summName = sum.summName;
        icon = sum.icon;
        level = sum.level;
        id = sum.id;

        Log.i(TAG, "Summoner created and Thread running for downloading icon image");
        String s = "/profileicon/"+icon+".png";
        iconImage = ConnectionThread.ConnectionThreadImage(s);
    }

    public Summoner(String summName, int icon, int level, int id){
        this.summName = summName;
        this.icon = icon;
        this.level = level;
        this.id = id;

        Log.i(TAG, "Summoner created and Thread running for downloading icon image");
        String s = "/profileicon/"+icon+".png";
        this.iconImage = ConnectionThread.ConnectionThreadImage(s);
    }


    public String getSummName(){return summName;}

    public int getLevel(){return level;}

    public String getLevelString(){
        String s = "" + level;
        return s;
    }

    public int getIcon(){return icon;}

    public int getId(){return id;}

    public Bitmap getIconImage(){return iconImage;}


}
