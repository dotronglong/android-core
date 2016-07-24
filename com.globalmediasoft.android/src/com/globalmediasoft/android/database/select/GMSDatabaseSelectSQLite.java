package com.globalmediasoft.android.database.select;

public class GMSDatabaseSelectSQLite extends GMSDatabaseSelect {

	public GMSDatabaseSelectSQLite(String table) {
		// TODO Auto-generated constructor stub
		super(table);
	}
	
	public GMSDatabaseSelectSQLite(String table, String[] columns) {
		// TODO Auto-generated constructor stub
		super(table);
		columns(columns);
	}
}