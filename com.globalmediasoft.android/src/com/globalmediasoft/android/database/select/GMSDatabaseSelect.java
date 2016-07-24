package com.globalmediasoft.android.database.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.globalmediasoft.android.database.GMSDatabase;

import android.os.Bundle;
import android.util.Log;

public abstract class GMSDatabaseSelect {
	public static String SQL_WILDCARD   = "*";
	public static String SQL_SELECT     = "SELECT";
	public static String SQL_UNION      = "UNION";
	public static String SQL_UNION_ALL  = "UNION ALL";
	public static String SQL_FROM       = "FROM";
	public static String SQL_JOIN       = "JOIN";
	public static String SQL_LEFT_JOIN  = "LEFT JOIN";
    public static String SQL_RIGHT_JOIN = "RIGHT JOIN";
    public static String SQL_WHERE      = "WHERE";
    public static String SQL_DISTINCT   = "DISTINCT";
    public static String SQL_GROUP_BY   = "GROUP BY";
    public static String SQL_ORDER_BY   = "ORDER BY";
    public static String SQL_HAVING     = "HAVING";
    public static String SQL_LIMIT      = "LIMIT";
    public static String SQL_AND        = "AND";
    public static String SQL_AS         = "AS";
    public static String SQL_OR         = "OR";
    public static String SQL_ON         = "ON";
    public static String SQL_ASC        = "ASC";
    public static String SQL_DESC       = "DESC";
    
    protected static String DISTINCT       = "distinct";
    protected static String COLUMNS        = "columns";
    protected static String FROM           = "from";
    protected static String UNION          = "union";
    protected static String JOIN           = "join";
    protected static String WHERE          = "where";
    protected static String GROUP          = "group";
    protected static String HAVING         = "having";
    protected static String ORDER          = "order";
    protected static String LIMIT          = "limit";

    protected static int INNER_JOIN 	= 0;
    protected static int LEFT_JOIN 		= 1;
    protected static int RIGHT_JOIN 	= 2;
    
    protected static int AND 	= 1;
    protected static int OR 	= 2;
    
    protected String sql;
    protected Bundle data;
    protected ArrayList<QueryParam> params;
    protected ArrayList<QueryWhere> wheres;
    protected ArrayList<QueryJoin>  joins;
    
    GMSDatabaseSelect() {
    	data 	= new Bundle();
    	params 	= new ArrayList<QueryParam>();
    	wheres  = new ArrayList<QueryWhere>();
    	joins   = new ArrayList<QueryJoin>();
    }
    
    GMSDatabaseSelect(String table) {
    	data = new Bundle();
    	data.putStringArray(FROM, new String[]{table});
    	data.putStringArray(COLUMNS, new String[]{SQL_WILDCARD});
    	
    	params 	= new ArrayList<QueryParam>();
    	wheres 	= new ArrayList<QueryWhere>();
    	joins  	= new ArrayList<QueryJoin>();
    }
    
    public static String escape(String query) {
    	return GMSDatabase.escape(query);
    }
    
    public static class QueryWhere {
    	private boolean AND = true;
    	private String value;
    	
    	public QueryWhere(String value) {
    		this.value = value;
    	}
    	
    	public QueryWhere(String value, boolean AND) {
    		this.value = value;
    		this.AND   = AND;
    	}
    	
    	public boolean AND() {
    		return AND;
    	}
    	
    	public String getValue() {
    		return value;
    	}
    }
    
    public static class QueryParam {
    	private String key;
    	private String value;
    	
    	public QueryParam(String key, String value) {
    		this.key = key;
    		this.value = value;
    	}
    	
    	public String getKey() {
    		return key;
    	}
    	
    	public String getValue() {
    		return escape(value);
    	}
    	
    	public String getValue(boolean original) {
    		return original ? value : getValue();
    	}
    }
    
    public GMSDatabaseSelect setParams(QueryParam[] params) {
    	return setParams(params, true);
    }
    
    public GMSDatabaseSelect setParams(QueryParam[] params, boolean merge) {
    	if (params != null && params.length > 0) {
    		if (!merge) {
    			this.params.clear();
    		}
    		
    		for (int i = 0; i < params.length; i++) {
    			this.params.add(params[i]);
    		}
    	}
    	
    	return this;
    }
    
