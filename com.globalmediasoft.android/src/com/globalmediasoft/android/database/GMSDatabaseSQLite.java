package com.globalmediasoft.android.database;

import com.globalmediasoft.android.database.select.GMSDatabaseSelect;
import com.globalmediasoft.android.database.select.GMSDatabaseSelectSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class GMSDatabaseSQLite extends GMSDatabase {
	private static GMSDatabaseSQLiteHelper helper;
	
	public GMSDatabaseSQLite() {
		// TODO Auto-generated constructor stub
		super();
		if (helper == null) {
			try {
				throw new Exception("There is no database helper. Please make sure setDatabaseHelper is called");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		setupTable();
		createTable();
	}
	
	public static abstract class GMSDatabaseSQLiteHelper extends SQLiteOpenHelper {

		public GMSDatabaseSQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	public static void setDatabaseHelper(GMSDatabaseSQLiteHelper helper) {
		GMSDatabaseSQLite.helper = helper;
	}
	
	abstract protected void setupTable();
	
	public static SQLiteDatabase db() {		
		return helper.getWritableDatabase();
	}
	
	public static SQLiteDatabase db(boolean read) {		
		return read ? helper.getReadableDatabase() : helper.getWritableDatabase();
	}
	
	protected void createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + " (";
		int i = 0;
		for (TableColumn column : TBL_COLUMNS) {
			sql += column.name + " " + column.type + (column.primary ? " PRIMARY KEY" : "") + (column.auto_increment ? " AUTOINCREMENT" : "");
			sql += i < TBL_COLUMNS.size() - 1 ? ", " : ");";
			i++;
		}
		
		db().execSQL(sql);
	}
	
	public void dropTable(SQLiteDatabase db) {
		dropTable(db == null ? db() : db, TBL_NAME);
	}
	
	public static void dropTable(SQLiteDatabase db, String tbl_name) {
		db.execSQL("DROP TABLE IF EXISTS " + tbl_name + ";");
	}
	
	@Override
	public GMSDatabaseSelect select() {
		// TODO Auto-generated method stub
		return new GMSDatabaseSelectSQLite(getTableName(), getColumns());
	}

	@Override
	public Cursor query(String sql) {
		return execute(sql);
	}
	
	public static Cursor execute(String sql) {
		// TODO Auto-generated method stub
		return db().rawQuery(sql, null);
	}
	
	public static Cursor execute(GMSDatabaseSelect select) {
		// TODO Auto-generated method stub
		return execute(select.toString());
	}

	@Override
	public long insert(ContentValues values) {
		// TODO Auto-generated method stub
		try {
			return db().insert(getTableName(), null, values);
		} catch (SQLiteException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public long insert(GMSDatabase.Model model) {
		return db().insert(getTableName(), null, model.toContentValues());
	}

	@Override
	public long update(ContentValues values, String where) {
		// TODO Auto-generated method stub
		return db().update(getTableName(), values, where, null);
	}

	@Override
	public long delete(String where) {
		// TODO Auto-generated method stub
		return db().delete(getTableName(), where, null);
	}
}
