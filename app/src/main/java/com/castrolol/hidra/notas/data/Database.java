package com.castrolol.hidra.notas.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by 'Luan on 28/11/2015.
 */
public class Database extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 2;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
