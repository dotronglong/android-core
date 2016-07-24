package com.globalmediasoft.android.database;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.globalmediasoft.android.database.select.GMSDatabaseSelect;
import com.globalmediasoft.android.database.select.GMSDatabaseSelect.QueryTable;

public abstract class GMSDatabase {
	
	public static boolean SQL_Log = false;

	protected String TBL_NAME;
	protected Vector<TableColumn> TBL_COLUMNS;
	protected Vector<TableColumn> TBL_PRIMARY;
	protected Class<? extends Model> modelClass;
	
	public GMSDatabase() {
		TBL_NAME	= "";
		TBL_COLUMNS = new Vector<TableColumn>();
		TBL_PRIMARY = new Vector<TableColumn>();
		modelClass  = Model.class;
	}
	
	public static class TableColumn {
		public String name;
		public String type;
		public boolean primary = false;
		public boolean auto_increment = false;
		
		public TableColumn(String name, String type) {
			this.name = name;
			this.type = type;
		}
		
		public TableColumn(String name, String type, boolean primary) {
			this.name = name;
			this.type = type;
			this.primary = primary;
		}	
		
		public TableColumn(String name, String type, boolean primary, boolean auto_increment) {
			this.name = name;
			this.type = type;
			this.primary = primary;
			this.auto_increment = auto_increment;
		}	
	}
	
	protected void setupTable() {
		// TODO Auto-generated method stub
		if (TBL_COLUMNS.size() > 0) {
			for (TableColumn column : TBL_COLUMNS) {
				if (column.primary) {
					TBL_PRIMARY.add(column);
				}
			}
		}
	}
	
	public GMSDatabase setTableName(String tbl_name) {
		TBL_NAME = tbl_name;
		return this;
	}
	
    public String getTableName() {
		return TBL_NAME;
	}
    
    public GMSDatabase addColumn(TableColumn column) {
    	TBL_COLUMNS.add(column);
    	return this;
    }
    
    public String[] getColumns() {
		String[] columns = new String[TBL_COLUMNS.size() + 1];
		columns[0] = "rowid _id";
		
		int i = 1;
		for (TableColumn column : TBL_COLUMNS) {
			columns[i] = "`" + column.name + "`";
			i++;
		}
		
		return columns;
	}
	
	public static class Model implements Parcelable {
		/**
		 * Check whether a cursor is valid and ready to use or not
		 * 
		 * @param cursor
		 * @return boolean true if yes, false if otherwise
		 */
		private static boolean isValid(Cursor cursor) {
			if (cursor.getCount() == 0) {
				return false;
			} else if (cursor.isBeforeFirst()) {
				cursor.moveToFirst();
			}
			
			return true;
		}
		
