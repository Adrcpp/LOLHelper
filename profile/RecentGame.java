package com.example.adrien.lolhelper.profile;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.adrien.lolhelper.util.ConnectionThread;

/**
 * Created by Adrien CESARO on 22/02/2016.
 */
public class RecentGame implements Parcelable{
    private final String TAG = "RecentGame";

    int championId;
    long createdate;
    int duration;
    int mapid;
    boolean win;
    int minionsKilled;

    int item0;
    int item1;
    int item2;
    int item3;
    int item4;
    int item5;

    int trinket;
    int goldEarned;
    int spell1;
    int spell2;
    String subtype;
    int playerrole;
    int kill;
    int death;
    int assist;

    private Bitmap bItem0;
    private Bitmap bItem1;
    private Bitmap bItem2;
    private Bitmap bItem3;
    private Bitmap bItem4;
    private Bitmap bItem5;

    private Bitmap bTrinket;

    private Bitmap bChamp;

    private Bitmap bSpell0;
    private Bitmap bSpell1;


    public RecentGame(int championId,
                      long createdate,
                      int duration,
                      int mapid,
                      boolean win,
                      int spell1,
                      int spell2 ,
                      int item0,
                      int item1,
                      int item2,
                      int item3,
                      int item4,
                      int item5,
                      int trinket,
                      int goldEarned,
                      String subtype,
                      int playerrole,
                      int kill,
                      int death,
                      int assist,
                      int minionsKilled){

        this.championId = championId;
        this.createdate = createdate;
        this.duration = duration;
        this.win = win;
        this.mapid = mapid;

        this.spell1 = spell1;

        this.spell2 = spell2;
        this.item0 = item0;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;

        this.trinket = trinket;

        this.goldEarned = goldEarned;
        this.subtype = subtype;
        this.playerrole = playerrole;
        this.kill = kill;
        this.death = death;
        this.assist = assist;
        this.minionsKilled = minionsKilled;

        new Thread(new Runnable() {
            @Override
            public void run() {
                setImage();
            }
        }).start();

    }