    public class QueryJoin {
    	public int type = INNER_JOIN;
    	public QueryTable table;
    	public String on;
    	public String[] columns;
    	
    	public QueryJoin(QueryTable table, String on, String[] columns, int type) {
    		this.table 		= table;
    		this.on 		= on;
    		this.columns	= columns;
    		this.type		= type;
    	}
    }
    
    public static class QueryTable {
    	public String name;
    	public String alias = "";
    	
    	public QueryTable(String name) {
    		this.name = name;
    	}
    	
    	public QueryTable(String name, String alias) {
    		this.name  = name;
    		this.alias = alias;
    	}
    	
    	public String getName() {
    		return "`" + name + "`";
    	}
    	
    	public String getAlias() {
    		return "`" + alias + "`";
    	}
    	
    	public String getFullName() {
    		return getName() + (alias.isEmpty() ? "" : " " + getAlias());
    	}
    }
    
    protected String[] merge(String[] a, String[] b) {
    	ArrayList<String> c = new ArrayList<String>(Arrays.asList(a));
    	c.addAll(Arrays.asList(b));
    	
    	return (String[]) c.toArray();
    }
    
    public GMSDatabaseSelect columns(String[] columns) {
    	return columns(columns, false);
    }
    
    public GMSDatabaseSelect columns(String[] columns, String table) {
    	for (int i = 0; i < columns.length; i++) {
    		columns[i] = table + "." + columns[i];
    	}
    	return columns(columns, false);
    }
    
    public GMSDatabaseSelect columns(String[] columns, boolean merge) {
    	data.putStringArray(COLUMNS, merge ? merge(data.getStringArray(COLUMNS), columns) : columns);
    	return this;
    }
    
    public GMSDatabaseSelect from(QueryTable table) {
    	return from(table, false);
    }
    
    public GMSDatabaseSelect from(QueryTable table, boolean merge) {
    	return from(new QueryTable[]{table}, merge);
    }
    
    public GMSDatabaseSelect from(QueryTable[] tables) {
    	return from(tables, false);
    }
    
    public GMSDatabaseSelect from(QueryTable[] tables, boolean merge) {    	
    	String[] _tables = new String[tables.length];
    	for (int i = 0; i < tables.length; i++) {
    		_tables[i] = tables[i].getFullName();
    	}
    	
    	data.putStringArray(FROM, merge ? merge(data.getStringArray(FROM), _tables) : _tables);
    	return this;
    }
    
    public GMSDatabaseSelect distinct(boolean distinct) {
    	data.putBoolean(DISTINCT, distinct);
    	return this;
    }
    
    public GMSDatabaseSelect group(String group) {
    	data.putString(GROUP, group);
    	return this;
    }
    
    public GMSDatabaseSelect having(String having) {
    	data.putString(HAVING, having);
    	return this;
    }
    
    public GMSDatabaseSelect limit(int limit) {
    	data.putString(LIMIT, "0, " + String.valueOf(limit));
    	return this;
    }
    
    public GMSDatabaseSelect limit(int start, int limit) {
    	data.putString(LIMIT, String.valueOf(start) +  ", " + String.valueOf(limit));
    	return this;
    }
    
    public GMSDatabaseSelect order(String order) {
    	return order(new String[]{order});
    }
    
    public GMSDatabaseSelect order(String[] order) {
    	data.putStringArray(ORDER, order);
    	return this;
    }
    
    public GMSDatabaseSelect where(String where, QueryParam[] params) {
    	wheres.add(new QueryWhere(where));
    	setParams(params);
    	
    	return this;
    }
    
    public GMSDatabaseSelect orWhere(String where, QueryParam[] params) {
    	wheres.add(new QueryWhere(where, false));
    	setParams(params);
    	
    	return this;
    }
    
    public GMSDatabaseSelect where(String where, Object param) {
    	wheres.add(new QueryWhere(bind(where, "?", param)));
    	
    	return this;
    }
    