		public Model() {
			final Class<? extends Model> clss = this.getClass();
			CREATOR = new Parcelable.Creator<Model>() {
				public Model createFromParcel(Parcel in) {
					try {
						return clss.newInstance().parse(in);
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					return null;
				}
				
				public Model[] newArray(int size) {
		            return new Model[size];
		        }
			};
		}
		
		public Model(Parcel in) {
			parse(in);
		}
		
		public static Parcelable.Creator<Model> CREATOR;
		
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			String type;
			Field[] attributes = this.getClass().getFields();
			for (Field field : attributes) {
				if (field.getModifiers() != Modifier.PUBLIC) {
					continue;
				}
				
				type = field.getType().getSimpleName();
				try {
					if (type.equals("int")) {
						dest.writeInt(field.getInt(this));
					} else if (type.equals("String")) {
						dest.writeString((String) field.get(this));
					} else if (type.equals("double")) {
						dest.writeDouble(field.getDouble(this));
					} else if (type.equals("float")) {
						dest.writeFloat(field.getFloat(this));
					} else if (type.equals("long")) {
						dest.writeLong(field.getLong(this));
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * Parse a Parcel into Model
		 * 
		 * @param in
		 * @return {@link GMSDatabase.Model}
		 */
		public Model parse(Parcel in) {
			String type;
			Field[] attributes = this.getClass().getFields();
			for (Field field : attributes) {
				if (field.getModifiers() != Modifier.PUBLIC) {
					continue;
				}
				
				type = field.getType().getSimpleName();
				try {
					if (type.equals("int")) {
						field.setInt(this, in.readInt());
					} else if (type.equals("String")) {
						field.set(this, in.readString());
					} else if (type.equals("double")) {
						field.setDouble(this, in.readDouble());
					} else if (type.equals("float")) {
						field.setFloat(this, in.readFloat());
					} else if (type.equals("long")) {
						field.setLong(this, in.readLong());
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return this;
		}
		
		/**
		 * Parse Model into a Bundle
		 * 
		 * @param m
		 * @return {@link Bundle}
		 */
		public static Bundle parse(Model m) {
			String type = "";
			Bundle item = new Bundle();
			Field[] attributes =  m.getClass().getFields();
			for (Field field : attributes) {
				if (field.getModifiers() != Modifier.PUBLIC) {
					continue;
				}
			
				type = field.getType().getSimpleName();
				try {
					if (type.equals("int")) {
						item.putInt(field.getName(), field.getInt(m));
					} else if (type.equals("String")) {
						item.putString(field.getName(), (String) field.get(m));
					} else if (type.equals("double")) {
						item.putDouble(field.getName(), field.getDouble(m));
					} else if (type.equals("long")) {
						item.putLong(field.getName(), field.getLong(m));
					}
				}  catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return item;
		}
		
		/**
		 * Transform a model into Bundle itself
		 * 
		 * @return {@link Bundle}
		 */
		public Bundle toBundle() {
			Bundle item = new Bundle();
			String type = "";
			Field[] attributes =  this.getClass().getFields();
			for (Field field : attributes) {
				if (field.getModifiers() != Modifier.PUBLIC) {
					continue;
				}
				
				type = field.getType().getSimpleName();
				try {
					if (type.equals("int")) {
						item.putInt(field.getName(), field.getInt(this));
					} else if (type.equals("String")) {
						item.putString(field.getName(), (String) field.get(this));
					} else if (type.equals("double")) {
						item.putDouble(field.getName(), field.getDouble(this));
					} else if (type.equals("float")) {
						item.putFloat(field.getName(), field.getFloat(this));
					} else if (type.equals("long")) {
						item.putLong(field.getName(), field.getLong(this));
					} else if (type.equals("boolean")) {
						item.putBoolean(field.getName(), field.getBoolean(this));
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return item;
		}
		
		/**
		 * Transform a Model into ContentValues itself
		 * 
		 * @return {@link ContentValues}
		 */
		public ContentValues toContentValues() {
			ContentValues item = new ContentValues();
			String type = "";
			Field[] attributes =  this.getClass().getFields();
			for (Field field : attributes) {
				if (field.getModifiers() != Modifier.PUBLIC) {
					continue;
				}
				
				type = field.getType().getSimpleName();
				try {
					if (type.equals("int")) {
						item.put(field.getName(), field.getInt(this));
					} else if (type.equals("String")) {
						item.put(field.getName(), (String) field.get(this));
					} else if (type.equals("double")) {
						item.put(field.getName(), field.getDouble(this));
					} else if (type.equals("float")) {
						item.put(field.getName(), field.getFloat(this));
					} else if (type.equals("long")) {
						item.put(field.getName(), field.getLong(this));
					} else if (type.equals("boolean")) {
						item.put(field.getName(), field.getBoolean(this));
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return item;
		}
		
		/**
		 * Parse a Cursor into a Model
		 * 
		 * @param cursor
		 * @return {@link GMSDatabase.Model}
		 */
		public Model parse(Cursor cursor) {
			if (!isValid(cursor)) {
				return null;
			}
			
			String type = "";
			Field[] attributes =  this.getClass().getFields();
			for (Field field : attributes) {
				int columnIndex = cursor.getColumnIndex(field.getName());
				if (columnIndex == -1 || field.getModifiers() != Modifier.PUBLIC) {
					continue;
				}
				
				type = field.getType().getSimpleName();
				try {
					if (type.equals("int")) {
						field.setInt(this, cursor.getInt(columnIndex));
					} else if (type.equals("String")) {
						field.set(this, cursor.getString(columnIndex));
					} else if (type.equals("double")) {
						field.setDouble(this, cursor.getDouble(columnIndex));
					} else if (type.equals("float")) {
						field.setFloat(this, cursor.getFloat(columnIndex));
					} else if (type.equals("long")) {
						field.setLong(this, cursor.getLong(columnIndex));
					} else if (type.equals("boolean")) {
						int value = cursor.getInt(columnIndex);
						field.setBoolean(this, value == 0 ? false : true);
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return this;
		}
		
		/**
		 * Parse a set of rows inside a Cursor into set of Model
		 * 
		 * @param cursor
		 * @return Vector<{@link GMSDatabase.Model}>
		 */
		public Vector<? extends Model> parseAll(Cursor cursor) {
			Vector<Model> items = new Vector<Model>();
			if (isValid(cursor)) {
				while (!cursor.isAfterLast()) {
					try {
						items.add(this.getClass().newInstance().parse(cursor));
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cursor.moveToNext();
				}
			}
			
			return items;
		}
		
		/**
		 * Parse a {@link JSONObject} into Model
		 * @param o
		 * @return {@link Model}
		 */
		public Model parse(JSONObject o) {
			String type = "", name ="";
			Field[] attributes =  this.getClass().getFields();
			for (Field field : attributes) {
				name = field.getName();
				if (!o.has(name) || field.getModifiers() != Modifier.PUBLIC) {
					continue;
				}
				
				type = field.getType().getSimpleName();
				try {
					if (type.equals("int")) {
						field.setInt(this, o.getInt(name));
					} else if (type.equals("String")) {
						field.set(this, o.getString(name));
					} else if (type.equals("double")) {
						field.setDouble(this, o.getDouble(name));
					} else if (type.equals("long")) {
						field.setLong(this, o.getLong(name));
					} else if (type.equals("boolean")) {
						try {
							field.setBoolean(this, o.getBoolean(name));
						} catch (JSONException e) {
							int value = o.getInt(name);
							field.setBoolean(this, value == 0 ? false : true);
						}
					}
				}  catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return this;
		}
		
		public Vector<? extends Model> parse(JSONArray array) {
			Vector<Model> items = new Vector<Model>();
			if (array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					try {
						items.add(this.getClass().newInstance().parse(array.getJSONObject(i)));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return items;
		}
		
		public void parse(SharedPreferences sharedPreferences) {
			String type = "", name ="";
			Field[] attributes =  this.getClass().getFields();
			for (Field field : attributes) {
				name = field.getName();
				if (!sharedPreferences.contains(name) || field.getModifiers() != Modifier.PUBLIC) {
					continue;
				}
				
				type = field.getType().getSimpleName();
				try {
					if (type.equals("int")) {
						field.setInt(this, sharedPreferences.getInt(name, 0));
					} else if (type.equals("String")) {
						field.set(this, sharedPreferences.getString(name, ""));
					} else if (type.equals("float")) {
						field.setDouble(this, sharedPreferences.getFloat(name, 0));
					} else if (type.equals("long")) {
						field.setLong(this, sharedPreferences.getLong(name, 0));
					}
				}  catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
		
	/**
	 * Get Select Adapter
	 * @return GMSDatabaseSelectAbstract
	 */
	abstract public GMSDatabaseSelect select();
	
	/**
	 * Execute a single query
	 * @param sql
	 * @return Cursor
	 */
	abstract public Cursor query(String sql);
	
	/**
	 * Insert a record
	 * @param values
	 * @return long the last insertId if possible
	 */
	abstract public long insert(ContentValues values);
	
	/**
	 * Update table with supplied data
	 * 
	 * @param values
	 * @param where
	 * @return long how many rows effect
	 */
	abstract public long update(ContentValues values, String where);
	
	/**
	 * Delete from table
	 * @param where
	 * @return long how many rows effect
	 */
	abstract public long delete(String where);
	
	/**
	 * Fetch a row by selector
	 * 
	 * @param sql
	 * @return {@link GMSDatabase.Model}
	 */
	public Model fetchRow(String sql) {
		try {
			return modelClass.newInstance().parse(query(sql));
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Fetch all rows by selector
	 * 
	 * @param sql
	 * @return Vector<{@link GMSDatabase.Model}>
	 */
	public Vector<? extends Model> fetchAll(String sql) {
		try {
			return modelClass.newInstance().parseAll(query(sql));
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Fetch all rows
	 * 
	 * @return Vector<{@link GMSDatabase.Model}>
	 */
	public Vector<? extends Model> fetchAll() {
		try {
			return modelClass.newInstance().parseAll(query(select().toString()));
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Fetch a row by selector
	 * 
	 * @param select
	 * @return {@link GMSDatabase.Model}
	 */
	public Model fetchRow(GMSDatabaseSelect select) {
		return fetchRow(select.limit(1).toString());
	}
	
	/**
	 * Fetch all rows by selector
	 * 
	 * @param select
	 * @return Vector<{@link GMSDatabase.Model}>
	 */
	public Vector<? extends Model> fetchAll(GMSDatabaseSelect select) {
		return fetchAll(select.toString());
	}
	
	public long insert(Model model) {
		return insert(model.toContentValues());
	}
    
    public static String escape(String query) {
    	return query.replace("'", "''");
    }
    
    public static String bind(String query, Object param) {
    	return bind(query, "?", param);
    }
    
    public static String bind(String query, String key, Object param) {
    	return query.replace(key, "'" + escape(String.valueOf(param)) + "'");
    }
	
	public long delete(String where, Object param) {
		return delete(GMSDatabaseSelect.bind(where, "?", param));
	}
	
	public static QueryTable QueryTable(String name) {
    	return new GMSDatabaseSelect.QueryTable(name);
    }
    
    public static QueryTable QueryTable(String name, String alias) {
    	return new GMSDatabaseSelect.QueryTable(name, alias);
    }
}