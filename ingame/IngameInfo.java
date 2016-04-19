package com.example.adrien.lolhelper.ingame;

import android.graphics.Bitmap;

import com.example.adrien.lolhelper.util.ConnectionThread;

/**
 * Created by Adrien CESARO on 13/02/2016.
 */
public class IngameInfo {

    //From API Current game
    private String summName;
    private int idChampions;
    private int idTeam;
    private int idSum;
    private int[] spell;

   // private Map<Integer,Integer> runes;
    private String runes;
    private String masteries;
    private int keystone;

    //From API League
    private String division;
    private int wins;
    private int losses;

    //From API Stats
    private int[] champStat;


    private Bitmap imageChampion;
    private Bitmap bSpell0;
    private Bitmap bSpell1;
    private Bitmap bKeystone;
    private String keystoneDes;

    public IngameInfo(){

    }

    public IngameInfo(String summName,int idChampions, int idTeam, int idSum,int[] spell, String runes, String masteries, int keystone, String division, int wins, int losses, int[] champStat){
        this.summName = summName;
        this.idChampions = idChampions;
        this.idTeam = idTeam;
        this.idSum = idSum;
        this.spell = spell;

        this.runes = runes;
        this.masteries = masteries;
        this.keystone = keystone;

        this.division = division;
        this.wins = wins;
        this.losses = losses;

        this.champStat = champStat;

        imageChampion = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
              imageUpdate();
            }
        }).start();

    }

    public void imageUpdate(){
        //Lancer thread pour recup image chammpion et spell
        if(idChampions!=-1)
            //setImageChampion(ConnectionThread.ConnectionThreadChamp(idChampions));
            setImageChampion(ConnectionThread.TestChampImage(idChampions));
        if(spell[0]!=-1)
            setbSpell0(ConnectionThread.ConnectionThreadSpell(spell[0]));
        if(spell[1]!=-1)
            setbSpell1(ConnectionThread.ConnectionThreadSpell(spell[1]));
        if(keystone!=-1){
            setbKeystone(ConnectionThread.ConnectionThreadKeystone(keystone));
            keystoneDes = ConnectionThread.ConnectionThreadKeystoneDes(keystone);
        }
    }

    public String getSummName(){return summName;}
    public int getIdChampions(){
        return idChampions;
    }
    public Bitmap getImageChampion(){
        return imageChampion;
    }
    public void setImageChampion(Bitmap imageChampion){this.imageChampion = imageChampion;}
    public int getIdSum() {return idSum;}
    public int getIdTeam() {return idTeam;}
    public int[] getSpell() {return spell;}

    public String getRunes() {return runes;}
    public String getMasteries() {return masteries;}
    public int getKeystone() {return keystone;}
    public String getKeystoneDes() {return keystoneDes;}

    public String getDivision() {return division;}
    public int getWins() {return wins;}
    public int getLosses() {return losses;}

    public int[] getChampStat() {return champStat;}

    public Bitmap getbSpell0() {return bSpell0;}
    public Bitmap getbSpell1() {return bSpell1;}

    public void setbSpell0(Bitmap bSpell0) {this.bSpell0 = bSpell0;}
    public void setbSpell1(Bitmap bSpell1) {this.bSpell1 = bSpell1;}

    public void setbKeystone(Bitmap bKeystone) {this.bKeystone = bKeystone;}

    public Bitmap getbKeystone() {return bKeystone;}

    public void setIdChampions(int idChampions) {this.idChampions = idChampions;}

}
