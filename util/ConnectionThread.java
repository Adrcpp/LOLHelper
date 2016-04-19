package com.example.adrien.lolhelper.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Adrien CESARO on 14/02/2016.
 */
public class ConnectionThread {

    static final String version = VersionStaticdata();
    static final String API_KEY = Const.API_KEY;
    static final String TAG = "ConnectionThread";
    static final ExecutorService service = Executors.newFixedThreadPool(30);

    public static String VersionStaticdata(){
        String url = "https://global.api.pvp.net/api/lol/static-data/euw/v1.2/realm?api_key="+API_KEY;

        RunData rd = new RunData(url);
        Thread t1 = new Thread(rd);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rd.getResult();
    }

    private static class RunData implements Runnable{
        String url;
        String result;

        public RunData(String url){
            this.url = url;
            result = "6.4.1";
        }
        public void run(){
            InputStream is = null;
            try{
                URL url1 = new URL(url);
                HttpsURLConnection conn =(HttpsURLConnection) url1.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                String s =  InputStreamOperations.InputStreamToString(is);
                JSONObject js = new JSONObject(s);
                result = js.getString("v");
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        public String getResult() {return result;}
    }




    public static String ConnectionThreadChampMastery(int id){
        String url = "https://euw.api.pvp.net/championmastery/location/EUW1/player/"+id+"/topchampions?count=5&api_key="+API_KEY;

        Future<String> future = service.submit(new RunnCallable(url));


        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap ConnectionThreadKeystone(int id) {
        String url = "http://ddragon.leagueoflegends.com/cdn/" + version + "/img/mastery/" + id + ".png";

        Future<Bitmap> future = service.submit(new RunnCallableImage(url));

        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ConnectionThreadKeystoneDes(int id){
        String url = "https://global.api.pvp.net/api/lol/static-data/euw/v1.2/mastery/"+id+"?&api_key="+API_KEY;
        String s ="";


        Future<String> future = service.submit(new RunnCallable(url));


        try {

            JSONObject js = null;
            try {
                js = new JSONObject(future.get());
                s = js.optString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return s;
    }

    public static String ConnectionThreadRune(int id){
        String url = "https://global.api.pvp.net/api/lol/static-data/euw/v1.2/rune/"+id+"?runeData=all&api_key="+API_KEY;


        Future<String> future = service.submit(new RunnCallable(url));

        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String ConnectionThreadStatsChamp(int id){

        String url = "https://euw.api.pvp.net/api/lol/euw/v1.3/stats/by-summoner/"+id+"/ranked?season=SEASON2016&api_key="+API_KEY;

        Future<String> future = service.submit(new RunnCallable(url));


        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ConnectionThreadStatsSummary(int id){

        String url = "https://euw.api.pvp.net/api/lol/euw/v1.3/stats/by-summoner/"+id+"/summary?season=SEASON2016&api_key="+API_KEY;

        Future<String> future = service.submit(new RunnCallable(url));

        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param id: Spell id
     * First connect to Static-data API to retrieve spell name, then connect to cdn to retrieve Bitmap image
     *
     */

    public static Bitmap ConnectionThreadSpell(int id) {

        String url = "https://global.api.pvp.net/api/lol/static-data/euw/v1.2/summoner-spell/"+id+"?spellData=all&api_key="+API_KEY;

        Future<String> future = service.submit(new RunnCallable(url));

        try {
            JSONObject js = new JSONObject(future.get());
            url = "http://ddragon.leagueoflegends.com/cdn/"+version+"/img/spell/"+js.getJSONObject("image").getString("full");
            Future<Bitmap> future2 = service.submit(new RunnCallableImage(url));

            return future2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }



    public static String ConnectionThreadforResult(int id) {

        //Connection à l'API ingame
        String urls = "https://euw.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/EUW1/"+id+"?api_key="+API_KEY;

        Future<String> future = service.submit(new RunnCallable(urls));


        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ConnectionThreadSumm(String id){
        String url = "https://euw.api.pvp.net/api/lol/euw/v1.4/summoner/by-name/"+id+"?api_key="+API_KEY;

        Future<String> future = service.submit(new RunnCallable(url));


        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ConnectionThreadLeague(int id) {
        //Connection à l'API league summoner
        String urls = "https://euw.api.pvp.net/api/lol/euw/v2.5/league/by-summoner/"+id+"/entry?api_key="+API_KEY;

        Future<String> future = service.submit(new RunnCallable(urls));


        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ConnectionThreadLeague(String id) {
        //Connection à l'API league summoner
        String urls = "https://euw.api.pvp.net/api/lol/euw/v2.5/league/by-summoner/"+id+"/entry?api_key="+API_KEY;


        Future<String> future = service.submit(new RunnCallable(urls));

        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }




    public static String ConnectionThreadBuild(int id) {
        //Connection à l'API RecentGame
        Log.i(TAG, "ConnectionThreadBuild");
        String urls = "https://euw.api.pvp.net/api/lol/euw/v2.2/matchlist/by-summoner/31377020?championIds="+id+"&rankedQueues=TEAM_BUILDER_DRAFT_RANKED_5x5&seasons=SEASON2016&api_key="+API_KEY;

        Future<String> future = service.submit(new RunnCallable(urls));

        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ConnectionThreadRencentGame(int id) {
        //Connection à l'API RecentGame
        Log.i(TAG, "ConnectionThreadRecentGame");
        String urls = "https://euw.api.pvp.net/api/lol/euw/v1.3/game/by-summoner/"+id+"/recent?api_key="+API_KEY;

        Future<String> future = service.submit(new RunnCallable(urls));


        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap ConnectionThreadImage(String id) {

        Log.i(TAG, "ConnectionThreadImage");
        String urls = "http://ddragon.leagueoflegends.com/cdn/"+version+"/img/"+id;

        Future<Bitmap> future = service.submit(new RunnCallableImage(urls));

        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }




    public static String ConnectionThreadInGame(final int id){

        String url = "https://euw.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/EUW1/"+id+"?api_key="+API_KEY;


        Future<String> future = service.submit(new RunnCallable(url));


        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap ConnectionThreadChamp(final int id) {
        Log.i(TAG, "ConnectionThreadChamp");

        /*RunnChamp runn = new RunnChamp(id);
        Thread t1 = new Thread(runn);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return runn.getBitmap();*/



        Future<Bitmap> future = service.submit(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                InputStream is = null;
                Bitmap test = null;
                String url1 = "https://global.api.pvp.net/api/lol/static-data/euw/v1.2/champion/" +id + "?api_key=" + API_KEY;

                //Connection à API lol Summoner
                URL url = null;
                try {
                    url = new URL(url1);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    if (conn.getResponseCode() != 200) {
                        Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>" + conn.getResponseCode() + "\n " + url.toString());

                    } else {
                        Log.i(TAG, "Key champ : OK ><><><>" + conn.getResponseCode() + " " + url.toString());
                        //Recup le nom du hero
                        is = conn.getInputStream();
                        String json = InputStreamOperations.InputStreamToString(is);
                        is.close();
                        conn.disconnect();

                        //
                        JSONObject obj = new JSONObject(json);
                        String champ = obj.getString("key");

                        //Nouvelle connection
                        String urls = "http://ddragon.leagueoflegends.com/cdn/" + version + "/img/champion/" + champ + ".png";
                        url = new URL(urls);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setDoInput(true);
                        conn.connect();

                        if (conn.getResponseCode() != 200) {
                            Log.i(TAG, "Pb image Champ >>>>>>>>>>>>>>>>>>>>" + conn.getResponseCode() + "\n " + url.toString());


                        } else {
                            Log.i(TAG, "Image champion DL :OK ><><><>" + conn.getResponseCode() + " " + url.toString());
                            is = conn.getInputStream();
                            test = BitmapFactory.decodeStream(is);
                            is.close();
                            conn.disconnect();
                            Log.i(TAG, "Champion image set " + test.toString());
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return test;
            }
        });

        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    *
    *       CALLABLE / FUTURE
    *
    */
    public static class RunnCallable implements Callable {
        String url;
        String data;

        public RunnCallable(String url){
            this.url = url;
            data = null;
        }

        @Override
        public String call() throws Exception {
            InputStream is = null;
            URL url1 = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) url1.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            if(conn.getResponseCode()!=200){
                Log.i(TAG,"Erreur connection @Callable/Ingame: " + conn.getResponseCode());
            }else{
                is = conn.getInputStream();
                data = InputStreamOperations.InputStreamToString(is);
                is.close();
                conn.disconnect();
            }
            return data;
        }
    }
    public static class RunnCallableImage implements Callable {
        String url;
        Bitmap data;

        public RunnCallableImage(String url){
            this.url = url;
            data = null;
        }

        @Override
        public Bitmap call() throws Exception {
            InputStream is = null;
            URL url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            if(conn.getResponseCode()!=200){
                Log.i(TAG,"Erreur connection @Callable/Ingame: " + conn.getResponseCode());
            }else{
                is = conn.getInputStream();
                data = BitmapFactory.decodeStream(is);
                is.close();
                conn.disconnect();
            }
            return data;
        }
    }

    public static Bitmap TestChampImage(int id){

       final int id2 = id;


        Future<Bitmap> future = service.submit(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {

                InputStream is = null;

                String url1 = "https://global.api.pvp.net/api/lol/static-data/euw/v1.2/champion/"+id2+"?api_key="+API_KEY;
                Bitmap test = null;
                //Connection à API lol Summoner
                URL url = null;
                try {
                    Log.i(TAG, "                     CALLABLE  §§!§ ");
                    url = new URL(url1);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    if(conn.getResponseCode() !=200){
                        Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>" + conn.getResponseCode() + "\n " + url.toString());
                    }else {
                        Log.i(TAG, "Key champ : OK ><><><>" + conn.getResponseCode() + " " + url.toString());
                        //Recup le nom du hero
                        is = conn.getInputStream();
                        String json  = InputStreamOperations.InputStreamToString(is);
                        is.close();
                        conn.disconnect();

                        //
                        JSONObject obj = new JSONObject(json);
                        String champ = obj.getString("key");

                        //Nouvelle connection
                        String urls = "http://ddragon.leagueoflegends.com/cdn/"+version+"/img/champion/"+champ+".png";
                        url = new URL(urls);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setDoInput(true);
                        conn.connect();

                        if(conn.getResponseCode() !=200){
                            Log.i(TAG, "Pb image Champ >>>>>>>>>>>>>>>>>>>>" + conn.getResponseCode() +  "\n " + url.toString());

                        }else {
                            Log.i(TAG, "Image champion DL :OK ><><><>" + conn.getResponseCode() + " " + url.toString());
                            is = conn.getInputStream();
                            test = BitmapFactory.decodeStream(is);
                            is.close();
                            conn.disconnect();
                            //setImageChampion(test);
                            Log.i(TAG, "Champion image set ");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {

                    return test;
                }
            }
        });


        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }





    /*
    *
    *       RUNNABLE
    *
    */


    private static class RunnSum implements Runnable{
        private String id;
        private String result;

        public RunnSum(String id){
            this.id = id;
            result = "";
        }
        public void run(){
            InputStream is = null;
            String url =  id;
            URL url1 = null;

            try{
                url1 = new URL(url);
                HttpsURLConnection conn = (HttpsURLConnection) url1.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                if(conn.getResponseCode() != 200){
                    Log.i(TAG, ">>>>>>>>>>>>>>>>>>>> " + conn.getResponseCode() + "\n  " + url1.toString());
                }else{
                    is = conn.getInputStream();
                    result = InputStreamOperations.InputStreamToString(is);

                    Log.i(TAG,"Connection to " + url1.toString());
                    is.close();
                    conn.disconnect();
                }


            }catch (Exception e){
                e.printStackTrace();
            }

        }
        public String getResult() {
            return result;
        }
    }

    private static class RunnChamp implements Runnable{
        private int icon;
        private Bitmap test;

        public RunnChamp(int icon){
            this.icon = icon;
            test = null;
        }

        public void run(){
            InputStream is = null;

                String url1 = "https://global.api.pvp.net/api/lol/static-data/euw/v1.2/champion/"+icon+"?api_key="+API_KEY;

                //Connection à API lol Summoner
                URL url = null;
                try {
                    url = new URL(url1);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    if(conn.getResponseCode() !=200){
                        Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>" + conn.getResponseCode() + "\n " + url.toString());

                    }else {
                        Log.i(TAG, "Key champ : OK ><><><>" + conn.getResponseCode() + " " + url.toString());
                        //Recup le nom du hero
                        is = conn.getInputStream();
                        String json  = InputStreamOperations.InputStreamToString(is);
                        is.close();
                        conn.disconnect();

                        //
                        JSONObject obj = new JSONObject(json);
                        String champ = obj.getString("key");

                        //Nouvelle connection
                        String urls = "http://ddragon.leagueoflegends.com/cdn/"+version+"/img/champion/"+champ+".png";
                        url = new URL(urls);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setDoInput(true);
                        conn.connect();

                        if(conn.getResponseCode() !=200){
                            Log.i(TAG, "Pb image Champ >>>>>>>>>>>>>>>>>>>>" + conn.getResponseCode() +  "\n " + url.toString());


                        }else {
                            Log.i(TAG, "Image champion DL :OK ><><><>" + conn.getResponseCode() + " " + url.toString());
                            is = conn.getInputStream();
                            test = BitmapFactory.decodeStream(is);
                            is.close();
                            conn.disconnect();
                            //setImageChampion(test);

                            Log.i(TAG, "Champion image set " + test.toString());
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
            }
        }
        public Bitmap getBitmap(){
            return test;
        }
    }


    private static class Runn implements Runnable{
        String urls;
        String result;

        public Runn(String urls){
            this.urls = urls;
            result = "";
        }

        @Override
        public void run(){

            try{
                InputStream is = null;
                URL url = new URL(urls);

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                if(conn.getResponseCode() !=200){
                    Log.i(TAG, ">>>>>>>>>>>>>>>>>>>> " + conn.getResponseCode() + "\n " + urls);
                    setResult(null);
                }else {
                    Log.i(TAG, "OK ><><><> " + conn.getResponseCode() +  " " + url.toString());
                    is = conn.getInputStream();

                    result = InputStreamOperations.InputStreamToString(is);

                    setResult(result);
                    conn.disconnect();
                    is.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        public synchronized void setResult(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }
    }

    private static class RunnImage implements Runnable{
        private String url;
        private Bitmap test;

        public RunnImage(String url){
            this.url = url;
            test = null;
        }

        public void run(){
            InputStream is = null;

            //Connection à API lol Summoner
            URL urls = null;
            try {
                urls = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                if(conn.getResponseCode() !=200){
                    Log.i(TAG, "RunnImage pb >>>>>>>>>>>>>>>>>>>>" + conn.getResponseCode() + "\n " + url);

                }else {
                    Log.i(TAG, "RunnImage OK ><><><>" + conn.getResponseCode() +  " " + url.toString());

                    is = conn.getInputStream();
                    test = BitmapFactory.decodeStream(is);

                    Log.i(TAG, "Champion image set " + test.toString());
                    conn.disconnect();
                    is.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Bitmap getBitmap(){
            return test;
        }
    }

}
