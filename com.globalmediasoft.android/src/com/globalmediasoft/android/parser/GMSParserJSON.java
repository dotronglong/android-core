package com.globalmediasoft.android.parser;

import org.json.JSONException;
import org.json.JSONObject;

public class GMSParserJSON extends JSONObject {
	public GMSParserJSON(String content) throws JSONException {
		// TODO Auto-generated constructor stub
		super(content);
	}

	public GMSParserJSON() {
		// TODO Auto-generated constructor stub
		super();
	}

	public boolean getBoolean(String name, boolean def) {
		try {
			return getBoolean(name);
		} catch (JSONException e) {
			return def;
		}
	}
	
	public double getDouble(String name, double def) {
		try {
			return getDouble(name);
		} catch (JSONException e) {
			return def;
		}
	}
	
	public int getInt(String name, int def) {
		try {
			return getInt(name);
		} catch (JSONException e) {
			return def;
		}
	}
	
	public long getLong(String name, long def) {
		try {
			return getLong(name);
		} catch (JSONException e) {
			return def;
		}
	}
	
	public String getString(String name, String def) {
		try {
			return getString(name);
		} catch (JSONException e) {
			return def;
		}
	}
	
	public GMSParserJSON getJSONObject(String name) {
        return getJSONObject(name, new GMSParserJSON());
    }
	
	public GMSParserJSON getJSONObject(String name, GMSParserJSON def) {
		Object object;
		try {
			object = get(name);
			if (object instanceof GMSParserJSON) {
	            return (GMSParserJSON) object;
	        } else if (object instanceof JSONObject) {
	        	return new GMSParserJSON(object.toString());
	        }
		} catch (JSONException e) {
			
		}
		
		return def;
	}
}
