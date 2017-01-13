package com.example.hb.monprojetvelo.main;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.otto.Bus;

import greendao.DaoMaster;
import greendao.DaoSession;

/**
 * Created by hb on 10/01/2017.
 */
public class MyApplication extends Application {

    private DaoSession daoSession;
    private static Bus eventBus;
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        setupDatabase();
        eventBus = new Bus();
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "mytable-db",
                null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static Bus getEventBus() {
        return eventBus;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
