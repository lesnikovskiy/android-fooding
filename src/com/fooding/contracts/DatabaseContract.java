package com.fooding.contracts;

import android.database.sqlite.SQLiteException;

public interface DatabaseContract {
	void open() throws SQLiteException;
	void close();
}
