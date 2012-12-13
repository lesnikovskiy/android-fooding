package com.fooding.adapters;

import static com.fooding.utils.DbConsts.CREATE_EVENTS_SQL;
import static com.fooding.utils.DbConsts.CREATE_PRODUCTS_SQL;
import static com.fooding.utils.DbConsts.MIGRATE_EVENTS_SQL;
import static com.fooding.utils.DbConsts.MIGRATE_PRODUCTS_SQL;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OpenHelper extends SQLiteOpenHelper {
	static final private String TAG = "OpenHelper";
	
	public OpenHelper(Context context, String name, CursorFactory factory, int version) {		
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "executing sql command: " + CREATE_PRODUCTS_SQL);
		db.execSQL(CREATE_PRODUCTS_SQL);
		Log.d(TAG, "executing sql command: " + CREATE_EVENTS_SQL);
		db.execSQL(CREATE_EVENTS_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "executing sql command: " + MIGRATE_PRODUCTS_SQL);		
		db.execSQL(MIGRATE_PRODUCTS_SQL);
		Log.d(TAG, "executing sql command: " + MIGRATE_EVENTS_SQL);
		db.execSQL(MIGRATE_EVENTS_SQL);
		onCreate(db);
	}		
}