    public GMSDatabaseSelect orWhere(String where, Object param) {
    	wheres.add(new QueryWhere(bind(where, "?", param), false));
    	
    	return this;
    }
    
    public static String bind(String query, String key, Object param) {
    	return GMSDatabase.bind(query, key, param);
    }
    
    public GMSDatabaseSelect join(QueryTable table, String on, String[] columns, int type) {
    	joins.add(new QueryJoin(table, on, columns, type));
    	if (columns != null) {
    		columns(columns, table.alias.isEmpty() ? table.name : table.alias);
    	}
    	
    	return this;
    }
    
    public GMSDatabaseSelect join(QueryTable table, String on, String[] columns) {
    	return join(table, on, columns, INNER_JOIN);
    }
    
    public GMSDatabaseSelect joinLeft(QueryTable table, String on, String[] columns) {
    	return join(table, on, columns, LEFT_JOIN);
    }
    
    public GMSDatabaseSelect joinRight(QueryTable table, String on, String[] columns) {
    	return join(table, on, columns, RIGHT_JOIN);
    }
    
    public String toString() {
    	String sql = SQL_SELECT;
    	int i = 0;
    	
    	String[] columns 	= data.getStringArray(COLUMNS);
    	String _columns		= "";
    	for (i = 0; i < columns.length; i++) {
    		_columns += (i > 0 ? ", " : "") + columns[i];
    	}
    	sql += " " + _columns;
    	
    	String[] tables = data.getStringArray(FROM);
    	String _from 	= "";
    	for (i = 0; i < tables.length; i++) {
    		_from += (i > 0 ? ", " : "") + tables[i];
    	}
    	sql += " " + SQL_FROM + " " + _from;
    	
    	if (joins.size() > 0) {
    		String _join = "";
    		Iterator<QueryJoin> iterator = joins.iterator();
    		while (iterator.hasNext()) {
    			QueryJoin item = iterator.next();
    			_join += " ";
    			if (item.type == INNER_JOIN) {
    				_join += SQL_JOIN;
    			} else if (item.type == LEFT_JOIN) {
    				_join += SQL_LEFT_JOIN;
    			} else if (item.type == RIGHT_JOIN) {
    				_join += SQL_RIGHT_JOIN;
    			}
    			_join += " " + item.table.getFullName() + " " + SQL_ON + " " + item.on;
    		}
    		
    		if (!_join.isEmpty()) {
    			sql += _join;
    		}
    	}
    	
    	if (wheres.size() > 0) {
    		String _where = "";
    		boolean flag  = false;
    		Iterator<QueryWhere> iterator = wheres.iterator();
    		while (iterator.hasNext()) {
    			QueryWhere item = iterator.next();
    			if (flag) {
    				_where += " " + (item.AND() ? SQL_AND : SQL_OR);
    			} else {
    				flag = true;
    			}
    			
    			_where += " (" + item.getValue() + ")"; 
    		}
    		
    		if (!_where.isEmpty()) {
    			sql += " " + SQL_WHERE + _where;
    		}
    	}
    	
    	String group = data.containsKey(GROUP) ? data.getString(GROUP) : "";
    	if (!group.isEmpty()) {
    		sql += " " + SQL_GROUP_BY + " " + group;
    	}
    	
    	String having = data.containsKey(HAVING) ? data.getString(HAVING) : "";
    	if (!having.isEmpty()) {
    		sql += " " + SQL_HAVING + " " + having;
    	}
    	
    	String[] orders = data.containsKey(ORDER) ? data.getStringArray(ORDER) : null;
    	if (orders != null && orders.length > 0) {
    		sql += " " + SQL_ORDER_BY;
    		for (i = 0; i < orders.length; i++) {
    			sql += (i == 0 ? " " : ", ") + orders[i];
    		}
    	}
    	
    	String limit = data.containsKey(LIMIT) ? data.getString(LIMIT) : "";
    	if (!limit.isEmpty()) {
    		sql += " " + SQL_LIMIT + " " + limit;
    	}
    	
    	if (GMSDatabase.SQL_Log) {
    		Log.d("SQL_LOG", sql);
    	}
    	
    	return sql;
    }
}
