package com.example.hb.monprojetvelo.dao.bdd;

import com.example.hb.monprojetvelo.main.MyApplication;

import java.util.ArrayList;

import greendao.Position;
import greendao.PositionDao;

/**
 * Created by hb on 11/01/2017.
 */
public class PositionBDDManager {

    public static void insertOrUpdate(Position position) {
        getPositionDao().insertOrReplace(position);
    }

    public static void clearStation() {
        getPositionDao().deleteAll();
    }

    public static void deletePositionithId(long id) {
        getPositionDao().delete(getPositionForId(id));
    }

    public static Position getPositionForId(long id) {
        return getPositionDao().load(id);
    }

    public static ArrayList<Position> getAllPosition() {
        return (ArrayList<Position>) getPositionDao().loadAll();
    }

    private static PositionDao getPositionDao() {
        return MyApplication.getInstance().getDaoSession().getPositionDao();
    }
}