    protected RecentGame(Parcel in) {

        championId = in.readInt();
        createdate = in.readLong();
        duration = in.readInt();
        mapid = in.readInt();
        win = in.readByte() != 0;
        minionsKilled = in.readInt();
        item0 = in.readInt();
        item1 = in.readInt();
        item2 = in.readInt();
        item3 = in.readInt();
        item4 = in.readInt();
        item5 = in.readInt();
        trinket = in.readInt();
        goldEarned = in.readInt();
        spell1 = in.readInt();
        spell2 = in.readInt();
        subtype = in.readString();
        playerrole = in.readInt();
        kill = in.readInt();
        death = in.readInt();
        assist = in.readInt();
        bItem0 = in.readParcelable(Bitmap.class.getClassLoader());
        bItem1 = in.readParcelable(Bitmap.class.getClassLoader());
        bItem2 = in.readParcelable(Bitmap.class.getClassLoader());
        bItem3 = in.readParcelable(Bitmap.class.getClassLoader());
        bItem4 = in.readParcelable(Bitmap.class.getClassLoader());
        bItem5 = in.readParcelable(Bitmap.class.getClassLoader());
        bTrinket = in.readParcelable(Bitmap.class.getClassLoader());
        bChamp = in.readParcelable(Bitmap.class.getClassLoader());
        bSpell0 = in.readParcelable(Bitmap.class.getClassLoader());
        bSpell1 = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(championId);
        dest.writeLong(createdate);
        dest.writeInt(duration);
        dest.writeInt(mapid);
        dest.writeByte((byte) (win ? 1 : 0));
        dest.writeInt(minionsKilled);
        dest.writeInt(item0);
        dest.writeInt(item1);
        dest.writeInt(item2);
        dest.writeInt(item3);
        dest.writeInt(item4);
        dest.writeInt(item5);
        dest.writeInt(trinket);
        dest.writeInt(goldEarned);
        dest.writeInt(spell1);
        dest.writeInt(spell2);
        dest.writeString(subtype);
        dest.writeInt(playerrole);
        dest.writeInt(kill);
        dest.writeInt(death);
        dest.writeInt(assist);
        dest.writeParcelable(bItem0, flags);
        dest.writeParcelable(bItem1, flags);
        dest.writeParcelable(bItem2, flags);
        dest.writeParcelable(bItem3, flags);
        dest.writeParcelable(bItem4, flags);
        dest.writeParcelable(bItem5, flags);
        dest.writeParcelable(bTrinket, flags);
        dest.writeParcelable(bChamp, flags);
        dest.writeParcelable(bSpell0, flags);
        dest.writeParcelable(bSpell1, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecentGame> CREATOR = new Creator<RecentGame>() {
        @Override
        public RecentGame createFromParcel(Parcel in) {
            return new RecentGame(in);
        }

        @Override
        public RecentGame[] newArray(int size) {
            return new RecentGame[size];
        }
    };

    public void setImage(){
        //Si result = -1 c'est que non trouvé donc on lance pas le thread
        Log.i(TAG, "Recent game created " + championId + "/n Starting DL image");
        if(item0!=-1)
            setbItem0(ConnectionThread.ConnectionThreadImage("item/" + item0 + ".png"));
        if(item1!=-1)
            setbItem1(ConnectionThread.ConnectionThreadImage("item/" + item1 + ".png"));
        if(item2!=-1)
            setbItem2(ConnectionThread.ConnectionThreadImage("item/" + item2 + ".png"));
        if(item3!=-1)
            setbItem3(ConnectionThread.ConnectionThreadImage("item/" + item3 + ".png"));
        if(item4!=-1)
            setbItem4(ConnectionThread.ConnectionThreadImage("item/" + item4 + ".png"));
        if(item5!=-1)
            setbItem5(ConnectionThread.ConnectionThreadImage("item/" + item5 + ".png"));
        if(trinket!=-1)
            setbTrinket(ConnectionThread.ConnectionThreadImage("item/" + trinket + ".png"));
        if(championId!=-1)
            setbChamp(ConnectionThread.ConnectionThreadChamp(championId));
        if(spell1!=-1)
            setbSpell0(ConnectionThread.ConnectionThreadSpell(spell1));
        if(spell2!=-1)
            setbSpell1(ConnectionThread.ConnectionThreadSpell(spell2));
    }

    public Bitmap getbChamp() {
        return bChamp;
    }

    public Bitmap getbItem0() {
        return bItem0;
    }

    public Bitmap getbItem1() {
        return bItem1;
    }

    public Bitmap getbItem2() {
        return bItem2;
    }

    public Bitmap getbItem3() {
        return bItem3;
    }

    public Bitmap getbItem4() {
        return bItem4;
    }

    public Bitmap getbItem5() {
        return bItem5;
    }

    public Bitmap getbSpell0() {
        return bSpell0;
    }

    public Bitmap getbSpell1() {
        return bSpell1;
    }

    public Bitmap getbTrinket() {
        return bTrinket;
    }

    public boolean isWin() {
        return win;
    }

    public void setbChamp(Bitmap bChamp) {

        this.bChamp = bChamp;
    }

    public void setbItem0(Bitmap bItem0) {

        this.bItem0 = bItem0;
    }

    public void setbItem1(Bitmap bItem1) {

        this.bItem1 = bItem1;
    }

    public void setbItem2(Bitmap bItem2) {

        this.bItem2 = bItem2;
    }

    public void setbItem3(Bitmap bItem3) {

        this.bItem3 = bItem3;
    }

    public void setbItem4(Bitmap bItem4) {
        this.bItem4 = bItem4;
    }

    public void setbItem5(Bitmap bItem5) {
        this.bItem5 = bItem5;
    }

    public void setbTrinket(Bitmap bTrinket) {
        this.bTrinket = bTrinket;
    }

    public void setbSpell0(Bitmap bSpell0) {

        this.bSpell0 = bSpell0;
    }

    public void setbSpell1(Bitmap bSpell1) {
        this.bSpell1 = bSpell1;
    }

    //Recuperer les images des sum champ item
    //http://ddragon.leagueoflegends.com/cdn/6.3.1/img/champion/Aatrox.png
    //http://ddragon.leagueoflegends.com/cdn/6.3.1/img/item/1001.png
    //http://ddragon.leagueoflegends.com/cdn/6.3.1/img/sprite/spell0.png

}
