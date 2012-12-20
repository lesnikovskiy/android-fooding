package com.fooding.adapters;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class DbAdapter {
	private static final String SQLITE_SEQUENCE = "sqlite_sequence";
	private static final String SELECTION = "name = ?";
	private static final String SEQ = "seq";
	
	abstract SQLiteDatabase getDatabase();
	
	public final long getLastInsertedId(String tableName) {
		long index = 0;
		
		SQLiteDatabase db = getDatabase();
		if (db == null)
			throw new NullPointerException("SQLiteDatabase is null. Override getDatabase() method correctly.");
		
		Cursor c = db.query(SQLITE_SEQUENCE, new String[] {tableName}, SELECTION, null, null, null, null);
		if (c.moveToFirst()) {
			index = c.getLong(c.getColumnIndex(SEQ));
		}
		
		return index;
	}
}
