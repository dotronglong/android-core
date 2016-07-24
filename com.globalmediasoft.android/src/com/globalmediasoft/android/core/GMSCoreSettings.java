package com.globalmediasoft.android.core;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;

public class GMSCoreSettings {
	protected static Activity activity;
	protected static String appSIDSettings;
	
	protected static SharedPreferences getSettingsPreference() {
		return activity.getSharedPreferences(appSIDSettings, 0);
	}
	
	public static Activity getActivity() {
		return activity;
	}
	
	public static void setActivity(Activity activity) {
		GMSCoreSettings.activity = activity;
	}
	
	public static Resources getResources() {
		if (activity != null) {
			return activity.getResources();
		}
		
		return null;
	}
	
	public static void setInt(String key, int value) {
		getSettingsPreference().edit().putInt(key, value).commit();
	}
	
	public static void setFloat(String key, float value) {
		getSettingsPreference().edit().putFloat(key, value).commit();
	}
	
	public static void setLong(String key, long value) {
		getSettingsPreference().edit().putLong(key, value).commit();
	}
	
	public static void setBoolean(String key, boolean value) {
		getSettingsPreference().edit().putBoolean(key, value).commit();
	}
	
	public static void setString(String key, String value) {
		getSettingsPreference().edit().putString(key, value).commit();
	}
	
	public static void set(String key, String value) {
		setString(key, value);
	}
	
	public static int getInt(String key, int defValue) {
		return getSettingsPreference().getInt(key, defValue);
	}
	
	public static float getFloat(String key, float defValue) {
		return getSettingsPreference().getFloat(key, defValue);
	}
	
	public static long getLong(String key, long defValue) {
		return getSettingsPreference().getLong(key, defValue);
	}
	
	public static boolean getBoolean(String key, boolean defValue) {
		return getSettingsPreference().getBoolean(key, defValue);
	}
	
	public static String getString(String key, String defValue) {
		return getSettingsPreference().getString(key, defValue);
	}
	
	public static String get(String key, String defValue) {
		return getString(key, defValue);
	}
}
