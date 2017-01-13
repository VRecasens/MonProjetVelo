package com.example.hb.monprojetvelo.dao.bdd;

import com.example.hb.monprojetvelo.main.MyApplication;

import java.util.ArrayList;

import greendao.Station;
import greendao.StationDao;

/**
 * Created by hb on 11/01/2017.
 */
public class StationBDDManager {

    public static void insertOrUpdate(Station station) {
        getStationDao().insertOrReplace(station);
    }

    public static void clearStation() {
        getStationDao().deleteAll();
    }

    public static void deleteStationithId(long id) {
        getStationDao().delete(getStationForId(id));
    }

    public static Station getStationForId(long id) {
        return getStationDao().load(id);
    }

    public static ArrayList<Station> getAllStations() {
        return (ArrayList<Station>) getStationDao().loadAll();
    }

    private static StationDao getStationDao() {
        return MyApplication.getInstance().getDaoSession().getStationDao();
    }

    public static void chargementBDDStations(ArrayList<Station> alStation) {

        for (Station station : alStation) {

            PositionBDDManager.insertOrUpdate(station.getForcePosition());
            station.setPositionId(station.getForcePosition().getId());
            StationBDDManager.insertOrUpdate(station);
            //                Log.w("Chargement", "station" + station.getPosition().toString() + "##");
            //                PositionBDDManager.insertOrUpdate(station.getPosition());
        }
    }
}

