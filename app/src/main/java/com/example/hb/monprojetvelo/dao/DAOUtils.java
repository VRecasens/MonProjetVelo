package com.example.hb.monprojetvelo.dao;

import android.util.Pair;

import com.example.hb.monprojetvelo.dao.bdd.StationBDDManager;
import com.example.hb.monprojetvelo.dao.webservice.WebServices;

import java.util.ArrayList;

import greendao.Station;

/**
 * Created by hb on 12/01/2017.
 */
public class DAOUtils {

    public enum FROM {BDD, INTERNET}

    /**
     * Recupère les stations soit sur le serveur soit en base de donnée
     *
     * @return
     */
    public static Pair<FROM, ArrayList<Station>> getAllStation() {
        ArrayList<Station> stations;
        FROM from;
        try {
            if (!WebServices.isOnline()) {
                throw new Exception("Pas d'internet");
            }

            //Req
            stations = WebServices.getAllStationToulouse();
            from = FROM.INTERNET;
        }
        catch (Exception e) {
            e.printStackTrace();
            //Req echoué
            stations = StationBDDManager.getAllStations();
            from = FROM.BDD;
        }

        //Si ca vient d'internet je sauvegarde en abse
        if (from == FROM.INTERNET) {
            //sauvegarde
            StationBDDManager.chargementBDDStations(stations);
        }

        return new Pair<>(from, stations);
    }
}
