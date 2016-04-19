package com.example.adrien.lolhelper.profile;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.adrien.lolhelper.util.ConnectionThread;

import org.json.JSONObject;

/**
 * Created by Adrien CESARO on 15/03/2016.
 */
public class PChampHeader implements Parcelable{
    private final String TAG = "PChampHeader";
    Bitmap pcIcon;
    int idChamp;
    String pcKda;
    int pcwin;
    JSONObject Child;

    public PChampHeader(int idChamp, String pcKda, int pcWin, JSONObject Child){
        Log.i(TAG, "PChampHeader" + idChamp);
        this.idChamp = idChamp;
        this.pcKda = pcKda;
        this.pcwin = pcWin;
        this.Child = Child;

        new Thread(new Runnable() {
            @Override
            public void run() {
               setImage();
            }
        }).start();
    }

    protected PChampHeader(Parcel in) {

        pcIcon = in.readParcelable(Bitmap.class.getClassLoader());
        idChamp = in.readInt();
        pcKda = in.readString();
        pcwin = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(pcIcon, flags);
        dest.writeInt(idChamp);
        dest.writeString(pcKda);
        dest.writeInt(pcwin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PChampHeader> CREATOR = new Creator<PChampHeader>() {
        @Override
        public PChampHeader createFromParcel(Parcel in) {
            return new PChampHeader(in);
        }

        @Override
        public PChampHeader[] newArray(int size) {
            return new PChampHeader[size];
        }
    };

    public void setImage(){
        pcIcon = ConnectionThread.ConnectionThreadChamp(this.idChamp);
    }
}
