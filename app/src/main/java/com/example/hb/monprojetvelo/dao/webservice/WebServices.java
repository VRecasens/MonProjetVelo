package com.example.hb.monprojetvelo.dao.webservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.hb.monprojetvelo.main.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import greendao.Station;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hb on 10/01/2017.
 */
public class WebServices {
    private static final String URL_SERVER_JCDECAUX = "https://api.jcdecaux.com/vls/v1/stations";
    private static final String CLE_JCDECAUX = "apiKey=c6825cd45b60feefa143659f9668488e472cd00d";
    private static final String CONTRAT_TOULOUSE = "contract=Toulouse";
    //monurl2 = "https://api.jcdecaux.com/vls/v1/stations?&contract=Toulouse&apiKey=c6825cd45b60feefa143659f9668488e472cd00d";

    /**
     * Retourne la liste des stations de velo de toulouse depuis le serveur de JCDECAUX
     *
     * @return
     * @throws Exception
     */
    public static ArrayList<Station> getAllStationToulouse() throws Exception {

        String result = WebServices.sendGetOkHttpRequest(URL_SERVER_JCDECAUX + "?&" + CONTRAT_TOULOUSE + "&" + CLE_JCDECAUX);
        //Création de l'objet
        Gson gson = new Gson();
        //parsing du flux ou du String contenant du JSON
        return gson.fromJson(result,
                new TypeToken<ArrayList<Station>>() {
                }.getType());
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //    public static ArrayList<Eleve> loadEleveFromWeb() {
    //        ArrayList<Eleve> temp = new ArrayList<>();
    //        for (int i = 0; i < 10; i++) {
    //            temp.add(new Eleve("Eleve_" + i, "prenom"));
    //            SystemClock.sleep(500);
    //        }
    //        return temp;
    //    }
    private static String sendGetOkHttpRequest(String url) throws Exception {
        OkHttpClient client = new OkHttpClient();

        //Corps de la requête
        Request request = new Request.Builder().url(url).build();

        // Execution de la requête
        Response response = client.newCall(request).execute();
        // Analyse du code retour
        if (response.code() != 200)

        {
            throw new Exception("Réponse du serveur incorrect : " + response.code());
        }

        else {
            //Résultat de la requete.
            return response.body().string();
        }
    }
}
